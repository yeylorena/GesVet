package com.example.gesvet;

import com.example.gesvet.service.CustomSuccessHandler;
import com.example.gesvet.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Order(1)
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
                .authorizeHttpRequests(request -> request.requestMatchers("/homes", "/perfil_admin","/cliente", "/razas", "/citasRapidas", "/listado","/productos", "/getCartadmin", "/verhomeadmin", "/ayuda","/productohomeadmin", "/cartadmin" ,"/facturaadmin", "/detalleadmin", "/editarFactura")
                .hasAuthority("ADMIN").requestMatchers("/home", "/perfil", "/mascotasUsuarios","/citas", "/verhome", "/getCart", "/comprasUser", "/ayudausuario" ,"/productohome", "/factura", "/productohomes", "/detalle").hasAuthority("USER")
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
        http.exceptionHandling().accessDeniedPage("/error403");

        http.headers().cacheControl().disable();
        return http.build();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsServices).passwordEncoder(passwordEncoder());
    }

}
