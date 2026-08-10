package com.jobhub.jobhub.repository;

import com.jobhub.jobhub.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByRecruiterId(Long recruiterId);

    List<Company> findByIndustry(String industry);

    @Query("SELECT c FROM Company c WHERE " +
           "(:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:industry IS NULL OR LOWER(c.industry) LIKE LOWER(CONCAT('%', :industry, '%'))) AND " +
           "(:location IS NULL OR LOWER(c.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    Page<Company> searchCompanies(
            @Param("keyword") String keyword,
            @Param("industry") String industry,
            @Param("location") String location,
            Pageable pageable
    );
}
