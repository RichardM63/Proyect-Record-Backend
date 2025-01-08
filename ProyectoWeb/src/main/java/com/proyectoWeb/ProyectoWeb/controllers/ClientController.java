//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.controllers;

import com.proyectoWeb.ProyectoWeb.entity.Client;
import com.proyectoWeb.ProyectoWeb.entity.dto.ClientDTO;
import com.proyectoWeb.ProyectoWeb.services.impl.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8081",originPatterns = "*")
@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientService service;

    @GetMapping
    public List<ClientDTO> viewAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id){
        ClientDTO client=null;
        try {
            Optional<ClientDTO> clientOptional=service.findbyId(id);
            if(clientOptional.isPresent()){
                client=clientOptional.orElseThrow();
            }
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(client,HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> viewByUsername(@PathVariable String username){
        ClientDTO client=null;
        try {
            Optional<ClientDTO> clientOptional=service.findByUsername2(username);
            if(clientOptional.isPresent()){
                client=clientOptional.orElseThrow();
            }
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(client,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> save(@Valid @RequestBody Client client,BindingResult result){
        if (result.hasFieldErrors()){
            return validation(result);
        }
        ClientDTO clientDB;
        try {
            clientDB=service.save(client);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(clientDB,HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Client client,BindingResult result){
        if (result.hasFieldErrors()){
            return validation(result);
        }
        ClientDTO clientDB;
        try {
            client.setAdmin(false);
            clientDB=service.save(client);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(clientDB,HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody ClientDTO client,BindingResult result, @PathVariable Long id){
        if (result.hasFieldErrors()){
            return validation(result);
        }
        Optional<ClientDTO> optionalClientDTO;
        try{
            optionalClientDTO =service.update(id,client);
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (optionalClientDTO.isPresent()){
            return new ResponseEntity<>(optionalClientDTO.orElseThrow(), HttpStatus.CREATED);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/suspend/{id}")
    public ResponseEntity<?> suspend(@PathVariable Long id){
        ClientDTO client=null;
        try {
            Optional<ClientDTO> clientOptional=service.suspend(id);
            if(clientOptional.isPresent()){
                client=clientOptional.orElseThrow();
            }
        }catch (DataAccessException e){
            Map<String, Object> response=new HashMap<>();
            response.put("Message","Se ha generado un error");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(client,HttpStatus.CREATED);
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String,Object> errors = new HashMap<>();

        result.getFieldErrors().forEach(err ->{
            errors.put(err.getField(),"El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
