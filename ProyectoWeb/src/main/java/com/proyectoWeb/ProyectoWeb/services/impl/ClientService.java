//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.services.impl;

import com.proyectoWeb.ProyectoWeb.entity.Client;
import com.proyectoWeb.ProyectoWeb.entity.Role;
import com.proyectoWeb.ProyectoWeb.entity.TokenEntity;
import com.proyectoWeb.ProyectoWeb.entity.dto.ClientDTO;
import com.proyectoWeb.ProyectoWeb.entity.dto.authentication.AuthRequestDTO;
import com.proyectoWeb.ProyectoWeb.repositories.IClientRepository;
import com.proyectoWeb.ProyectoWeb.repositories.IRoleRepository;
import com.proyectoWeb.ProyectoWeb.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService implements IClientService {
    @Autowired
    private IClientRepository clientRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenEntityService tokenEntityService;

    @Override
    public List<ClientDTO> findAll() {
        List<Client> clients =(List<Client>) clientRepository.findAll();
        return clients.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientDTO> findbyId(Long id) {
        return clientRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public Optional<ClientDTO> findByUsername2(String username) {
        return clientRepository.findByUsername(username).map(this::convertToDTO);
    }

    @Override
    @Transactional
    public ClientDTO save(Client client) {
        Optional<Role> optionalRole=roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        optionalRole.ifPresent(roles::add);
        if(client.isAdmin()){
            Optional<Role> optionalRoleAdmin=roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }
        client.setRoles(roles);
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return convertToDTO(clientRepository.save(client));
    }

    @Override
    @Transactional
    public Optional<ClientDTO> update(Long id,ClientDTO client) {
        Optional<Client> optionalClient=clientRepository.findById(id);
        optionalClient.ifPresent(client1 -> {
            client1.setId(id);
            client1.setName(client.getName());
            client1.setLastname(client.getLastname());
            client1.setEmail(client.getEmail());
            client1.setPhone(client.getPhone());
            clientRepository.save(client1);
        });
        return optionalClient.map(this::convertToDTO);
    }

    @Override
    @Transactional
    public Optional<ClientDTO> suspend(Long id) {
        Optional<Client> optionalClient=clientRepository.findById(id);
        optionalClient.ifPresent(client -> {
            Optional<TokenEntity> token;
            token =tokenEntityService.findByUsername(client.getUsername());
            token.ifPresent(token1 -> {
                token1.setTokenId("");
                tokenEntityService.save(token1.getTokenId(), token1.getUsername());
            });
        });
        return optionalClient.map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return clientRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existByDni(String dni) {
        return clientRepository.existsByDni(dni);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthRequestDTO> findByUsername(String username) {
        return clientRepository.findByUsername(username).map(this::convertRequestDTO);
    }

    private AuthRequestDTO convertRequestDTO(Client client){
        AuthRequestDTO requestDTO = new AuthRequestDTO();
        requestDTO.setUsername(client.getUsername());
        requestDTO.setPassword(client.getPassword());
        requestDTO.setRoles(client.getRoles());
        requestDTO.setEnable(client.isEnable());
        return requestDTO;
    }

    private ClientDTO convertToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setDni(client.getDni());
        dto.setUsername(client.getUsername());
        dto.setName(client.getName());
        dto.setLastname(client.getLastname());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());
        return dto;
    }
}
