package byrybdyk.me.lb1.controller;

import byrybdyk.me.lb1.dto.AuthResponse;
import byrybdyk.me.lb1.dto.LoginRequest;
import byrybdyk.me.lb1.dto.RegisterRequest;
import byrybdyk.me.lb1.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(service.signUp(req));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(service.signIn(req));
    }
}
