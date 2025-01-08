//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.controllers;

import com.proyectoWeb.ProyectoWeb.entity.dto.AddressDTO;
import com.proyectoWeb.ProyectoWeb.entity.Address;
import com.proyectoWeb.ProyectoWeb.services.impl.AddressService;
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
@RequestMapping("/clients/address")
public class AddressController {
    @Autowired
    private AddressService service;

    @GetMapping("/{idClient}")
    public ResponseEntity<?> viewAddress(@PathVariable Long idClient){
        List<AddressDTO> address;
        try {
            address=service.findByClient(idClient);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(address,HttpStatus.OK);
    }

    @PostMapping("/create/{idClient}")
    public ResponseEntity<?> save(@Valid @RequestBody Address address,BindingResult result,@PathVariable Long idClient){
        if (result.hasFieldErrors()){
            return validation(result);
        }
        AddressDTO addressDB;
        try {
            addressDB=service.save(address,idClient);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(addressDB,HttpStatus.CREATED);
    }

    @PutMapping("/{idClient}")
    public ResponseEntity<?> update(@Valid @RequestBody Address address,BindingResult result, @PathVariable Long idClient){
        if (result.hasFieldErrors()){
            return validation(result);
        }
        Optional<AddressDTO> optionalAddress;
        try{
            optionalAddress =service.update(idClient,address);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (optionalAddress.isPresent()){
            return new ResponseEntity<>(optionalAddress.orElseThrow(), HttpStatus.CREATED);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id){
        try {
            service.delete(id);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("La direccion se ha eliminado exitosamente",HttpStatus.OK);
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String,Object> errors = new HashMap<>();

        result.getFieldErrors().forEach(err ->{
            errors.put(err.getField(),"El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
        });

        return ResponseEntity.badRequest().body(errors);
    }


}
