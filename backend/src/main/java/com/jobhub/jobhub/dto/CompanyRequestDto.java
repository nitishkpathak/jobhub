package com.jobhub.jobhub.dto;

import jakarta.validation.constraints.NotBlank;

public class CompanyRequestDto {

    @NotBlank(message = "Company name is required")
    private String name;

    private String logoUrl;
    private String description;
    private String industry;
    private String website;
    private String location;
    private String companySize;
    private Integer foundedYear;

    public CompanyRequestDto() {
    }

    public CompanyRequestDto(String name, String logoUrl, String description, String industry, String website, String location, String companySize, Integer foundedYear) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
        this.industry = industry;
        this.website = website;
        this.location = location;
        this.companySize = companySize;
        this.foundedYear = foundedYear;
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
}
