package com.proyectoWeb.ProyectoWeb.services;

import com.proyectoWeb.ProyectoWeb.entity.TokenEntity;

import java.util.Optional;

public interface ITokenEntityService {
    Optional<TokenEntity> findByUsername(String username);
    Boolean existByUsernameAndTokenId(String username, String tokenId);
    TokenEntity save(String tokenId, String username);
}
