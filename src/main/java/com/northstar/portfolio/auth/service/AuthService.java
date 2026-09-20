package com.northstar.portfolio.auth.service;


import com.northstar.portfolio.auth.dto.AuthResponse;
import com.northstar.portfolio.auth.dto.RegisterRequest;
import com.northstar.portfolio.user.entity.Role;
import com.northstar.portfolio.user.entity.User;
import com.northstar.portfolio.user.repository.RoleRepository;
import com.northstar.portfolio.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un utilisateur avec l'adresse email " + email + " existe deja");


        }
        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(() -> new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Role ADMIN est introuvable"));

        User user = new User();
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(adminRole));

        User savedUser = userRepository.save(user);

        return new AuthResponse(savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRoles().stream()
                        .map(Role::getName)
                        .collect(java.util.stream.Collectors.toSet()),
                null);



    }
}
