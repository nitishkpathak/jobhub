package com.jobhub.jobhub.service;

import com.jobhub.jobhub.dto.JobResponseDto;
import com.jobhub.jobhub.dto.SavedJobResponseDto;
import com.jobhub.jobhub.entity.Job;
import com.jobhub.jobhub.entity.SavedJob;
import com.jobhub.jobhub.entity.User;
import com.jobhub.jobhub.exception.DuplicateApplicationException;
import com.jobhub.jobhub.exception.ResourceNotFoundException;
import com.jobhub.jobhub.repository.JobRepository;
import com.jobhub.jobhub.repository.SavedJobRepository;
import com.jobhub.jobhub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JobService jobService;

    public SavedJobService(SavedJobRepository savedJobRepository, JobRepository jobRepository, UserRepository userRepository, JobService jobService) {
        this.savedJobRepository = savedJobRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.jobService = jobService;
    }

    // Save / Favorite a Job (Candidate Only)
    public SavedJobResponseDto saveJob(Long jobId, String candidateEmail) {
        User candidate = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate account not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        if (savedJobRepository.existsByCandidateIdAndJobId(candidate.getId(), job.getId())) {
            throw new DuplicateApplicationException("Job is already saved in your bookmarks");
        }

        SavedJob savedJob = new SavedJob();
        savedJob.setCandidateId(candidate.getId());
        savedJob.setJobId(job.getId());

        SavedJob newSavedJob = savedJobRepository.save(savedJob);
        JobResponseDto jobDto = jobService.mapToResponseDto(job);

        return new SavedJobResponseDto(newSavedJob.getId(), candidate.getId(), jobDto, newSavedJob.getCreatedAt());
    }

    // Get All Saved Jobs for Candidate
    public List<SavedJobResponseDto> getSavedJobs(String candidateEmail) {
        User candidate = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate account not found"));

        return savedJobRepository.findByCandidateId(candidate.getId())
                .stream()
                .map(savedJob -> {
                    Job job = jobRepository.findById(savedJob.getJobId()).orElse(null);
                    JobResponseDto jobDto = job != null ? jobService.mapToResponseDto(job) : null;
                    return new SavedJobResponseDto(savedJob.getId(), candidate.getId(), jobDto, savedJob.getCreatedAt());
                })
                .collect(Collectors.toList());
    }

    // Remove Saved Job Bookmark (Candidate Only)
    @Transactional
    public void removeSavedJob(Long jobId, String candidateEmail) {
        User candidate = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate account not found"));

        if (!savedJobRepository.existsByCandidateIdAndJobId(candidate.getId(), jobId)) {
            throw new ResourceNotFoundException("Saved job bookmark not found for job id: " + jobId);
        }

        savedJobRepository.deleteByCandidateIdAndJobId(candidate.getId(), jobId);
    }
}
