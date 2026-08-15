package io.github.williamandradesantana.sports.application.user;

import io.github.williamandradesantana.sports.domain.user.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class GoogleLoginUseCase {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final TokenService tokenService;

    public GoogleLoginUseCase(UserRepository userRepository, PermissionRepository permissionRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.tokenService = tokenService;
    }

    public User resolveUser(GoogleProfileCommand command) {
        return userRepository.findByEmail(command.email()).orElseGet(() -> registerNewGoogleUser(command));
    }

    public String generateTokenFor(User user) {
        Set<String> roles = user.getPermissions().stream()
                .map(Permission::getDescription)
                .collect(Collectors.toSet());

        return tokenService.generateToken(user.getUsername(), roles);
    }

    private User registerNewGoogleUser(GoogleProfileCommand command) {
        Permission defaultPermission = permissionRepository.findByDescription(PermissionName.COMMON_USER)
                .orElseThrow(() -> new IllegalStateException("Default permission not seeded: " + PermissionName.COMMON_USER));
        
        String username = generateUniqueUsername(command.email());

        User user = new User(
            UUID.randomUUID(),
            username,
            command.fullName(),
            command.email(),
            null,
            AuthProvider.GOOGLE,
            true, true, true, true,
            Set.of(defaultPermission)
        );

        userRepository.save(user);
        return user;
    }

    private String generateUniqueUsername(String email) {
        String base = email.substring(0, email.indexOf('@')).replaceAll("[^a-zA-Z0-9._-]", "");
        String candidate = base;
        int suffix = 1;
        while (userRepository.findByUsername(candidate).isPresent()) {
            candidate = base + suffix;
            suffix++;
        }
        return candidate;
    }
}
