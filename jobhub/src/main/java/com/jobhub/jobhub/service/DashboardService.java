package com.jobhub.jobhub.service;

import com.jobhub.jobhub.dto.ApplicationResponseDto;
import com.jobhub.jobhub.dto.CandidateDashboardDto;
import com.jobhub.jobhub.dto.JobResponseDto;
import com.jobhub.jobhub.dto.RecruiterDashboardDto;
import com.jobhub.jobhub.entity.Application;
import com.jobhub.jobhub.entity.ApplicationStatus;
import com.jobhub.jobhub.entity.Job;
import com.jobhub.jobhub.entity.User;
import com.jobhub.jobhub.exception.ResourceNotFoundException;
import com.jobhub.jobhub.repository.ApplicationRepository;
import com.jobhub.jobhub.repository.JobRepository;
import com.jobhub.jobhub.repository.SavedJobRepository;
import com.jobhub.jobhub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final SavedJobRepository savedJobRepository;
    private final ApplicationService applicationService;
    private final JobService jobService;

    public DashboardService(UserRepository userRepository, JobRepository jobRepository, ApplicationRepository applicationRepository, SavedJobRepository savedJobRepository, ApplicationService applicationService, JobService jobService) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.savedJobRepository = savedJobRepository;
        this.applicationService = applicationService;
        this.jobService = jobService;
    }

    // Candidate Dashboard Statistics
    public CandidateDashboardDto getCandidateDashboard(String candidateEmail) {
        User candidate = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate account not found"));

        Long candidateId = candidate.getId();

        long totalApps = applicationRepository.countByCandidateId(candidateId);
        long reviewingApps = applicationRepository.countByCandidateIdAndStatus(candidateId, ApplicationStatus.REVIEWING);
        long shortlistedApps = applicationRepository.countByCandidateIdAndStatus(candidateId, ApplicationStatus.SHORTLISTED);
        long selectedApps = applicationRepository.countByCandidateIdAndStatus(candidateId, ApplicationStatus.SELECTED);
        long rejectedApps = applicationRepository.countByCandidateIdAndStatus(candidateId, ApplicationStatus.REJECTED);
        long savedJobsCount = savedJobRepository.countByCandidateId(candidateId);

        List<ApplicationResponseDto> recentApplications = applicationService.getCandidateApplications(candidateEmail);
        // Limit to 5 most recent
        if (recentApplications.size() > 5) {
            recentApplications = recentApplications.subList(0, 5);
        }

        return new CandidateDashboardDto(
                totalApps,
                reviewingApps,
                shortlistedApps,
                selectedApps,
                rejectedApps,
                savedJobsCount,
                recentApplications
        );
    }

    // Recruiter Dashboard Statistics
    public RecruiterDashboardDto getRecruiterDashboard(String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter account not found"));

        List<Job> recruiterJobs = jobRepository.findByRecruiterId(recruiter.getId());
        long totalJobsPosted = recruiterJobs.size();

        List<Long> jobIds = recruiterJobs.stream().map(Job::getId).collect(Collectors.toList());

        long totalApplicationsReceived = 0;
        long shortlistedCount = 0;
        long selectedCount = 0;
        List<ApplicationResponseDto> recentAppsList = Collections.emptyList();

        if (!jobIds.isEmpty()) {
            totalApplicationsReceived = applicationRepository.countByJobIdIn(jobIds);
            shortlistedCount = applicationRepository.countByJobIdInAndStatus(jobIds, ApplicationStatus.SHORTLISTED);
            selectedCount = applicationRepository.countByJobIdInAndStatus(jobIds, ApplicationStatus.SELECTED);

            List<Application> recentApplications = applicationRepository.findByJobIdIn(jobIds);
            recentAppsList = recentApplications.stream()
                    .map(app -> {
                        Job job = jobRepository.findById(app.getJobId()).orElse(null);
                        User candidate = userRepository.findById(app.getCandidateId()).orElse(null);
                        return new ApplicationResponseDto(
                                app.getId(),
                                app.getJobId(),
                                job != null ? job.getTitle() : "Unknown",
                                job != null ? job.getCompanyName() : "Unknown",
                                app.getCandidateId(),
                                candidate != null ? candidate.getName() : "Unknown",
                                candidate != null ? candidate.getEmail() : "Unknown",
                                app.getResumeUrl(),
                                app.getCoverLetter(),
                                app.getStatus(),
                                app.getAppliedAt(),
                                app.getUpdatedAt()
                        );
                    })
                    .limit(5)
                    .collect(Collectors.toList());
        }

        List<JobResponseDto> recentJobsList = recruiterJobs.stream()
                .map(jobService::mapToResponseDto)
                .limit(5)
                .collect(Collectors.toList());

        return new RecruiterDashboardDto(
                totalJobsPosted,
                totalApplicationsReceived,
                shortlistedCount,
                selectedCount,
                recentJobsList,
                recentAppsList
        );
    }
}
