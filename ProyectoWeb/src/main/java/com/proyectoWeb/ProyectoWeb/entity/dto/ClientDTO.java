package com.proyectoWeb.ProyectoWeb.entity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter@Getter
@NoArgsConstructor
public class ClientDTO {
    private Long id;
    private String dni;
    private String username;
    private String name;
    private String lastname;
    private String email;
    private Integer phone;

    public ClientDTO(String name, String lastname, String email, Integer phone) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
    }
}
