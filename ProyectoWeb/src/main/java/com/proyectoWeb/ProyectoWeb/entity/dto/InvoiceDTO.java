package com.proyectoWeb.ProyectoWeb.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
public class InvoiceDTO {
    private Long id;
    private String state;
    private Date paymentDate;
    private Float amount;
    private byte[] fileData;  // Cambiado de byte[] a String para base64
    private Long idClient;
    private Long idOrder;


}
