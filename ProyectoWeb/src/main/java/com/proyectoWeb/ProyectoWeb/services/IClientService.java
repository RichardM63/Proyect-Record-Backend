package com.proyectoWeb.ProyectoWeb.services;

import com.proyectoWeb.ProyectoWeb.entity.Client;
import com.proyectoWeb.ProyectoWeb.entity.dto.ClientDTO;
import com.proyectoWeb.ProyectoWeb.entity.dto.authentication.AuthRequestDTO;

import java.util.List;
import java.util.Optional;

public interface IClientService {
    List<ClientDTO> findAll();
    Optional<ClientDTO> findbyId(Long id);
    Optional<ClientDTO> findByUsername2(String username);
    ClientDTO save(Client client);
    Optional<ClientDTO> update(Long id, ClientDTO client);
    Optional<ClientDTO> suspend(Long id);
    boolean existsByUsername(String username);
    boolean existByDni(String dni);
    Optional<AuthRequestDTO> findByUsername(String username);
}
