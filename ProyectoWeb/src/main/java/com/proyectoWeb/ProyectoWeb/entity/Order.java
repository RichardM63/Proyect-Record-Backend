//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orders")
    private Long id;
    private String state= "Pendiente";
    @Column(name = "start_date")
    private Date startDate= new Date();
    @Column(name = "update_date")
    private Date updateDate;
    private String description;

    @JsonIgnoreProperties({"orders","handler","hibernateLazyInitializer"})
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_clients")
    private Client client;

    @OneToOne
    @JoinColumn(name = "id_services")
    private ServiceEntity serviceE;

    @OneToOne
    @JoinColumn(name = "id_address")
    private Address address;

    public Order() {
        this.updateDate=new Date();
    }

    public Order(String state, String description) {
        this();
        this.state = state;
        this.description = description;
    }

    public Order(String state){
        this();
        this.state = state;
    }
}
