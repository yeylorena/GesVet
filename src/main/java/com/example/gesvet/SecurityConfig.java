/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.gesvet;

import com.example.gesvet.service.CustomSuccessHandler;
import com.example.gesvet.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomSuccessHandler customSuccessHandler;
    
    @Autowired
    CustomUserDetailsService customUserDetailsServices;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(c -> c.disable())
                .authorizeHttpRequests(request -> request.requestMatchers("/homes")
                .hasAuthority("ADMIN").requestMatchers("/home").hasAuthority("USER")
                .requestMatchers("/**").permitAll()
                .requestMatchers("/img/imagenes_perfil/**").permitAll()
                .requestMatchers("/register", "/reset-password").permitAll()
                .requestMatchers("/password-request").permitAll()
                .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
                .successHandler(customSuccessHandler).permitAll())
                .logout(form -> form.invalidateHttpSession(true).clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout").permitAll());

        http.headers().cacheControl().disable();
        return http.build();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsServices).passwordEncoder(passwordEncoder());
    }

}
