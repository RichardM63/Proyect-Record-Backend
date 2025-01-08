package com.proyectoWeb.ProyectoWeb.services;

import com.proyectoWeb.ProyectoWeb.entity.ServiceEntity;

import java.util.List;
import java.util.Optional;

public interface IServiceEntityService {
    List<ServiceEntity> findAll();
    Optional<ServiceEntity> findById(Long id);
    ServiceEntity save(ServiceEntity serviceEntity);
    Optional<ServiceEntity> update(Long id, ServiceEntity serviceEntity);
}
