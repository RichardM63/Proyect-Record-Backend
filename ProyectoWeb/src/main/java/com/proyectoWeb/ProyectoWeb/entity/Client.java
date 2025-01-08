//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.proyectoWeb.ProyectoWeb.validation.ExistByUsername;
import com.proyectoWeb.ProyectoWeb.validation.ExistByDni;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name="clients")
@Getter@Setter@NoArgsConstructor
@ToString
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clients")
    private Long id;

    @ExistByDni
    @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 dígitos")
    @NotBlank
    @Column(unique = true)
    private String dni;

    @ExistByUsername
    @NotBlank
    @Column(unique = true)
    @Size(min = 3,max = 30)
    private String username;

    @NotBlank
    @Size(min = 3,max = 30)
    private String name;

    @NotBlank
    @Size(min = 3,max = 50)
    private String lastname;

    @NotBlank
    @Size(min = 5,max = 60)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Size(max = 50)
    private String email;

    @Min(value = 900000000, message = "debe comenzar con 9 y tener 9 dígitos")
    @Max(value = 999999999, message = "debe comenzar con 9 y tener 9 dígitos")
    @Digits(integer = 9, fraction = 0, message = "debe tener exactamente 9 dígitos")
    private Integer phone;

    private boolean enable;

    @PrePersist
    public void prePersist(){
        enable=true;
    }

    public Client(boolean admin, Integer phone, String email, String password, String lastname, String name, String dni, String username) {
        this.admin = admin;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.lastname = lastname;
        this.name = name;
        this.dni = dni;
        this.username =username;
    }

    public Client(Integer phone, String email, String lastname, String name, String dni) {
        this.phone = phone;
        this.email = email;
        this.lastname = lastname;
        this.name = name;
        this.dni = dni;
    }

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @JsonIgnoreProperties({"clients","handler","hibernateLazyInitializer"})
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "clients_roles",
            joinColumns = @JoinColumn(name="id_clients"),
            inverseJoinColumns = @JoinColumn(name = "id_roles"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"id_clients","id_roles"}))
    private List<Role> roles;

    @JsonIgnoreProperties({"clients","handler","hibernateLazyInitializer"})
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "client")
    private List<RecordEntity> records;

    @JsonIgnoreProperties({"clients","handler","hibernateLazyInitializer"})
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "client")
    private List<Invoice> invoices;

    @JsonIgnoreProperties({"clients","handler","hibernateLazyInitializer"})
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "client")
    private List<Address> addresses;

    @JsonIgnoreProperties({"clients","handler","hibernateLazyInitializer"})
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "client")
    private List<Order> orders;
}
