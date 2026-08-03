package io.github.williamandradesantana.sports.application.user;

import io.github.williamandradesantana.sports.domain.user.Permission;
import io.github.williamandradesantana.sports.domain.user.User;
import io.github.williamandradesantana.sports.domain.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Set;
import java.util.stream.Collectors;

public class LoginUseCase {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public LoginUseCase(TokenService tokenService, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public String execute(LoginCommand command) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(command.username(), command.rawPassword())
        );
        User user = userRepository.findByUsername(command.username())
                .orElseThrow(() -> new IllegalStateException("User not found!"));
        Set<String> roles = user.getPermissions().stream().map(Permission::getDescription).collect(Collectors.toSet());

        return tokenService.generateToken(user.getUsername(), roles);
    }
}
