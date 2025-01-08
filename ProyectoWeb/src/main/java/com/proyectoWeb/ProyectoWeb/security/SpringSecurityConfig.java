//By RichardM63
//https://github.com/RichardM63
package com.proyectoWeb.ProyectoWeb.security;

import com.proyectoWeb.ProyectoWeb.repositories.IClientRepository;
import com.proyectoWeb.ProyectoWeb.security.filter.JwtAuthenticationFilter;
import com.proyectoWeb.ProyectoWeb.security.filter.JwtValidationFilter;
import com.proyectoWeb.ProyectoWeb.services.IRecordService;
import com.proyectoWeb.ProyectoWeb.services.impl.TokenEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import static com.proyectoWeb.ProyectoWeb.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.proyectoWeb.ProyectoWeb.security.TokenJwtConfig.HEADER_TYPE;

@Configuration
public class SpringSecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private TokenEntityService service;

    @Autowired
    private IRecordService serviceRecord;

    @Autowired
    private IClientRepository clientRepository;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET,"/clients/services","/clients/services/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST,"/clients/register","/email/create/{username}/{email}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/clients/{id}","/clients/invoices/order/{idOrder}","/clients/user/{username}","/clients/address/{idClient}","/clients/invoices/{id}","/clients/orders/{id}").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/clients/address/create/{idClient}","/clients/orders/create/{idClient}/{idService}/{idAddress}").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/clients/update/{id}","/clients/address/{idClient}").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.GET,"/clients","/clients/invoices","/clients/orders","/clients/invoices/invoice/{id}","/clients/orders/order/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/clients/create","/clients/services/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/clients/invoices/{id}","/clients/suspend/{id}","/clients/orders/update/{id}","/clients/services/{id}").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(),service,serviceRecord,clientRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtValidationFilter(authenticationManager(),service), UsernamePasswordAuthenticationFilter.class)
                .csrf(config->config.disable())
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(managment->managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        corsConfiguration.setAllowedHeaders(Arrays.asList(HEADER_AUTHORIZATION,HEADER_TYPE));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}


