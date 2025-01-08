//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.controllers;

import com.proyectoWeb.ProyectoWeb.entity.Invoice;
import com.proyectoWeb.ProyectoWeb.entity.dto.InvoiceDTO;
import com.proyectoWeb.ProyectoWeb.services.impl.InvoiceService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081",originPatterns = "*")
@RestController
@RequestMapping("/clients/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceService service;

    @GetMapping
    public List<InvoiceDTO> viewAll(){
        return service.findAll();
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<?> view(@PathVariable Long id){
        InvoiceDTO invoiceDTO=null;
        try {
            Optional<InvoiceDTO> optionalInvoiceDTO=service.findById(id);
            if(optionalInvoiceDTO.isPresent()){
                invoiceDTO=optionalInvoiceDTO.orElseThrow();
            }
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(invoiceDTO,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewByClient(@PathVariable Long id){
        List<InvoiceDTO> invoiceDTO;
        try {
            invoiceDTO= service.findByClientId(id);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(invoiceDTO,HttpStatus.OK);
    }

    @GetMapping("/order/{idOrder}")
    public ResponseEntity<?> viewByOrder(@PathVariable Long idOrder){
        InvoiceDTO invoiceDTO;
        try {
            invoiceDTO= service.findByOrderId(idOrder);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(invoiceDTO,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Invoice invoice, BindingResult result, @PathVariable Long id) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }

        Optional<InvoiceDTO> optionalInvoiceDTO;
        try {
            optionalInvoiceDTO = service.update(id, invoice);
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("Message", "Se ha generado un error");
            response.put("Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        if (optionalInvoiceDTO.isPresent()) {
            return new ResponseEntity<>(optionalInvoiceDTO.orElseThrow(), HttpStatus.CREATED);
        }

        return ResponseEntity.notFound().build();
    }
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String,Object> errors = new HashMap<>();

        result.getFieldErrors().forEach(err ->{
            errors.put(err.getField(),"El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
