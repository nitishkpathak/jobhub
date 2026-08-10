package com.jobhub.jobhub.controller;

import com.jobhub.jobhub.dto.*;
import com.jobhub.jobhub.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // Search & Browse Companies with Pagination
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CompanyResponseDto>>> getAllCompanies(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PageResponse<CompanyResponseDto> response = companyService.searchCompanies(
                keyword, industry, location, page, size, sortBy, sortDir
        );
        return ResponseEntity.ok(ApiResponse.success("Companies retrieved successfully", response));
    }

    // Explicit Search Endpoint
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<CompanyResponseDto>>> searchCompanies(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PageResponse<CompanyResponseDto> response = companyService.searchCompanies(
                keyword, industry, location, page, size, sortBy, sortDir
        );
        return ResponseEntity.ok(ApiResponse.success("Companies search retrieved successfully", response));
    }

    // Get Company Statistics
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<CompanyStatsDto>> getCompanyStats() {
        CompanyStatsDto stats = companyService.getCompanyStats();
        return ResponseEntity.ok(ApiResponse.success("Company statistics retrieved successfully", stats));
    }

    // Get Single Company By ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponseDto>> getCompanyById(@PathVariable Long id) {
        CompanyResponseDto company = companyService.getCompanyById(id);
        return ResponseEntity.ok(ApiResponse.success("Company details retrieved successfully", company));
    }

    // Get Jobs belonging to a specific company
    @GetMapping("/{id}/jobs")
    public ResponseEntity<ApiResponse<PageResponse<JobResponseDto>>> getCompanyJobs(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        PageResponse<JobResponseDto> jobs = companyService.getCompanyJobs(id, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success("Company jobs retrieved successfully", jobs));
    }

    // Create or Update Company (Recruiter Only)
    @PostMapping
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CompanyResponseDto>> createOrUpdateCompany(
            @Valid @RequestBody CompanyRequestDto dto,
            Authentication authentication) {

        String recruiterEmail = authentication.getName();
        CompanyResponseDto company = companyService.createOrUpdateCompany(dto, recruiterEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Company profile saved successfully", company));
    }

    // Delete Company (Recruiter Only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(
            @PathVariable Long id,
            Authentication authentication) {

        String recruiterEmail = authentication.getName();
        companyService.deleteCompany(id, recruiterEmail);
        return ResponseEntity.ok(ApiResponse.success("Company profile deleted successfully", null));
    }
}
