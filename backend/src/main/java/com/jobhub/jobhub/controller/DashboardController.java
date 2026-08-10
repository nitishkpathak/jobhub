package com.jobhub.jobhub.controller;

import com.jobhub.jobhub.dto.ApiResponse;
import com.jobhub.jobhub.dto.CandidateDashboardDto;
import com.jobhub.jobhub.dto.RecruiterDashboardDto;
import com.jobhub.jobhub.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // Candidate Dashboard Metrics
    @GetMapping("/candidate")
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CandidateDashboardDto>> getCandidateDashboard(Authentication authentication) {
        String candidateEmail = authentication.getName();
        CandidateDashboardDto dashboard = dashboardService.getCandidateDashboard(candidateEmail);
        return ResponseEntity.ok(ApiResponse.success("Candidate dashboard metrics retrieved successfully", dashboard));
    }

    // Recruiter Dashboard Metrics
    @GetMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RecruiterDashboardDto>> getRecruiterDashboard(Authentication authentication) {
        String recruiterEmail = authentication.getName();
        RecruiterDashboardDto dashboard = dashboardService.getRecruiterDashboard(recruiterEmail);
        return ResponseEntity.ok(ApiResponse.success("Recruiter dashboard metrics retrieved successfully", dashboard));
    }
}
