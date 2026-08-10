package com.jobhub.jobhub.dto;

public class CompanyStatsDto {

    private long totalCompanies;
    private long companiesHiring;
    private long openPositions;
    private long industries;

    public CompanyStatsDto() {
    }

    public CompanyStatsDto(long totalCompanies, long companiesHiring, long openPositions, long industries) {
        this.totalCompanies = totalCompanies;
        this.companiesHiring = companiesHiring;
        this.openPositions = openPositions;
        this.industries = industries;
    }

    public long getTotalCompanies() {
        return totalCompanies;
    }

    public void setTotalCompanies(long totalCompanies) {
        this.totalCompanies = totalCompanies;
    }

    public long getCompaniesHiring() {
        return companiesHiring;
    }

    public void setCompaniesHiring(long companiesHiring) {
        this.companiesHiring = companiesHiring;
    }

    public long getOpenPositions() {
        return openPositions;
    }

    public void setOpenPositions(long openPositions) {
        this.openPositions = openPositions;
    }

    public long getIndustries() {
        return industries;
    }

    public void setIndustries(long industries) {
        this.industries = industries;
    }
}
