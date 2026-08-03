package io.github.williamandradesantana.sports.interfaces.auth;

import io.github.williamandradesantana.sports.application.user.LoginCommand;
import io.github.williamandradesantana.sports.application.user.LoginUseCase;
import io.github.williamandradesantana.sports.application.user.RegisterUserCommand;
import io.github.williamandradesantana.sports.application.user.RegisterUserUseCase;
import io.github.williamandradesantana.sports.domain.user.User;
import io.github.williamandradesantana.sports.interfaces.auth.dto.LoginRequest;
import io.github.williamandradesantana.sports.interfaces.auth.dto.LoginResponse;
import io.github.williamandradesantana.sports.interfaces.auth.dto.RegisterRequest;
import io.github.williamandradesantana.sports.interfaces.auth.dto.RegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase, LoginUseCase loginUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping(
        value = "/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        User user = registerUserUseCase.execute(
            new RegisterUserCommand(request.username(), request.fullName(), request.password())
        );
        RegisterResponse response = new RegisterResponse(user.getId(), user.getUsername(), user.getFullName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(
        value = "/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = loginUseCase.execute(new LoginCommand(request.username(), request.password()));
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
