package com.jobhub.jobhub.dto;

import java.time.LocalDateTime;

public class CompanyResponseDto {

    private Long id;
    private String name;
    private String logoUrl;
    private String description;
    private String industry;
    private String website;
    private String location;
    private String companySize;
    private Integer foundedYear;
    private Long recruiterId;
    private long openJobsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CompanyResponseDto() {
    }

    public CompanyResponseDto(Long id, String name, String logoUrl, String description, String industry, String website, String location, String companySize, Integer foundedYear, Long recruiterId, long openJobsCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
        this.industry = industry;
        this.website = website;
        this.location = location;
        this.companySize = companySize;
        this.foundedYear = foundedYear;
        this.recruiterId = recruiterId;
        this.openJobsCount = openJobsCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public Integer getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(Integer foundedYear) {
        this.foundedYear = foundedYear;
    }

    public Long getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
    }

    public long getOpenJobsCount() {
        return openJobsCount;
    }

    public void setOpenJobsCount(long openJobsCount) {
        this.openJobsCount = openJobsCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
