//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invoices")
    private Long id;

    private String state = "Pendiente";

    @Lob
    @Column(name = "file_data")
    private byte[] fileData;

    @Column(name = "payment_date")
    private Date paymentDate;

    private Float amount;

    @Transient
    private static final String PAY_STATUS = "Pagado";

    @JsonIgnoreProperties({"invoices", "handler", "hibernateLazyInitializer"})
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_clients")
    private Client client;

    @OneToOne
    @JoinColumn(name = "id_orders")
    private Order order;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String cancellationReason;

    // Constructor con MultipartFile para el archivo
    public Invoice(Float amount, MultipartFile file, String state, String cancellationReason) throws IOException {
        this.amount = amount;
        if (file != null && !file.isEmpty()) {
            this.fileData = file.getBytes();
        }
        if (state.equals(PAY_STATUS)) {
            this.paymentDate = new Date();
        }
        this.state = state;
        this.cancellationReason = cancellationReason;
    }

    // Constructor con solo amount y fileData en base64
    public Invoice(Float amount, String fileData, String state, String cancellationReason) throws IOException {
        this.amount = amount;
        if (fileData != null && !fileData.isEmpty()) {
            this.fileData = java.util.Base64.getDecoder().decode(fileData);  // Convertir base64 a byte[]
        }
        if (state.equals(PAY_STATUS)) {
            this.paymentDate = new Date();
        }
        this.state = state;
        this.cancellationReason = cancellationReason;
    }

    // Constructor para cuando no se pasa archivo
    public Invoice(Float amount, String fileData) throws IOException {
        this.amount = amount;
        if (fileData != null && !fileData.isEmpty()) {
            this.fileData = java.util.Base64.getDecoder().decode(fileData);  // Convertir base64 a byte[]
        }
        if (state.equals(PAY_STATUS)) {
            this.paymentDate = new Date();
        }
    }

    // Constructor sin estado y cancelación
    public Invoice(String state, String cancellationReason) {
        this.state = state;
        this.cancellationReason = cancellationReason;
        if (state.equals(PAY_STATUS)) {
            this.paymentDate = new Date();
        }
    }

    public Invoice(Float amount) {
        this.amount = amount;
    }
}