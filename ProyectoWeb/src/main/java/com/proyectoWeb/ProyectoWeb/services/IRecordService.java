package com.proyectoWeb.ProyectoWeb.services;

import com.proyectoWeb.ProyectoWeb.entity.Client;
import com.proyectoWeb.ProyectoWeb.entity.RecordEntity;

import java.util.Date;
import java.util.Optional;

public interface IRecordService {
    RecordEntity save(Client client);
}
