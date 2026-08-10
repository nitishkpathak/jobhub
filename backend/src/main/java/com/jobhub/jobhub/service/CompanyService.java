package com.jobhub.jobhub.service;

import com.jobhub.jobhub.dto.*;
import com.jobhub.jobhub.entity.Company;
import com.jobhub.jobhub.entity.Job;
import com.jobhub.jobhub.entity.User;
import com.jobhub.jobhub.exception.ResourceNotFoundException;
import com.jobhub.jobhub.exception.UnauthorizedAccessException;
import com.jobhub.jobhub.repository.CompanyRepository;
import com.jobhub.jobhub.repository.JobRepository;
import com.jobhub.jobhub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JobService jobService;

    public CompanyService(CompanyRepository companyRepository, JobRepository jobRepository, UserRepository userRepository, JobService jobService) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.jobService = jobService;
    }

    // Create or Update Recruiter's Company Profile
    public CompanyResponseDto createOrUpdateCompany(CompanyRequestDto dto, String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        if (!"RECRUITER".equalsIgnoreCase(recruiter.getRole()) && !"ADMIN".equalsIgnoreCase(recruiter.getRole())) {
            throw new UnauthorizedAccessException("Only recruiters can create company profiles");
        }

        Company company = companyRepository.findByRecruiterId(recruiter.getId())
                .orElse(new Company());

        company.setName(dto.getName());
        company.setLogoUrl(dto.getLogoUrl());
        company.setDescription(dto.getDescription());
        company.setIndustry(dto.getIndustry());
        company.setWebsite(dto.getWebsite());
        company.setLocation(dto.getLocation());
        company.setCompanySize(dto.getCompanySize());
        company.setFoundedYear(dto.getFoundedYear());
        company.setRecruiterId(recruiter.getId());

        Company savedCompany = companyRepository.save(company);
        return mapToResponseDto(savedCompany);
    }

    // Search & Filter Companies with Pagination
    public PageResponse<CompanyResponseDto> searchCompanies(
            String keyword,
            String industry,
            String location,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Company> companyPage = companyRepository.searchCompanies(keyword, industry, location, pageable);

        List<CompanyResponseDto> content = companyPage.getContent()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                companyPage.getNumber(),
                companyPage.getSize(),
                companyPage.getTotalElements(),
                companyPage.getTotalPages(),
                companyPage.isLast()
        );
    }

    // Get Company By ID
    public CompanyResponseDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        return mapToResponseDto(company);
    }

    // Get Jobs for a Specific Company
    public PageResponse<JobResponseDto> getCompanyJobs(Long companyId, int page, int size, String sortBy, String sortDir) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        List<Job> allJobs = jobRepository.findAll();
        List<JobResponseDto> companyJobs = allJobs.stream()
                .filter(j -> isJobBelongingToCompany(j, company))
                .map(jobService::mapToResponseDto)
                .collect(Collectors.toList());

        int totalElements = companyJobs.size();
        int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        int fromIndex = Math.min(page * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<JobResponseDto> pagedContent = companyJobs.subList(fromIndex, toIndex);

        return new PageResponse<>(
                pagedContent,
                page,
                size,
                totalElements,
                totalPages,
                page >= Math.max(0, totalPages - 1)
        );
    }

    // Get Live Company Statistics
    public CompanyStatsDto getCompanyStats() {
        List<Company> allCompanies = companyRepository.findAll();
        List<Job> allJobs = jobRepository.findAll();

        long totalCompanies = allCompanies.size();
        long openPositions = allJobs.size();

        long companiesHiring = allCompanies.stream()
                .filter(comp -> allJobs.stream().anyMatch(job -> isJobBelongingToCompany(job, comp)))
                .count();

        long industries = allCompanies.stream()
                .map(Company::getIndustry)
                .filter(Objects::nonNull)
                .filter(i -> !i.isBlank())
                .distinct()
                .count();

        return new CompanyStatsDto(totalCompanies, companiesHiring, openPositions, industries);
    }

    // Delete Company
    public void deleteCompany(Long id, String recruiterEmail) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));

        User currentUser = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!company.getRecruiterId().equals(currentUser.getId()) && !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new UnauthorizedAccessException("You are not authorized to delete this company");
        }

        companyRepository.delete(company);
    }

    private boolean isJobBelongingToCompany(Job job, Company company) {
        if (job.getCompanyId() != null && job.getCompanyId().equals(company.getId())) {
            return true;
        }
        return job.getCompanyName() != null && job.getCompanyName().equalsIgnoreCase(company.getName());
    }

    // Helper: Map Entity -> Response DTO with dynamic open jobs count
    public CompanyResponseDto mapToResponseDto(Company company) {
        long openJobsCount = jobRepository.findAll().stream()
                .filter(job -> isJobBelongingToCompany(job, company))
                .count();

        return new CompanyResponseDto(
                company.getId(),
                company.getName(),
                company.getLogoUrl(),
                company.getDescription(),
                company.getIndustry(),
                company.getWebsite(),
                company.getLocation(),
                company.getCompanySize(),
                company.getFoundedYear(),
                company.getRecruiterId(),
                openJobsCount,
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }
}
