package com.proyectoWeb.ProyectoWeb.repositories;

import com.proyectoWeb.ProyectoWeb.entity.Invoice;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IInvoiceRepository extends CrudRepository<Invoice,Long> {
    List<Invoice> findByClientId(Long clientId);
    Invoice findByOrderId(Long orderId);
}
