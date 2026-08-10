package com.jobhub.jobhub.dto;

import java.util.List;

public class AiRecommendationResponseDto {

    private String engineUsed;
    private String candidateSkills;
    private int totalRecommendations;
    private List<JobRecommendationDto> recommendations;

    public AiRecommendationResponseDto() {
    }

    public AiRecommendationResponseDto(String engineUsed, String candidateSkills, int totalRecommendations, List<JobRecommendationDto> recommendations) {
        this.engineUsed = engineUsed;
        this.candidateSkills = candidateSkills;
        this.totalRecommendations = totalRecommendations;
        this.recommendations = recommendations;
    }

    public String getEngineUsed() {
        return engineUsed;
    }

    public void setEngineUsed(String engineUsed) {
        this.engineUsed = engineUsed;
    }

    public String getCandidateSkills() {
        return candidateSkills;
    }

    public void setCandidateSkills(String candidateSkills) {
        this.candidateSkills = candidateSkills;
    }

    public int getTotalRecommendations() {
        return totalRecommendations;
    }

    public void setTotalRecommendations(int totalRecommendations) {
        this.totalRecommendations = totalRecommendations;
    }

    public List<JobRecommendationDto> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<JobRecommendationDto> recommendations) {
        this.recommendations = recommendations;
    }
}
