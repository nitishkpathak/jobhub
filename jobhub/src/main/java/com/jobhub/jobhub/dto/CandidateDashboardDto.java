package com.jobhub.jobhub.dto;

import java.util.List;

public class CandidateDashboardDto {

    private long totalApplications;
    private long reviewingApplications;
    private long shortlistedApplications;
    private long selectedApplications;
    private long rejectedApplications;
    private long savedJobsCount;
    private List<ApplicationResponseDto> recentApplications;

    public CandidateDashboardDto() {
    }

    public CandidateDashboardDto(long totalApplications, long reviewingApplications, long shortlistedApplications, long selectedApplications, long rejectedApplications, long savedJobsCount, List<ApplicationResponseDto> recentApplications) {
        this.totalApplications = totalApplications;
        this.reviewingApplications = reviewingApplications;
        this.shortlistedApplications = shortlistedApplications;
        this.selectedApplications = selectedApplications;
        this.rejectedApplications = rejectedApplications;
        this.savedJobsCount = savedJobsCount;
        this.recentApplications = recentApplications;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getReviewingApplications() {
        return reviewingApplications;
    }

    public void setReviewingApplications(long reviewingApplications) {
        this.reviewingApplications = reviewingApplications;
    }

    public long getShortlistedApplications() {
        return shortlistedApplications;
    }

    public void setShortlistedApplications(long shortlistedApplications) {
        this.shortlistedApplications = shortlistedApplications;
    }

    public long getSelectedApplications() {
        return selectedApplications;
    }

    public void setSelectedApplications(long selectedApplications) {
        this.selectedApplications = selectedApplications;
    }

    public long getRejectedApplications() {
        return rejectedApplications;
    }

    public void setRejectedApplications(long rejectedApplications) {
        this.rejectedApplications = rejectedApplications;
    }

    public long getSavedJobsCount() {
        return savedJobsCount;
    }

    public void setSavedJobsCount(long savedJobsCount) {
        this.savedJobsCount = savedJobsCount;
    }

    public List<ApplicationResponseDto> getRecentApplications() {
        return recentApplications;
    }

    public void setRecentApplications(List<ApplicationResponseDto> recentApplications) {
        this.recentApplications = recentApplications;
    }
}
