package com.proyectoWeb.ProyectoWeb.repositories;

import com.proyectoWeb.ProyectoWeb.entity.TokenEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ITokenRepository extends CrudRepository<TokenEntity,Long> {
    Optional<TokenEntity> findByUsernameAndTokenId(String username, String tokenId);
    Optional<TokenEntity> findByUsername(String username);
}
