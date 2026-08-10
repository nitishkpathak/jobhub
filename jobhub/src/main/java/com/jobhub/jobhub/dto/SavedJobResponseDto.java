package com.jobhub.jobhub.dto;

import java.time.LocalDateTime;

public class SavedJobResponseDto {

    private Long id;
    private Long candidateId;
    private JobResponseDto job;
    private LocalDateTime savedAt;

    public SavedJobResponseDto() {
    }

    public SavedJobResponseDto(Long id, Long candidateId, JobResponseDto job, LocalDateTime savedAt) {
        this.id = id;
        this.candidateId = candidateId;
        this.job = job;
        this.savedAt = savedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public JobResponseDto getJob() {
        return job;
    }

    public void setJob(JobResponseDto job) {
        this.job = job;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }
}
