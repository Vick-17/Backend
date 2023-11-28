package com.tindMovie.tindMovie.Security;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager, JwtUtils jwtUtils) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, "/actors/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comment/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/genre/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie/allMovieByUser/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/note/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/realisator/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/match/**").hasAnyAuthority("ROLE_USER", "ROLE_MODO")
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyAuthority("ROLE_USER", "ROLE_MODO")
                        .requestMatchers(HttpMethod.POST, "/note/**").hasAnyAuthority("ROLE_USER", "ROLE_MODO")
                        .requestMatchers(HttpMethod.POST, "/swipe/**").hasAnyAuthority("ROLE_USER", "ROLE_MODO")
                        .requestMatchers(HttpMethod.GET, "/swipe/**").hasAnyAuthority("ROLE_USER", "ROLE_MODO")
                        .requestMatchers(HttpMethod.PUT, "/swipe/**").hasAnyAuthority("ROLE_USER", "ROLE_MODO")
                        .requestMatchers(HttpMethod.DELETE, "/swipe/**").hasAnyAuthority("ROLE_USER", "ROLE_MODO")
                        .requestMatchers(HttpMethod.POST, "/comment/**").hasAnyAuthority("ROLE_USER", "ROLE_MODO")
                        .requestMatchers(HttpMethod.POST, "/actors/**").hasAnyAuthority("ROLE_USER", "ROLE_MODO")
                        .requestMatchers(HttpMethod.PUT, "/actors/**").hasAnyAuthority("ROLE_USER", "ROLE_MODO")
                        .requestMatchers(HttpMethod.DELETE, "/actors/**").hasAnyAuthority("ROLE_USER", "ROLE_MODO")
                        .anyRequest().authenticated()
                )
                .addFilter(new CustomAuthenticationFilter(authenticationManager, jwtUtils))
                .addFilterBefore(
                        new CustomAuthorizationFilter(jwtUtils),
                        UsernamePasswordAuthenticationFilter.class
                )
                .headers(headers -> headers.cacheControl(Customizer.withDefaults()));
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://tind-movie.vercel.app/"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setExposedHeaders(List.of("access_token"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
