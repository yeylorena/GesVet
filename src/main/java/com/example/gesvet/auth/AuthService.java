package com.example.gesvet.auth;

public interface AuthService {

    String login(String username, String password);

    String verifyToken(String token);
}
