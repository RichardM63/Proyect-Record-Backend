package com.proyectoWeb.ProyectoWeb.repositories;

import com.proyectoWeb.ProyectoWeb.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IOrderRepository extends CrudRepository<Order,Long> {
    List<Order> findByClientId(Long clientId);
}
