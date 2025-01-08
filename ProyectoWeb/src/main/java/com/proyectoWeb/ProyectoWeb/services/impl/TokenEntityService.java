//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.services.impl;

import com.proyectoWeb.ProyectoWeb.entity.TokenEntity;
import com.proyectoWeb.ProyectoWeb.repositories.ITokenRepository;
import com.proyectoWeb.ProyectoWeb.services.ITokenEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TokenEntityService implements ITokenEntityService {
    @Autowired
    ITokenRepository repository;

    @Transactional(readOnly = true)
    @Override
    public Optional<TokenEntity> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean existByUsernameAndTokenId(String username, String tokenId) {
        Optional<TokenEntity> tokenDB = repository.findByUsernameAndTokenId(username,tokenId);
        return tokenDB.isPresent();
    }

    @Transactional
    @Override
    public TokenEntity save(String tokenId, String username) {
        TokenEntity token = new TokenEntity(tokenId,username);
        return repository.save(token);
    }
}
