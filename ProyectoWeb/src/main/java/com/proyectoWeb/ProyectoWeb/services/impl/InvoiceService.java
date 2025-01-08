//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.services.impl;

import com.proyectoWeb.ProyectoWeb.entity.Client;
import com.proyectoWeb.ProyectoWeb.entity.Invoice;
import com.proyectoWeb.ProyectoWeb.entity.Order;
import com.proyectoWeb.ProyectoWeb.entity.dto.InvoiceDTO;
import com.proyectoWeb.ProyectoWeb.exceptions.ResourceNotFoundException;
import com.proyectoWeb.ProyectoWeb.repositories.IClientRepository;
import com.proyectoWeb.ProyectoWeb.repositories.IInvoiceRepository;
import com.proyectoWeb.ProyectoWeb.repositories.IOrderRepository;
import com.proyectoWeb.ProyectoWeb.services.IInvoiceServices;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceService implements IInvoiceServices {
    @Autowired
    private IInvoiceRepository invoiceRepository;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IClientRepository clientRepository;
    @Autowired
    private EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> findAll() {
        List<Invoice> invoices =(List<Invoice>) invoiceRepository.findAll();
        return invoices.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceDTO> findById(Long id) {
        return invoiceRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> findByClientId(Long id) {
        List<Invoice> invoices = invoiceRepository.findByClientId(id);
        return invoices.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceDTO findByOrderId(Long orderId) {
        Invoice invoice = invoiceRepository.findByOrderId(orderId);
        return convertToDTO(invoice);
    }

    @Override
    @Transactional
    public InvoiceDTO save(Invoice invoice, Client client, Order order) {
        invoice.setClient(client);
        invoice.setOrder(order);
        return convertToDTO(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional
    public Optional<InvoiceDTO> update(Long id, Invoice invoice) throws MessagingException {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        Map<String, Object> variables = new HashMap<>();

        optionalInvoice.ifPresent(invoiceDB -> {
            invoiceDB.setId(id);
            invoiceDB.setState(invoice.getState());
            variables.put("invoice_id", id);

            if (!invoice.getState().equals("Pendiente")) {
                if (invoice.getState().equals("Pagado")) {
                    invoiceDB.setPaymentDate(new Date());
                }
                variables.put("state", invoiceDB.getState());
                variables.put("service", invoiceDB.getOrder().getServiceE().getServiceName());
                variables.put("description", invoiceDB.getOrder().getDescription());
                variables.put("issueDate", invoiceDB.getOrder().getStartDate());
                variables.put("dni", invoiceDB.getClient().getDni());
                variables.put("client", invoiceDB.getClient().getUsername());
                variables.put("paymentDate", invoiceDB.getPaymentDate());
                variables.put("amount", invoiceDB.getAmount());

                if (invoice.getCancellationReason() != null) {
                    variables.put("cancellationReason", invoice.getCancellationReason());
                }

                if (invoiceDB.getClient().getEmail() == null) {
                    try {
                        emailService.sendProof(variables, invoiceDB.getClient().getEmail());
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                invoiceDB.setAmount(invoice.getAmount());

                // Decodificar base64 a byte[]
                if (invoice.getFileData() != null) {
                    byte[] decodedFile = java.util.Base64.getDecoder().decode(invoice.getFileData());
                    invoiceDB.setFileData(decodedFile);
                    variables.put("file", decodedFile);
                }
            }

            invoiceRepository.save(invoiceDB);
        });

        return optionalInvoice.map(this::convertToDTO);
    }

    private InvoiceDTO convertToDTO(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setState(invoice.getState());
        dto.setPaymentDate(invoice.getPaymentDate());
        dto.setAmount(invoice.getAmount());
        dto.setFileData((invoice.getFileData()));
        dto.setIdOrder(invoice.getOrder().getId());
        dto.setIdClient(invoice.getClient().getId());
        return dto;
    }
}
