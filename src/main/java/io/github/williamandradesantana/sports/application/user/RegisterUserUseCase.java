package io.github.williamandradesantana.sports.application.user;

import io.github.williamandradesantana.sports.domain.user.*;
import io.github.williamandradesantana.sports.domain.user.exceptions.EmailAlreadyExistsException;
import io.github.williamandradesantana.sports.domain.user.exceptions.UsernameAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepository userRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(RegisterUserCommand command) {
        userRepository.findByUsername(command.username()).ifPresent(existing -> {
            throw new UsernameAlreadyExistsException("Username already exists: " + command.username());
        });

        userRepository.findByEmail(command.email()).ifPresent(existing -> {
            throw new EmailAlreadyExistsException("Email already exists: " + command.username());
        });

        Permission defaultPermission = permissionRepository.findByDescription(PermissionName.COMMON_USER)
                .orElseThrow(() -> new IllegalStateException("Default permission not seeded: " + PermissionName.COMMON_USER));

        String encodedPassword = passwordEncoder.encode(command.rawPassword());

        User user = new User(
            UUID.randomUUID(),
            command.username(),
            command.fullName(),
            command.email(),
            encodedPassword,
            AuthProvider.LOCAL,
            true, true, true, true,
            Set.of(defaultPermission)
        );
        userRepository.save(user);
        return user;
    }
}
