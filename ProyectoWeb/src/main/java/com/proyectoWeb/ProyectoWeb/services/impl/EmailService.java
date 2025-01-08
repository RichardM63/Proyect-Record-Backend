//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.services.impl;

import com.proyectoWeb.ProyectoWeb.entity.Invoice;
import com.proyectoWeb.ProyectoWeb.entity.dto.ClientDTO;
import com.proyectoWeb.ProyectoWeb.entity.dto.InvoiceDTO;
import com.proyectoWeb.ProyectoWeb.entity.dto.OrderDTO;
import com.proyectoWeb.ProyectoWeb.repositories.IInvoiceRepository;
import com.proyectoWeb.ProyectoWeb.services.IClientService;
import com.proyectoWeb.ProyectoWeb.services.IEmailCodeService;
import com.proyectoWeb.ProyectoWeb.services.IEmailNotify;
import com.proyectoWeb.ProyectoWeb.services.IOrderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmailService implements IEmailCodeService, IEmailNotify {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private IInvoiceRepository invoiceRepository;

    MimeMessage message;
    MimeMessageHelper helper;
    Context context;

    @Override
    public String obtenerFecha() {
        Date now = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("EEEE dd 'de' MMMM, yyyy 'Hora:' hh:mm a", new Locale("es", "ES"));
        return formateador.format(now);
    }

    @Override
    public String generadorCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }

    @Override
    public String sendEmailCode(String username, String email) throws MessagingException {
        String code =generadorCode();
        message=javaMailSender.createMimeMessage();
        helper=new MimeMessageHelper(message, true,"UTF-8");
        helper.setTo(email);
        helper.setSubject("Verificacion de correo");
        context = new Context();
        Map variables = new HashMap();
        variables.put("client", username);
        variables.put("code", code);
        variables.put("dateTime", obtenerFecha());
        context.setVariables(variables);
        String contentHTML = templateEngine.process("emailCode",context);
        helper.setText(contentHTML,true);
        javaMailSender.send(message);
        return code;
    }

    @Override
    public void sendNotify(Map variables, String email) throws MessagingException {
        message=javaMailSender.createMimeMessage();
        helper=new MimeMessageHelper(message, true,"UTF-8");
        helper.setTo(email);
        helper.setSubject("Estado de tu Orden");
        context = new Context();

        switch (variables.get("state").toString()){
            case "Pendiente":
                variables.put("message","Gracias por confiar en nosotros y solicitar nuestros servicios. En un momento, estaremos revisando tu solicitud y comunicandonos con usted");
                break;
            case "En proceso":
                variables.put("message","Su solicitud ya se encuentra en elaboracion luego de una coordinacion previa con usted. ¡Gracias por tu paciencia!");
                break;
            case "Concluido":
                variables.put("message","Muchas gracias por acompañarnos en todo este proceso. Esperamos que estés satisfecho con el servicio. No dudes en contactarnos si necesitas algo más.");
                break;
        }

        variables.put("dateTime", obtenerFecha());
        context.setVariables(variables);
        String contentHTML = templateEngine.process("emailNotify",context);
        helper.setText(contentHTML,true);
        javaMailSender.send(message);
    }

    @Override
    public void sendProof(Map variables, String email) throws MessagingException {
        message=javaMailSender.createMimeMessage();
        helper=new MimeMessageHelper(message, true,"UTF-8");
        helper.setTo(email);
        context = new Context();
        String subject;

        if(variables.get("state").toString().equals("Pagado")){
            subject = "Constancia de Pago de Servicio";
            helper.setSubject(subject);
        }else {
            subject = "Constancia de Cancelo de Servicio";
            helper.setSubject(subject);
        }
        variables.put("subject",subject);
        variables.put("dateTime", obtenerFecha());
        context.setVariables(variables);
        String contentHTML = templateEngine.process("emailProof",context);
        helper.setText(contentHTML,true);

        if (variables.containsKey("invoiceId")) {
            Long invoiceId = Long.parseLong(variables.get("invoiceId").toString());
            byte[] fileData = (byte[])variables.get("file");
            if (fileData != null) {
                helper.addAttachment("Detalles_Factura_" + invoiceId + ".pdf", new ByteArrayResource(fileData));
            }
        }
        javaMailSender.send(message);
    }
}
