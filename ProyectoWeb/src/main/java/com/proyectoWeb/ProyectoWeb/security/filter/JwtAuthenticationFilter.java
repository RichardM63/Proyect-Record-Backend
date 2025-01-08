//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.security.filter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoWeb.ProyectoWeb.entity.Client;
import com.proyectoWeb.ProyectoWeb.entity.TokenEntity;
import com.proyectoWeb.ProyectoWeb.entity.dto.authentication.AuthRequestDTO;
import com.proyectoWeb.ProyectoWeb.repositories.IClientRepository;
import com.proyectoWeb.ProyectoWeb.services.IRecordService;
import com.proyectoWeb.ProyectoWeb.services.impl.TokenEntityService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.*;

import static com.proyectoWeb.ProyectoWeb.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenEntityService service;
    private final IRecordService serviceRecord;
    private final IClientRepository clientRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TokenEntityService service,IRecordService serviceRecord,IClientRepository clientRepository) {
        this.authenticationManager = authenticationManager;
        this.service =service;
        this.serviceRecord = serviceRecord;
        this.clientRepository = clientRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthRequestDTO clientDTO = null;
        String username=null;
        String password=null;

        try {
            clientDTO = new ObjectMapper().readValue(request.getInputStream(), AuthRequestDTO.class);
            username = clientDTO.getUsername();
            password = clientDTO.getPassword();
        }
        catch (StreamReadException e){
            e.printStackTrace();
        }
        catch (DatabindException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);
    }

    //Response es para devolver algo y request es para entregar

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User user= (org.springframework.security.core.userdetails.User)authResult.getPrincipal();
        String username = user.getUsername();

        Optional<TokenEntity> existingToken = service.findByUsername(username);
        String tokenId;
        if(existingToken.isPresent()){
            tokenId = existingToken.get().getTokenId();
        }else {
            tokenId = UUID.randomUUID().toString();
            service.save(tokenId,username);
        }



        Collection<? extends GrantedAuthority> roles =authResult.getAuthorities();
        Claims claims =Jwts.claims().add("tokenId",tokenId).add("username",username).add("Authorities",new ObjectMapper().writeValueAsString(roles)).build();
        String token=Jwts.builder()
                .subject(username)
                .signWith(SECRET_KEY)
                .issuedAt(new Date())
                .claims(claims)
                .compact();

        Optional<Client> clientDB=clientRepository.findByUsername(username);
        if(clientDB.isPresent()){
            serviceRecord.save(clientDB.orElseThrow());
        }
        response.addHeader(HEADER_AUTHORIZATION,PREFIX_TOKEN+token);

        Map<String,String> body=new HashMap<>();
        body.put("Token",token);
        body.put("Username",username);
        body.put("Message",String.format("Hola %s has iniciado sesion con exito",username));


        //Convertir el map en un Json
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TIPE);
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String,String> body=new HashMap<>();
        body.put("message","Error en la autenticacion username o password incorrectos!");
        body.put("error",failed.getMessage());
        //Convertir el map en un Json
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TIPE);
        response.setStatus(401);
    }
}
