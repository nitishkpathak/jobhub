package com.jobhub.jobhub.service;

import com.jobhub.jobhub.dto.JobRequestDto;
import com.jobhub.jobhub.dto.JobResponseDto;
import com.jobhub.jobhub.dto.PageResponse;
import com.jobhub.jobhub.entity.Job;
import com.jobhub.jobhub.entity.JobType;
import com.jobhub.jobhub.entity.User;
import com.jobhub.jobhub.exception.ResourceNotFoundException;
import com.jobhub.jobhub.exception.UnauthorizedAccessException;
import com.jobhub.jobhub.repository.JobRepository;
import com.jobhub.jobhub.repository.UserRepository;
import com.jobhub.jobhub.specification.JobSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    // Create Job (Recruiter Only)
    public JobResponseDto createJob(JobRequestDto dto, String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter account not found"));

        if (!"RECRUITER".equalsIgnoreCase(recruiter.getRole()) && !"ADMIN".equalsIgnoreCase(recruiter.getRole())) {
            throw new UnauthorizedAccessException("Only recruiters can post jobs");
        }

        Job job = mapToEntity(dto);
        job.setRecruiterId(recruiter.getId());

        Job savedJob = jobRepository.save(job);
        return mapToResponseDto(savedJob);
    }

    // Get All Jobs
    public List<JobResponseDto> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Search Jobs with Filters & Pagination
    public PageResponse<JobResponseDto> searchJobs(
            String keyword,
            String location,
            JobType jobType,
            String experienceLevel,
            Double minimumSalary,
            String skills,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Job> spec = JobSpecification.filterJobs(
                keyword, location, jobType, experienceLevel, minimumSalary, skills
        );

        Page<Job> jobsPage = jobRepository.findAll(spec, pageable);

        List<JobResponseDto> content = jobsPage.getContent()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                jobsPage.getNumber(),
                jobsPage.getSize(),
                jobsPage.getTotalElements(),
                jobsPage.getTotalPages(),
                jobsPage.isLast()
        );
    }

    // Get Job By ID
    public JobResponseDto getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        return mapToResponseDto(job);
    }

    // Get Jobs Posted By Recruiter
    public List<JobResponseDto> getJobsByRecruiterId(Long recruiterId) {
        if (!userRepository.existsById(recruiterId)) {
            throw new ResourceNotFoundException("Recruiter not found with id: " + recruiterId);
        }
        return jobRepository.findByRecruiterId(recruiterId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Update Job (Only job owner recruiter)
    public JobResponseDto updateJob(Long id, JobRequestDto dto, String recruiterEmail) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        User currentUser = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!existingJob.getRecruiterId().equals(currentUser.getId()) && !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new UnauthorizedAccessException("You are not authorized to update this job");
        }

        existingJob.setTitle(dto.getTitle());
        existingJob.setCompanyName(dto.getCompanyName());
        existingJob.setDescription(dto.getDescription());
        existingJob.setLocation(dto.getLocation());
        existingJob.setJobType(dto.getJobType());
        existingJob.setExperienceLevel(dto.getExperienceLevel());
        existingJob.setSalaryMin(dto.getSalaryMin());
        existingJob.setSalaryMax(dto.getSalaryMax());
        existingJob.setSkills(dto.getSkills());

        Job updatedJob = jobRepository.save(existingJob);
        return mapToResponseDto(updatedJob);
    }

    // Delete Job (Only job owner recruiter)
    public void deleteJob(Long id, String recruiterEmail) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        User currentUser = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!existingJob.getRecruiterId().equals(currentUser.getId()) && !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new UnauthorizedAccessException("You are not authorized to delete this job");
        }

        jobRepository.delete(existingJob);
    }

    // Helper: Map Entity -> Response DTO
    public JobResponseDto mapToResponseDto(Job job) {
        return new JobResponseDto(
                job.getId(),
                job.getTitle(),
                job.getCompanyName(),
                job.getDescription(),
                job.getLocation(),
                job.getJobType(),
                job.getExperienceLevel(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getSkills(),
                job.getRecruiterId(),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }

    // Helper: Map DTO -> Entity
    private Job mapToEntity(JobRequestDto dto) {
        Job job = new Job();
        job.setTitle(dto.getTitle());
        job.setCompanyName(dto.getCompanyName());
        job.setDescription(dto.getDescription());
        job.setLocation(dto.getLocation());
        job.setJobType(dto.getJobType());
        job.setExperienceLevel(dto.getExperienceLevel());
        job.setSalaryMin(dto.getSalaryMin());
        job.setSalaryMax(dto.getSalaryMax());
        job.setSkills(dto.getSkills());
        return job;
    }
}
