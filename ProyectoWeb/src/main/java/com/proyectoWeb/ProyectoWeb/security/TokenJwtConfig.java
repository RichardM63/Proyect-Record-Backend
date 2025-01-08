//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.security;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class TokenJwtConfig {
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    public static final String PREFIX_TOKEN ="Bearer ";
    public static final String HEADER_AUTHORIZATION ="Authorization";
    public static final String CONTENT_TIPE="application/json";
    public static final String HEADER_TYPE ="Content-Type";
}
