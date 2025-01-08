package com.proyectoWeb.ProyectoWeb.validation;

import com.proyectoWeb.ProyectoWeb.services.impl.ClientService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistByUsernameValidation implements ConstraintValidator<ExistByUsername,String> {

    @Autowired
    private ClientService service;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (service==null){
            return true;
        }
        return !service.existsByUsername(username);
    }
}
