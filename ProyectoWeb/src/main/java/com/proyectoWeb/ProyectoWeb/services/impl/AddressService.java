//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.services.impl;

import com.proyectoWeb.ProyectoWeb.entity.dto.AddressDTO;
import com.proyectoWeb.ProyectoWeb.entity.Address;
import com.proyectoWeb.ProyectoWeb.entity.Client;
import com.proyectoWeb.ProyectoWeb.repositories.IAddressRepository;
import com.proyectoWeb.ProyectoWeb.repositories.IClientRepository;
import com.proyectoWeb.ProyectoWeb.services.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService implements IAddressService {
    @Autowired
    private IAddressRepository addressRepository;
    @Autowired
    private IClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> findByClient(Long idClient) {
        List<Address> addresses = addressRepository.findByClientId(idClient);
        return addresses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDTO save(Address address, Long idClient) {
        Client client;
        Optional<Client> optionalClient = clientRepository.findById(idClient);
        if (optionalClient.isPresent()){
            client=optionalClient.orElseThrow();
            address.setClient(client);
            return convertToDTO(addressRepository.save(address));
        }
        return null;
    }

    @Override
    @Transactional
    public Optional<AddressDTO> update(Long id, Address address) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        optionalAddress.ifPresent(addressDB -> {
            addressDB.setId(id);
            addressDB.setDistrict(address.getDistrict());
            addressDB.setStreet(address.getStreet());
            addressDB.setNumber(address.getNumber());
            addressDB.setZipCode(address.getZipCode());
            addressRepository.save(addressDB);
        });
        return optionalAddress.map(this::convertToDTO);
    }

    @Override
    public void delete(Long id) {
        addressRepository.deleteById(id);
    }

    private AddressDTO convertToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setDistrict(address.getDistrict());
        dto.setStreet(address.getStreet());
        dto.setNumber(address.getNumber());
        dto.setZipCode(address.getZipCode());
        dto.setClientId(address.getClient().getId());
        return dto;
    }
}
