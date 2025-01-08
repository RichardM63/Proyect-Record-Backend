//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tokens")
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token_Id")
    private String tokenId;
    @Column(name = "username")
    private String username;

    public TokenEntity(String tokenId, String username) {
        this.tokenId = tokenId;
        this.username = username;
    }
}
