package com.proyectoWeb.ProyectoWeb.services;

import com.proyectoWeb.ProyectoWeb.entity.Client;
import com.proyectoWeb.ProyectoWeb.entity.Order;
import com.proyectoWeb.ProyectoWeb.entity.ServiceEntity;
import com.proyectoWeb.ProyectoWeb.entity.dto.OrderDTO;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<OrderDTO> findAll();
    Optional<OrderDTO> findById(Long id);
    List<OrderDTO> findByClientId(Long id);
    OrderDTO save(Order order, Long idClient, Long idService, Long idAddress) throws MessagingException;
    Optional<OrderDTO> update(Long id, Order order);
}
