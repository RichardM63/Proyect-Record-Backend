//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.services.impl;

import com.proyectoWeb.ProyectoWeb.entity.Client;
import com.proyectoWeb.ProyectoWeb.entity.RecordEntity;
import com.proyectoWeb.ProyectoWeb.repositories.IRecordRepository;
import com.proyectoWeb.ProyectoWeb.services.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecordService implements IRecordService {
    @Autowired
    private IRecordRepository repository;

    @Transactional
    @Override
    public RecordEntity save(Client client) {
        RecordEntity record = new RecordEntity(client);
        return repository.save(record);
    }
}
