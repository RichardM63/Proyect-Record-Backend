package com.proyectoWeb.ProyectoWeb.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressDTO {
    private Long id;
    private String district;
    private String street;
    private String number;
    private int zipCode;
    private Long clientId;
}
