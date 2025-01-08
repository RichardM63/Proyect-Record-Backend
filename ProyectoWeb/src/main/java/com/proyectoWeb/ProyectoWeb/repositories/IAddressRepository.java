package com.proyectoWeb.ProyectoWeb.repositories;

import com.proyectoWeb.ProyectoWeb.entity.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IAddressRepository extends CrudRepository<Address,Long> {
    List<Address> findByClientId(Long clientId);
}
