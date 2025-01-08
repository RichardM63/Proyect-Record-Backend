package com.proyectoWeb.ProyectoWeb.validation;

import com.proyectoWeb.ProyectoWeb.services.impl.ClientService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistByDniValidation implements ConstraintValidator<ExistByDni,String> {

    @Autowired
    private ClientService service;

    @Override
    public boolean isValid(String dni, ConstraintValidatorContext constraintValidatorContext) {
        if(service==null){
            return true;
        }
        return !service.existByDni(dni);
    }
}
