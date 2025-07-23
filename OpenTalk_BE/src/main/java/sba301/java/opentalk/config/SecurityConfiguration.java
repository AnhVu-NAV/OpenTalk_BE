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
                                "/api/files/**",
                                "/api/users/**")
                        .permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/company-branch/**").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers("/api/company-branch/**").hasRole("MEETING_MANAGER")
                        .requestMatchers("/api/cron/**").hasRole("MEETING_MANAGER")
                        .requestMatchers("/api/attendance/generate-checkin-code").hasRole("MEETING_MANAGER")
                        .requestMatchers("/api/attendance/checkin-code").hasRole("MEETING_MANAGER")
                        .requestMatchers("/api/attendance/summary").hasRole("HR")
                        .requestMatchers("/api/attendance/user/**").hasRole("HR")
                        .requestMatchers("/api/attendance/**").hasAnyRole("MEETING_MANAGER", "USER", "HR")
                        .requestMatchers("/api/feedbacks/**").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers("/api/files/**").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers("/api/files/**").hasRole("MEETING_MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/poll/**").hasRole("MEETING_MANAGER")
                        .requestMatchers("/api/poll/**").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/topic-idea/admin/status").hasRole("MEETING_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/topic-idea/admin/decision").hasRole("MEETING_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/topic-idea/suggestedBy/**").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/topic-idea/{id}").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/topic-idea").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/topic-idea").hasRole("MEETING_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/topic-idea").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/topic-idea/{id}").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers("/api/topic-poll/**").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers("/api/topic-vote/**").hasAnyRole("MEETING_MANAGER", "USER")
                        .requestMatchers("/api/opentalk-meeting/**").hasAnyRole("USER", "MEETING_MANAGER")
                        .requestMatchers("/api/hr/**").hasRole("HR")
                        .requestMatchers("/api/salaries/**").hasRole("HR")

                        .anyRequest()
                        .authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
