package com.jobhub.jobhub.service;

import com.jobhub.jobhub.dto.ApplicationRequestDto;
import com.jobhub.jobhub.dto.ApplicationResponseDto;
import com.jobhub.jobhub.entity.Application;
import com.jobhub.jobhub.entity.ApplicationStatus;
import com.jobhub.jobhub.entity.Job;
import com.jobhub.jobhub.entity.User;
import com.jobhub.jobhub.exception.DuplicateApplicationException;
import com.jobhub.jobhub.exception.ResourceNotFoundException;
import com.jobhub.jobhub.exception.UnauthorizedAccessException;
import com.jobhub.jobhub.repository.ApplicationRepository;
import com.jobhub.jobhub.repository.JobRepository;
import com.jobhub.jobhub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository, JobRepository jobRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    // Apply for a Job (Candidate Only)
    public ApplicationResponseDto applyForJob(ApplicationRequestDto dto, String candidateEmail) {
        User candidate = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate account not found"));

        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + dto.getJobId()));

        if (applicationRepository.existsByJobIdAndCandidateId(job.getId(), candidate.getId())) {
            throw new DuplicateApplicationException("You have already applied for this job");
        }

        Application application = new Application();
        application.setJobId(job.getId());
        application.setCandidateId(candidate.getId());
        application.setResumeUrl(dto.getResumeUrl());
        application.setCoverLetter(dto.getCoverLetter());
        application.setStatus(ApplicationStatus.APPLIED);

        Application savedApplication = applicationRepository.save(application);
        return mapToResponseDto(savedApplication, job, candidate);
    }

    // Get My Applications (Candidate Only)
    public List<ApplicationResponseDto> getCandidateApplications(String candidateEmail) {
        User candidate = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate account not found"));

        return applicationRepository.findByCandidateId(candidate.getId())
                .stream()
                .map(app -> {
                    Job job = jobRepository.findById(app.getJobId()).orElse(null);
                    return mapToResponseDto(app, job, candidate);
                })
                .collect(Collectors.toList());
    }

    // Get Application Details By ID
    public ApplicationResponseDto getApplicationById(Long applicationId, String userEmail) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User account not found"));

        Job job = jobRepository.findById(application.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Associated job not found"));

        boolean isCandidateOwner = application.getCandidateId().equals(user.getId());
        boolean isRecruiterOwner = job.getRecruiterId().equals(user.getId());

        if (!isCandidateOwner && !isRecruiterOwner && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new UnauthorizedAccessException("You are not authorized to view this application");
        }

        User candidate = userRepository.findById(application.getCandidateId()).orElse(null);
        return mapToResponseDto(application, job, candidate);
    }

    // Get Applications For A Job (Recruiter Owner Only)
    public List<ApplicationResponseDto> getApplicationsForJob(Long jobId, String recruiterEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter account not found"));

        if (!job.getRecruiterId().equals(recruiter.getId()) && !"ADMIN".equalsIgnoreCase(recruiter.getRole())) {
            throw new UnauthorizedAccessException("You are not authorized to view applications for this job");
        }

        return applicationRepository.findByJobId(jobId)
                .stream()
                .map(app -> {
                    User candidate = userRepository.findById(app.getCandidateId()).orElse(null);
                    return mapToResponseDto(app, job, candidate);
                })
                .collect(Collectors.toList());
    }

    // Update Application Status (Recruiter Owner Only)
    public ApplicationResponseDto updateApplicationStatus(Long applicationId, ApplicationStatus status, String recruiterEmail) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        Job job = jobRepository.findById(application.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter account not found"));

        if (!job.getRecruiterId().equals(recruiter.getId()) && !"ADMIN".equalsIgnoreCase(recruiter.getRole())) {
            throw new UnauthorizedAccessException("You are not authorized to update status for this application");
        }

        application.setStatus(status);
        Application updatedApp = applicationRepository.save(application);

        User candidate = userRepository.findById(application.getCandidateId()).orElse(null);
        return mapToResponseDto(updatedApp, job, candidate);
    }

    // Withdraw / Delete Application (Candidate Only)
    public void deleteApplication(Long applicationId, String candidateEmail) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        User candidate = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate account not found"));

        if (!application.getCandidateId().equals(candidate.getId())) {
            throw new UnauthorizedAccessException("You can only withdraw your own applications");
        }

        applicationRepository.delete(application);
    }

    // Helper: Map Application -> Response DTO
    private ApplicationResponseDto mapToResponseDto(Application app, Job job, User candidate) {
        return new ApplicationResponseDto(
                app.getId(),
                app.getJobId(),
                job != null ? job.getTitle() : "Unknown Job",
                job != null ? job.getCompanyName() : "Unknown Company",
                app.getCandidateId(),
                candidate != null ? candidate.getName() : "Unknown Candidate",
                candidate != null ? candidate.getEmail() : "Unknown Email",
                app.getResumeUrl(),
                app.getCoverLetter(),
                app.getStatus(),
                app.getAppliedAt(),
                app.getUpdatedAt()
        );
    }
}
