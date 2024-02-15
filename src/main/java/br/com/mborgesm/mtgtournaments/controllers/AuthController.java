package br.com.mborgesm.mtgtournaments.controllers;

import br.com.mborgesm.mtgtournaments.payload.request.LoginRequest;
import br.com.mborgesm.mtgtournaments.payload.request.SignupRequest;
import br.com.mborgesm.mtgtournaments.payload.response.JwtResponse;
import br.com.mborgesm.mtgtournaments.payload.response.MessageResponse;
import br.com.mborgesm.mtgtournaments.security.services.UserDetailsImpl;
import br.com.mborgesm.mtgtournaments.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            authService.registerUser(signupRequest);
            return ResponseEntity.ok(new MessageResponse("User registered succesfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Object[] items = authService.login(loginRequest);
        String jwt = (String) items[0];
        UserDetailsImpl userDetails = (UserDetailsImpl) items[1];

        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail()));
    }
}
