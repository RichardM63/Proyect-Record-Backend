//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthoritiesJsonCreator {
        @JsonCreator
        public SimpleGrantedAuthoritiesJsonCreator(@JsonProperty("authority") String role){

        }
}
