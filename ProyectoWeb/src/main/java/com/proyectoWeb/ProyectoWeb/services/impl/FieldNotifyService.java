//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.services.impl;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class FieldNotifyService {
    @Autowired
    private InvoiceService invoiceService;

    @Transactional(readOnly = true)
    public byte[] sendInvoiceEmailWithAttachment(Long invoiceId, Map<String, Object> variables) throws MessagingException {
        byte[] fileData = invoiceService.findById(invoiceId).orElseThrow().getFileData();
        if (fileData != null) {
            variables.put("invoiceId", invoiceId);
        }
        return fileData;
    }
}
