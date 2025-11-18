package byrybdyk.me.lb1.service;

import byrybdyk.me.lb1.dto.AuthResponse;
import byrybdyk.me.lb1.dto.LoginRequest;
import byrybdyk.me.lb1.dto.RegisterRequest;
import byrybdyk.me.lb1.model.User;
import byrybdyk.me.lb1.repository.UserRepository;
import byrybdyk.me.lb1.security.token.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;
    private final AuthenticationManager authManager;

    public AuthService(UserRepository repo, PasswordEncoder encoder,
                          TokenService tokenService, AuthenticationManager authManager) {
        this.repo = repo;
        this.encoder = encoder;
        this.tokenService = tokenService;
        this.authManager = authManager;
    }

    public AuthResponse signUp(RegisterRequest req) {
        if (repo.findByEmail(req.email()).isPresent())
            throw new IllegalArgumentException("Email exists");

        User user = new User();
        user.setEmail(req.email());
        user.setName(req.name());
        user.setPassword(encoder.encode(req.password()));
        repo.save(user);

        String token = tokenService.generate(user);
        return new AuthResponse(token);
    }

    public AuthResponse signIn(LoginRequest req) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.email(), req.password())
            );
            User user = repo.findByEmail(req.email()).orElseThrow();
            String token = tokenService.generate(user);
            return new AuthResponse(token);
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Wrong email/password");
        }
    }
}
