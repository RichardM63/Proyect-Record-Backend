package com.proyectoWeb.ProyectoWeb.services;

import com.proyectoWeb.ProyectoWeb.entity.Client;
import com.proyectoWeb.ProyectoWeb.entity.Invoice;
import com.proyectoWeb.ProyectoWeb.entity.Order;
import com.proyectoWeb.ProyectoWeb.entity.dto.InvoiceDTO;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Optional;

public interface IInvoiceServices {
    List<InvoiceDTO> findAll();
    Optional<InvoiceDTO> findById(Long id);
    List<InvoiceDTO> findByClientId(Long id);
    InvoiceDTO findByOrderId(Long orderId);
    InvoiceDTO save(Invoice invoice, Client client, Order order);
    Optional<InvoiceDTO> update(Long id, Invoice invoice) throws MessagingException;
}
