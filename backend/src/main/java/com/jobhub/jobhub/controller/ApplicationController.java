package com.jobhub.jobhub.controller;

import com.jobhub.jobhub.dto.ApiResponse;
import com.jobhub.jobhub.dto.ApplicationRequestDto;
import com.jobhub.jobhub.dto.ApplicationResponseDto;
import com.jobhub.jobhub.dto.UpdateStatusDto;
import com.jobhub.jobhub.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Candidate applies for a job
    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> applyForJob(
            @Valid @RequestBody ApplicationRequestDto dto,
            Authentication authentication) {

        String candidateEmail = authentication.getName();
        ApplicationResponseDto response = applicationService.applyForJob(dto, candidateEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job application submitted successfully", response));
    }

    // Candidate views own applications
    @GetMapping("/my")
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getMyApplications(Authentication authentication) {
        String candidateEmail = authentication.getName();
        List<ApplicationResponseDto> applications = applicationService.getCandidateApplications(candidateEmail);
        return ResponseEntity.ok(ApiResponse.success("My applications retrieved successfully", applications));
    }

    // View single application details by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> getApplicationById(
            @PathVariable Long id,
            Authentication authentication) {

        String userEmail = authentication.getName();
        ApplicationResponseDto application = applicationService.getApplicationById(id, userEmail);
        return ResponseEntity.ok(ApiResponse.success("Application details retrieved successfully", application));
    }

    // Recruiter views applications for a job
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getApplicationsForJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        String recruiterEmail = authentication.getName();
        List<ApplicationResponseDto> applications = applicationService.getApplicationsForJob(jobId, recruiterEmail);
        return ResponseEntity.ok(ApiResponse.success("Job applications retrieved successfully", applications));
    }

    // Recruiter updates application status
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusDto dto,
            Authentication authentication) {

        String recruiterEmail = authentication.getName();
        ApplicationResponseDto response = applicationService.updateApplicationStatus(id, dto.getStatus(), recruiterEmail);
        return ResponseEntity.ok(ApiResponse.success("Application status updated to " + dto.getStatus(), response));
    }

    // Candidate withdraws / deletes application
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> withdrawApplication(
            @PathVariable Long id,
            Authentication authentication) {

        String candidateEmail = authentication.getName();
        applicationService.deleteApplication(id, candidateEmail);
        return ResponseEntity.ok(ApiResponse.success("Application withdrawn successfully", null));
    }
}
