//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.controllers;

import com.proyectoWeb.ProyectoWeb.entity.Order;
import com.proyectoWeb.ProyectoWeb.entity.dto.OrderDTO;
import com.proyectoWeb.ProyectoWeb.services.impl.OrderService;
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
@RequestMapping("/clients/orders")
public class OrderController {
    @Autowired
    private OrderService service;

    @GetMapping
    public List<OrderDTO> viewAll(){
        return service.findAll();
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> view(@PathVariable Long id){
        OrderDTO orderDTO=null;
        try {
            Optional<OrderDTO> optionalOrderDTO=service.findById(id);
            if(optionalOrderDTO.isPresent()){
                orderDTO=optionalOrderDTO.orElseThrow();
            }
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderDTO,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewByClient(@PathVariable Long id){
        List<OrderDTO> orderDTO;
        try {
            orderDTO=service.findByClientId(id);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderDTO,HttpStatus.OK);
    }

    @PostMapping("/create/{idClient}/{idService}/{idAddress}")
    public ResponseEntity<?> save(@RequestBody Order order,@PathVariable Long idClient, @PathVariable Long idService,@PathVariable Long idAddress){
        OrderDTO orderDTO;
        try {
            orderDTO=service.save(order,idClient,idService,idAddress);
        }catch (DataAccessException | MessagingException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderDTO,HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Order order,BindingResult result, @PathVariable Long id){
        if (result.hasFieldErrors()){
            return validation(result);
        }
        Optional<OrderDTO> optionalOrderDTO;
        try{
            optionalOrderDTO =service.update(id,order);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        System.out.println(optionalOrderDTO.isPresent());
        if (optionalOrderDTO.isPresent()){
            return new ResponseEntity<>(optionalOrderDTO.orElseThrow(), HttpStatus.CREATED);
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
