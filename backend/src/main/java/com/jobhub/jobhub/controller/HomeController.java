package com.jobhub.jobhub.controller;

import com.jobhub.jobhub.dto.ApiResponse;
import com.jobhub.jobhub.entity.JobType;
import com.jobhub.jobhub.repository.JobRepository;
import com.jobhub.jobhub.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public HomeController(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHomeStats() {
        long totalJobs = jobRepository.count();
        long totalCompanies = jobRepository.findAll().stream()
                .map(j -> j.getCompanyName())
                .filter(c -> c != null && !c.isBlank())
                .distinct()
                .count();
        long internships = jobRepository.findAll().stream()
                .filter(j -> j.getJobType() == JobType.INTERNSHIP)
                .count();
        long activeRecruiters = userRepository.findAll().stream()
                .filter(u -> "RECRUITER".equalsIgnoreCase(u.getRole()))
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobs", totalJobs);
        stats.put("totalCompanies", totalCompanies);
        stats.put("internships", internships);
        stats.put("activeRecruiters", activeRecruiters);

        return ResponseEntity.ok(ApiResponse.success("Home statistics retrieved successfully", stats));
    }
}
