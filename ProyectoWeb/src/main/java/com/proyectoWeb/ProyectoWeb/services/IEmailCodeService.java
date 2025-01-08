package com.proyectoWeb.ProyectoWeb.services;

import jakarta.mail.MessagingException;

public interface IEmailCodeService {
    public String sendEmailCode(String username,String email) throws MessagingException;
    public String generadorCode();
    public String obtenerFecha();
}
