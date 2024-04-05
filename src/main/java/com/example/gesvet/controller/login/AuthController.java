package com.example.gesvet.controller.login;

import com.example.gesvet.auth.AuthService;
import com.example.gesvet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService UserService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        var user = UserService.findByUsername(authRequestDto.username());

        if (user.getRole().equals("USER")) {
            var jwtToken = authService.login(authRequestDto.username(), authRequestDto.password());

            var authResponseDto = new AuthResponseDto(jwtToken, AuthStatus.LOGIN_SUCCESS);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authResponseDto);
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @PostMapping("/verify-token")
    public ResponseEntity<String> verifyToken(@RequestBody String token) {
        try {
            var username = authService.verifyToken(token.substring(7));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(username);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }
}
