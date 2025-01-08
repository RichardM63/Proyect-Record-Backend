//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_services")
    private Long id;

    @NotBlank
    @Size(min = 3,max = 60)
    @Column(name = "service_name")
    private String serviceName;

    @NotBlank
    @Size(min = 10)
    private String description;

    @Override
    public String toString() {
        return "   -   *ID:* " + id + "\n" +
                "   - 🏷️ *Nombre:* " + serviceName + "\n" +
                "   - 📖 *Descripción:* " + description + "\n";
    }
}
