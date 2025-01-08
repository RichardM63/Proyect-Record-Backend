//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="addresses")
@Getter
@Setter
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_addresses")
    private Long id;

    @NotBlank
    @Size(min = 3,max = 30)
    private String district;
    @NotBlank
    @Size(min = 5,max = 40)
    private String street;

    @NotBlank
    @Size(max = 15)
    private String number;
    @NotNull
    @Column(name = "zip_code")
    @Min(value = 10000)
    @Max(value = 99999)
    private Integer zipCode;

    @JsonIgnoreProperties({"addresses","handler","hibernateLazyInitializer"})
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_clients")
    private Client client;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", zipCode=" + zipCode +
                '}';
    }

    public Address(String district, String street, String number, Integer zipCode) {
        this.district = district;
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
    }
}
