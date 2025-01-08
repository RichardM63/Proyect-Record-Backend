package com.proyectoWeb.ProyectoWeb.entity.dto;

import com.proyectoWeb.ProyectoWeb.entity.Client;
import com.proyectoWeb.ProyectoWeb.entity.ServiceEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
public class OrderDTO {
    private Long id;
    private String state;
    private Date startDate;
    private Date updateDate;
    private String description;
    private ServiceEntity Service;
    private Long idClient;

    @Override
    public String toString() {
        return "🚀 *Nuevo Pedido* 🚀\n" +
                "🆔 *ID*: " + id + "\n" +
                "📌 *Estado*: " + state + "\n" +
                "📅 *Fecha de Inicio*: " + startDate + "\n" +
                "📝 *Descripción*: " + description + "\n" +
                "🔧 *Servicio*: " + Service.toString() + "\n" +
                "✨ Gracias por usar nuestro sistema.";
    }
}
