package com.jobhub.jobhub.controller;

import com.jobhub.jobhub.dto.ApiResponse;
import com.jobhub.jobhub.dto.JobRequestDto;
import com.jobhub.jobhub.dto.JobResponseDto;
import com.jobhub.jobhub.dto.PageResponse;
import com.jobhub.jobhub.entity.JobType;
import com.jobhub.jobhub.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // Post New Job (Recruiter Only)
    @PostMapping
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<JobResponseDto>> createJob(
            @Valid @RequestBody JobRequestDto dto,
            Authentication authentication) {

        String recruiterEmail = authentication.getName();
        JobResponseDto createdJob = jobService.createJob(dto, recruiterEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job posted successfully", createdJob));
    }

    // Search & Filter Jobs with Pagination (Public / Candidates)
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<JobResponseDto>>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) String experienceLevel,
            @RequestParam(required = false) Double minimumSalary,
            @RequestParam(required = false) String skills,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        PageResponse<JobResponseDto> response = jobService.searchJobs(
                keyword, location, jobType, experienceLevel, minimumSalary, skills, page, size, sortBy, sortDir
        );
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved successfully", response));
    }

    // Get Latest Jobs (Public)
    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<List<JobResponseDto>>> getLatestJobs() {
        List<JobResponseDto> jobs = jobService.getLatestJobs();
        return ResponseEntity.ok(ApiResponse.success("Latest jobs retrieved successfully", jobs));
    }

    // Get Job Categories with live counts (Public)
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCategories() {
        List<Map<String, Object>> categories = jobService.getCategories();
        return ResponseEntity.ok(ApiResponse.success("Job categories retrieved successfully", categories));
    }

    // Get All Active Jobs (Public)
    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponseDto>>> getAllJobs() {
        List<JobResponseDto> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved successfully", jobs));
    }

    // Get Single Job By ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponseDto>> getJobById(@PathVariable Long id) {
        JobResponseDto job = jobService.getJobById(id);
        return ResponseEntity.ok(ApiResponse.success("Job details retrieved successfully", job));
    }

    // Get Jobs Posted By A Specific Recruiter
    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<ApiResponse<List<JobResponseDto>>> getJobsByRecruiter(@PathVariable Long recruiterId) {
        List<JobResponseDto> jobs = jobService.getJobsByRecruiterId(recruiterId);
        return ResponseEntity.ok(ApiResponse.success("Recruiter jobs retrieved successfully", jobs));
    }

    // Update Job (Only owner recruiter)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<JobResponseDto>> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequestDto dto,
            Authentication authentication) {

        String recruiterEmail = authentication.getName();
        JobResponseDto updatedJob = jobService.updateJob(id, dto, recruiterEmail);
        return ResponseEntity.ok(ApiResponse.success("Job updated successfully", updatedJob));
    }

    // Delete Job (Only owner recruiter)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @PathVariable Long id,
            Authentication authentication) {

        String recruiterEmail = authentication.getName();
        jobService.deleteJob(id, recruiterEmail);
        return ResponseEntity.ok(ApiResponse.success("Job deleted successfully", null));
    }
}
