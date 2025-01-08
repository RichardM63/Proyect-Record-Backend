//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.services.impl;

import com.proyectoWeb.ProyectoWeb.entity.dto.OrderDTO;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@PropertySource("classpath:WhatsApp.properties")
public class WhatsAppNotificationService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.from.whatsapp}")
    private String fromWhatsAppNumber;

    @Value("${twilio.admin.whatsapp}")
    private String adminWhatsAppNumber;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    public OrderDTO sendOrderNotification(OrderDTO orderDetails) {
        String messageBody = "Nuevo pedido recibido:\n" + orderDetails.toString();

        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(adminWhatsAppNumber),
                new com.twilio.type.PhoneNumber(fromWhatsAppNumber),
                messageBody
        ).create();

        System.out.println("Mensaje enviado con SID: " + message.getSid());
        return orderDetails;
    }
}
