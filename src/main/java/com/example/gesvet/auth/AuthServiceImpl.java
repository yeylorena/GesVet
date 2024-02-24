/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.gesvet.auth;


import com.example.gesvet.jwtUtil.JwtUtils;
import com.example.gesvet.repository.UserRepository;
import com.example.gesvet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
   @Autowired
    private UserService userService;

  @Override
  public String login(String username, String password) {
    var authToken = new UsernamePasswordAuthenticationToken(username, password);

    var authenticate = authenticationManager.authenticate(authToken);

    // Generate Token
    return JwtUtils.generateToken(((UserDetails)(authenticate.getPrincipal())).getUsername());
  }


  @Override
  public String verifyToken(String token) {
    var usernameOptional = JwtUtils.getUsernameFromToken(token);

    if(usernameOptional.isPresent()){
      return usernameOptional.get();
    }

    throw new RuntimeException("Token invalid");
  }
}