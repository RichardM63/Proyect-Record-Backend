package com.proyectoWeb.ProyectoWeb.services;

import com.proyectoWeb.ProyectoWeb.entity.dto.AddressDTO;
import com.proyectoWeb.ProyectoWeb.entity.Address;

import java.util.List;
import java.util.Optional;

public interface IAddressService {
    List<AddressDTO> findByClient(Long id);
    AddressDTO save(Address address, Long idClient);
    Optional<AddressDTO> update(Long id, Address address);
    void delete(Long id);
}
