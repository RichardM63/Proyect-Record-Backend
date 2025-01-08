package com.proyectoWeb.ProyectoWeb.entity.dto.authentication;

import com.proyectoWeb.ProyectoWeb.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
public class AuthRequestDTO {
    private String username;
    private String password;
    private List<Role> roles;
    private boolean enable;
}
