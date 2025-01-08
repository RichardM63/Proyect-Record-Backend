package com.proyectoWeb.ProyectoWeb.services;

import com.proyectoWeb.ProyectoWeb.entity.Invoice;
import com.proyectoWeb.ProyectoWeb.entity.dto.InvoiceDTO;
import com.proyectoWeb.ProyectoWeb.entity.dto.OrderDTO;
import jakarta.mail.MessagingException;

import java.util.Map;

public interface IEmailNotify {
    void sendNotify(Map variables, String email) throws MessagingException;
    void sendProof(Map variables, String email) throws MessagingException;
}
