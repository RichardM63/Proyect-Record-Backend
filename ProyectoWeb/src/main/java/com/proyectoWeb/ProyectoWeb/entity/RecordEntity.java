//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "records")
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties({"orders","handler","hibernateLazyInitializer"})
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    private Date date;

    public RecordEntity(Client client) {
        this.client = client;
        this.date = new Date();
    }
}
