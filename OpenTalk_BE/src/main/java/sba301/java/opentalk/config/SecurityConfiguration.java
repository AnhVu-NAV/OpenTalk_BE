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

                        .requestMatchers(HttpMethod.GET, "/api/company-branch/**").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers("/api/company-branch/**").hasRole("MEETING_MANAGER")
                        .requestMatchers("/api/attendance/generate-checkin-code").hasRole("MEETING_MANAGER")
                        .requestMatchers("/api/attendance/checkin-code").hasRole("MEETING_MANAGER")
                        .requestMatchers("/api/attendance/**").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers("/api/feedbacks/**").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers("/api/files/**").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers("/api/files/**").hasRole("MEETING_MANAGER")

                        .requestMatchers(HttpMethod.DELETE, "/api/poll/**").hasRole("MEETING_MANAGER")

                        .requestMatchers("/api/topic-vote/**")
                        .hasAnyRole("MEETING_MANAGER", "USER")

                        .requestMatchers("/api/topic-idea/suggestedBy/**")
                        .hasAnyRole("MEETING_MANAGER", "USER")

                        .requestMatchers("/api/opentalk-meeting/**")
                        .hasAnyRole("USER", "MEETING_MANAGER")

                        .requestMatchers("/api/topic-idea/**")
                        .hasRole("MEETING_MANAGER")

                        .requestMatchers("/api/opentalk-meeting/**")
                        .hasRole("USER").

                        requestMatchers("/api/topic-poll/**")
                        .hasAnyRole("MEETING_MANAGER", "USER")

                        .requestMatchers(HttpMethod.POST, "/api/attendance/generate-checkin-code")
                        .hasRole("MEETING_MANAGER")

                        .requestMatchers(HttpMethod.POST, "/api/attendance/checkin")
                        .hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/api/attendance/checkin-status")
                        .hasRole("USER")

                        .requestMatchers(
                                "/api/users",
                                "/api/opentalk-topic",
                                "/api/opentalk-meeting",
                                "/api/cron",
                                "/api/health-check",
                                "/api/company-branch/**",
                                "/api/hr/**")
                        .hasRole("MEETING_MANAGER")

                        .anyRequest()
                        .authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
