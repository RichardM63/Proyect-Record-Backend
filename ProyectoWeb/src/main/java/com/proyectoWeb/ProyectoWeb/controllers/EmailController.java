//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.controllers;

import com.proyectoWeb.ProyectoWeb.services.impl.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService service;

    @PostMapping("/create/{username}/{email}")
    public ResponseEntity<?> sendEmail(@PathVariable String username, @PathVariable String email){
        Map response = new HashMap();
        try{
            System.out.println(email);
            String codigo = service.sendEmailCode(username,email);
            response.put("message","Se ha enviado el correo con exito");
            response.put("codigo",codigo);
                return new ResponseEntity<>(response,HttpStatus.OK);
            } catch (MessagingException e) {
            response.put("message","Se ha generado un error al crear el codigo");
            response.put("error",e.getMessage());
                return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
            }
    }
}
