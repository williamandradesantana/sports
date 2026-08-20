package io.github.williamandradesantana.sports.infrastructure.security;

import io.github.williamandradesantana.sports.application.audit.RecordAccessLogUseCase;
import io.github.williamandradesantana.sports.application.user.GoogleLoginUseCase;
import io.github.williamandradesantana.sports.application.user.TokenService;
import io.github.williamandradesantana.sports.domain.user.UserRepository;
import io.github.williamandradesantana.sports.infrastructure.security.audit.AccessLogListener;
import io.github.williamandradesantana.sports.infrastructure.security.jwt.JwtAuthenticationFilter;
import io.github.williamandradesantana.sports.infrastructure.security.jwt.JwtProperties;
import io.github.williamandradesantana.sports.infrastructure.security.jwt.JwtService;
import io.github.williamandradesantana.sports.infrastructure.security.oauth2.CookieOAuth2AuthorizationRequestRepository;
import io.github.williamandradesantana.sports.infrastructure.security.oauth2.OAuth2LoginFailureHandler;
import io.github.williamandradesantana.sports.infrastructure.security.oauth2.OAuth2LoginSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    @Value("${app.oauth2.authorized-redirect-uri}")
    private String authorizedRedirectUri;

    @Bean
    public CookieOAuth2AuthorizationRequestRepository authorizationRequestRepository() {
        return new CookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public AccessLogListener accessLogListener(RecordAccessLogUseCase recordAccessLogUseCase) {
        return new AccessLogListener(recordAccessLogUseCase);
    }

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler(
            GoogleLoginUseCase googleLoginUseCase, RecordAccessLogUseCase recordAccessLogUseCase
    ) {
        return new OAuth2LoginSuccessHandler(googleLoginUseCase, recordAccessLogUseCase, authorizedRedirectUri);
    }

    @Bean
    public OAuth2LoginFailureHandler oAuth2LoginFailureHandler() {
        return new OAuth2LoginFailureHandler(authorizedRedirectUri);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtService jwtService(JwtProperties properties) {
        return new JwtService(properties);
    }

    @Bean
    public TokenService tokenService(JwtService jwtService) {
        return jwtService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService) {
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            CorsConfigurationSource corsConfigurationSource,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CookieOAuth2AuthorizationRequestRepository authorizationRequestRepository,
            OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
            OAuth2LoginFailureHandler oAuth2LoginFailureHandler
    ) {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                    auth
                        .requestMatchers(
                                "/api/v1/auth/**", "/oauth2/**",
                                "/scalar", "/scalar/**", "/v3/api-docs/**", "/swagger-ui/**"
                        ).permitAll()
                        .requestMatchers(
                            HttpMethod.GET,
                            "/api/v1/leagues/**", "/api/v1/teams/**", "/api/v1/matches/**", "/api/v1/standings/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint((
                                (request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN))))
                .oauth2Login(oauth2 ->
                    oauth2.authorizationEndpoint(
                        endpoint -> endpoint.authorizationRequestRepository(authorizationRequestRepository))
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
