package sba301.java.opentalk.config;

import java.util.List;

public class EndpointPermissions {
    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    );

    public static final List<String> HR_ENDPOINTS = List.of(
            "/api/company-branch/**"
    );

    public static final List<String> USER_ENDPOINTS = List.of(
            "/api/company-branch/**",//GET
            "/api/topic-idea/suggestedBy/**",
            "/api/opentalk-meeting/**",
            "/api/topic-poll/**",
            "/api/topic-vote/**",
            "/api/attendance/checkin",
            "/api/attendance/checkin-status"
    );

    public static final List<String> MEETING_MANAGER_ENDPOINTS = List.of(
            "/api/users",
            "/api/cron",
            "/api/health-check",
            "/api/topic-idea/**",
            "/api/attendance/generate-checkin-code",
            "/api/opentalk-meeting/**",
            "/api/hosts/**",
            "/api/company-branch/**",
            "/api/users/**"
    );
}
