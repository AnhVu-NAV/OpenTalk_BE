package sba301.java.opentalk.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sba301.java.opentalk.security.AuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final AuthenticationFilter authenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests((authorize) -> authorize

                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/api/files/**")
                        .permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/company-branch/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/company-branch/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/company-branch/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/topic-idea/suggestedBy/**")
                        .hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/api/opentalk-meeting/**")
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/api/topic-idea/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/topic-poll/**")
                        .hasRole("USER")

                        .requestMatchers(HttpMethod.POST, "/api/attendance/generate-checkin-code")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/attendance/checkin")
                        .hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/api/attendance/checkin-status")
                        .hasRole("USER")

                        .requestMatchers(
                                "/api/users",
                                "/api/opentalk-topic",
                                "/api/cron",
                                "/api/health-check")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
