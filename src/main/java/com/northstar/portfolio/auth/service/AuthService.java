package com.northstar.portfolio.auth.service;

import com.northstar.portfolio.auth.dto.AuthResponse;
import com.northstar.portfolio.auth.dto.LoginRequest;
import com.northstar.portfolio.auth.dto.RegisterRequest;
import com.northstar.portfolio.auth.security.JwtService;
import com.northstar.portfolio.user.entity.Role;
import com.northstar.portfolio.user.entity.User;
import com.northstar.portfolio.user.repository.RoleRepository;
import com.northstar.portfolio.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un utilisateur utilise déjà cette adresse email"
            );
        }

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Le rôle ADMIN est introuvable"
                ));

        User user = new User();
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(adminRole));

        User savedUser = userRepository.save(user);

        return toAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Adresse email ou mot de passe incorrect"
                ));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Adresse email ou mot de passe incorrect"
            );
        }

        return toAuthResponse(user);
    }

    private AuthResponse toAuthResponse(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                roleNames,
                token
        );
    }
}