package com.jobhub.jobhub.dto;

import java.util.List;

public class RecruiterDashboardDto {

    private long totalJobsPosted;
    private long totalApplicationsReceived;
    private long shortlistedCandidatesCount;
    private long selectedCandidatesCount;
    private List<JobResponseDto> recentJobs;
    private List<ApplicationResponseDto> recentApplications;

    public RecruiterDashboardDto() {
    }

    public RecruiterDashboardDto(long totalJobsPosted, long totalApplicationsReceived, long shortlistedCandidatesCount, long selectedCandidatesCount, List<JobResponseDto> recentJobs, List<ApplicationResponseDto> recentApplications) {
        this.totalJobsPosted = totalJobsPosted;
        this.totalApplicationsReceived = totalApplicationsReceived;
        this.shortlistedCandidatesCount = shortlistedCandidatesCount;
        this.selectedCandidatesCount = selectedCandidatesCount;
        this.recentJobs = recentJobs;
        this.recentApplications = recentApplications;
    }

    public long getTotalJobsPosted() {
        return totalJobsPosted;
    }

    public void setTotalJobsPosted(long totalJobsPosted) {
        this.totalJobsPosted = totalJobsPosted;
    }

    public long getTotalApplicationsReceived() {
        return totalApplicationsReceived;
    }

    public void setTotalApplicationsReceived(long totalApplicationsReceived) {
        this.totalApplicationsReceived = totalApplicationsReceived;
    }

    public long getShortlistedCandidatesCount() {
        return shortlistedCandidatesCount;
    }

    public void setShortlistedCandidatesCount(long shortlistedCandidatesCount) {
        this.shortlistedCandidatesCount = shortlistedCandidatesCount;
    }

    public long getSelectedCandidatesCount() {
        return selectedCandidatesCount;
    }

    public void setSelectedCandidatesCount(long selectedCandidatesCount) {
        this.selectedCandidatesCount = selectedCandidatesCount;
    }

    public List<JobResponseDto> getRecentJobs() {
        return recentJobs;
    }

    public void setRecentJobs(List<JobResponseDto> recentJobs) {
        this.recentJobs = recentJobs;
    }

    public List<ApplicationResponseDto> getRecentApplications() {
        return recentApplications;
    }

    public void setRecentApplications(List<ApplicationResponseDto> recentApplications) {
        this.recentApplications = recentApplications;
    }
}
