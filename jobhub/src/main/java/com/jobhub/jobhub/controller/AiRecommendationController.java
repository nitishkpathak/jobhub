package com.jobhub.jobhub.controller;

import com.jobhub.jobhub.dto.ApiResponse;
import com.jobhub.jobhub.dto.AiRecommendationResponseDto;
import com.jobhub.jobhub.service.AiRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;

    public AiRecommendationController(AiRecommendationService aiRecommendationService) {
        this.aiRecommendationService = aiRecommendationService;
    }

    // Get AI / Smart Job Recommendations for Candidate
    @GetMapping("/recommendations")
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AiRecommendationResponseDto>> getRecommendations(Authentication authentication) {
        String candidateEmail = authentication.getName();
        AiRecommendationResponseDto recommendations = aiRecommendationService.getRecommendations(candidateEmail);
        return ResponseEntity.ok(ApiResponse.success("AI Job recommendations generated successfully", recommendations));
    }
}
