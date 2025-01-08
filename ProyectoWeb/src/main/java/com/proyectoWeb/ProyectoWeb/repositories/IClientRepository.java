package com.proyectoWeb.ProyectoWeb.repositories;

import com.proyectoWeb.ProyectoWeb.entity.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IClientRepository extends CrudRepository<Client,Long> {
    Optional<Client> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByDni(String dni);
}
