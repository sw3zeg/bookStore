package com.example.bookstore.app.configurations.security;


import com.example.bookstore.app.service.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

    private final CustomerService customerService;
    private final RoleAccessDeniedHandler roleAccessDeniedHandler;
    private final NoAuthenticationEntryPoint noAuthenticationEntryPoint;


    public SecurityConfiguration(CustomerService customerService, RoleAccessDeniedHandler roleAccessDeniedHandler, NoAuthenticationEntryPoint noAuthenticationEntryPoint) {
        this.customerService = customerService;
        this.roleAccessDeniedHandler = roleAccessDeniedHandler;
        this.noAuthenticationEntryPoint = noAuthenticationEntryPoint;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> {
                    s.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/*/private/**").authenticated();
                    request.requestMatchers("/*/admin/**").hasRole("ADMIN");
                    request.anyRequest().permitAll();
                })
                .exceptionHandling(e -> {
                    e.authenticationEntryPoint(noAuthenticationEntryPoint);
                    e.accessDeniedHandler(roleAccessDeniedHandler);
                })
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customerService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public  JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }


}
