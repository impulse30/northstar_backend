package com.northstar.portfolio.user.config;

import com.northstar.portfolio.user.entity.Role;
import com.northstar.portfolio.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        createRoleIfMissing("ADMIN");
        createRoleIfMissing("EDITOR");
    }

    private void createRoleIfMissing(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            roleRepository.save(new Role(roleName));
        }
    }
}