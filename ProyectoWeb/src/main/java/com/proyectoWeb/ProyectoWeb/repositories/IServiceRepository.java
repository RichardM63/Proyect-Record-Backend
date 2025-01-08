package com.proyectoWeb.ProyectoWeb.repositories;

import com.proyectoWeb.ProyectoWeb.entity.ServiceEntity;
import org.springframework.data.repository.CrudRepository;

public interface IServiceRepository extends CrudRepository<ServiceEntity,Long> {
}
