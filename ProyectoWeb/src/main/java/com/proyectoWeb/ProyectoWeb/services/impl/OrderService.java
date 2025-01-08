//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.services.impl;

import com.proyectoWeb.ProyectoWeb.entity.*;
import com.proyectoWeb.ProyectoWeb.entity.dto.InvoiceDTO;
import com.proyectoWeb.ProyectoWeb.entity.dto.OrderDTO;
import com.proyectoWeb.ProyectoWeb.repositories.IAddressRepository;
import com.proyectoWeb.ProyectoWeb.repositories.IClientRepository;
import com.proyectoWeb.ProyectoWeb.repositories.IOrderRepository;
import com.proyectoWeb.ProyectoWeb.repositories.IServiceRepository;
import com.proyectoWeb.ProyectoWeb.services.IOrderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IClientRepository clientRepository;
    @Autowired
    private IServiceRepository serviceRepository;
    @Autowired
    private InvoiceService Invoiceservice;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IAddressRepository addressRepository;
    @Autowired
    private WhatsAppNotificationService whatsAppService;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> findAll() {
        List<Order> orders=(List<Order>)orderRepository.findAll();
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDTO> findById(Long id) {
        return orderRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> findByClientId(Long id) {
        List<Order> orders= orderRepository.findByClientId(id);
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO save(Order order, Long idClient, Long idService, Long idAddress) throws MessagingException {
        Client client;
        ServiceEntity serviceEntity;
        Order orderDB;
        Invoice invoice = new Invoice(0F);
        List<Address> addresses= addressRepository.findByClientId(idClient);
        Optional<Client> optionalClient = clientRepository.findById(idClient);
        Optional<ServiceEntity> optionalService=serviceRepository.findById(idService);
        Map variables = new HashMap();

        if(optionalClient.isPresent()&&optionalService.isPresent()){

        client=optionalClient.orElseThrow();
        serviceEntity=optionalService.orElseThrow();
        Optional<Address> optionalAddress= addresses.stream().filter(address -> address.getId().equals(idAddress)).findAny();

        if (optionalAddress.isPresent()){
            order.setAddress(optionalAddress.orElseThrow());
        }
        order.setClient(client);
        order.setServiceE(serviceEntity);
        orderDB=orderRepository.save(order);

        Invoiceservice.save(invoice,client,orderDB);

        variables.put("state",orderDB.getState());
        variables.put("description",orderDB.getDescription());
        variables.put("service",orderDB.getServiceE().getServiceName());
        variables.put("client",client.getUsername());
        if(client.getEmail()==null){
            emailService.sendNotify(variables,client.getEmail());
        }

        return whatsAppService.sendOrderNotification(convertToDTO(orderDB));
        }
        return null;
    }

    @Override
    @Transactional
    public Optional<OrderDTO> update(Long id, Order order) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        InvoiceDTO invoice = Invoiceservice.findByOrderId(id);
        Map variables = new HashMap();
        optionalOrder.ifPresent(orderDB -> {
            if(!order.getState().equals("Pendiente")){

                orderDB.setId(id);
                orderDB.setState(order.getState());
                orderDB.setUpdateDate(order.getUpdateDate());
                if(order.getDescription()!=null){
                    orderDB.setDescription(order.getDescription());
                }
                orderRepository.save(orderDB);
                variables.put("state",orderDB.getState());
                variables.put("description",orderDB.getDescription());
                variables.put("service",orderDB.getServiceE().getServiceName());
                variables.put("amount",invoice.getAmount());
                variables.put("stateInvoice",invoice.getState());
                variables.put("client",orderDB.getClient().getUsername());
                if(optionalOrder.orElseThrow().getClient().getEmail()==null) {
                    try {
                        emailService.sendNotify(variables, optionalOrder.orElseThrow().getClient().getEmail());
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        return optionalOrder.map(this::convertToDTO);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setState(order.getState());
        dto.setStartDate(order.getStartDate());
        dto.setUpdateDate(order.getUpdateDate());
        dto.setDescription(order.getDescription());
        dto.setService(order.getServiceE());
        dto.setIdClient(order.getClient().getId());
        return dto;
    }
}
