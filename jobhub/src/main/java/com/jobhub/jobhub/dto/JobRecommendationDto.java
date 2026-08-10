package com.jobhub.jobhub.dto;

import java.util.List;

public class JobRecommendationDto {

    private JobResponseDto job;
    private int matchPercentage;
    private List<String> matchingSkills;
    private List<String> missingSkills;
    private List<String> suggestedSkillsToLearn;
    private String reason;

    public JobRecommendationDto() {
    }

    public JobRecommendationDto(JobResponseDto job, int matchPercentage, List<String> matchingSkills, List<String> missingSkills, List<String> suggestedSkillsToLearn, String reason) {
        this.job = job;
        this.matchPercentage = matchPercentage;
        this.matchingSkills = matchingSkills;
        this.missingSkills = missingSkills;
        this.suggestedSkillsToLearn = suggestedSkillsToLearn;
        this.reason = reason;
    }

    public JobResponseDto getJob() {
        return job;
    }

    public void setJob(JobResponseDto job) {
        this.job = job;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public List<String> getMatchingSkills() {
        return matchingSkills;
    }

    public void setMatchingSkills(List<String> matchingSkills) {
        this.matchingSkills = matchingSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<String> getSuggestedSkillsToLearn() {
        return suggestedSkillsToLearn;
    }

    public void setSuggestedSkillsToLearn(List<String> suggestedSkillsToLearn) {
        this.suggestedSkillsToLearn = suggestedSkillsToLearn;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
