package com.jobhub.jobhub.controller;

import com.jobhub.jobhub.dto.ApiResponse;
import com.jobhub.jobhub.dto.SavedJobResponseDto;
import com.jobhub.jobhub.service.SavedJobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
public class SavedJobController {

    private final SavedJobService savedJobService;

    public SavedJobController(SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }

    // Save / Bookmark a Job (Candidate Only)
    @PostMapping("/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SavedJobResponseDto>> saveJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        String candidateEmail = authentication.getName();
        SavedJobResponseDto savedJob = savedJobService.saveJob(jobId, candidateEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job saved successfully", savedJob));
    }

    // Get My Saved Jobs (Candidate Only)
    @GetMapping
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SavedJobResponseDto>>> getSavedJobs(Authentication authentication) {
        String candidateEmail = authentication.getName();
        List<SavedJobResponseDto> savedJobs = savedJobService.getSavedJobs(candidateEmail);
        return ResponseEntity.ok(ApiResponse.success("Saved jobs retrieved successfully", savedJobs));
    }

    // Remove Job Bookmark (Candidate Only)
    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removeSavedJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        String candidateEmail = authentication.getName();
        savedJobService.removeSavedJob(jobId, candidateEmail);
        return ResponseEntity.ok(ApiResponse.success("Job removed from saved list", null));
    }
}
