//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.controllers;

import com.proyectoWeb.ProyectoWeb.entity.ServiceEntity;
import com.proyectoWeb.ProyectoWeb.services.impl.ServiceEntityService;
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
@RequestMapping("/clients/services")
public class ServiceController {
    @Autowired
    private ServiceEntityService service;

    @GetMapping
    public List<ServiceEntity> viewAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id){
        ServiceEntity serviceEntity=null;
        try {
            Optional<ServiceEntity> optionalService=service.findById(id);
            if(optionalService.isPresent()){
                serviceEntity=optionalService.orElseThrow();
            }
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(serviceEntity,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> save(@Valid @RequestBody ServiceEntity serviceEntity,BindingResult result){
        if (result.hasFieldErrors()){
            return validation(result);
        }
        ServiceEntity serviceEntityDB;
        try {
            serviceEntityDB=service.save(serviceEntity);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(serviceEntityDB,HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody ServiceEntity serviceEntity,BindingResult result, @PathVariable Long id){
        if (result.hasFieldErrors()){
            return validation(result);
        }
        Optional<ServiceEntity> optionalService;
        try{
            optionalService =service.update(id,serviceEntity);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (optionalService.isPresent()){
            return new ResponseEntity<>(optionalService.orElseThrow(), HttpStatus.CREATED);
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
