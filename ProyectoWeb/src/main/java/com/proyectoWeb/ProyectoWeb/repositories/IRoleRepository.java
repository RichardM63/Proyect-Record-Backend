package com.proyectoWeb.ProyectoWeb.repositories;

import com.proyectoWeb.ProyectoWeb.entity.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IRoleRepository extends CrudRepository<Role,Long> {
    Optional<Role> findByName(String name);
}
