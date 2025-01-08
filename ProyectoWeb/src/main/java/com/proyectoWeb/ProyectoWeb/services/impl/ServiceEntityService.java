//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.services.impl;

import com.proyectoWeb.ProyectoWeb.entity.ServiceEntity;
import com.proyectoWeb.ProyectoWeb.repositories.IServiceRepository;
import com.proyectoWeb.ProyectoWeb.services.IServiceEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceEntityService implements IServiceEntityService {
    @Autowired
    private IServiceRepository serviceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ServiceEntity> findAll() {
        return (List<ServiceEntity>) serviceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceEntity> findById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    @Transactional
    public ServiceEntity save(ServiceEntity serviceEntity) {
        return serviceRepository.save(serviceEntity);
    }

    @Override
    @Transactional
    public Optional<ServiceEntity> update(Long id, ServiceEntity serviceEntity) {
        Optional<ServiceEntity> optionalService = serviceRepository.findById(id);
        optionalService.ifPresent(serviceEntityDB -> {
            serviceEntityDB.setId(id);
            serviceEntityDB.setServiceName(serviceEntity.getServiceName());
            serviceEntityDB.setDescription(serviceEntity.getDescription());
        });
        return optionalService;
    }
}
