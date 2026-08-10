package com.jobhub.jobhub.specification;

import com.jobhub.jobhub.entity.Job;
import com.jobhub.jobhub.entity.JobType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobSpecification {

    public static Specification<Job> filterJobs(
            String keyword,
            String location,
            JobType jobType,
            String experienceLevel,
            Double minimumSalary,
            String skills
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search keyword in title, companyName, or description
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.trim().toLowerCase() + "%";
                Predicate titleMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchPattern);
                Predicate companyMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("companyName")), searchPattern);
                Predicate descMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern);

                predicates.add(criteriaBuilder.or(titleMatch, companyMatch, descMatch));
            }

            // Location filter
            if (location != null && !location.trim().isEmpty()) {
                String locationPattern = "%" + location.trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), locationPattern));
            }

            // Job Type filter
            if (jobType != null) {
                predicates.add(criteriaBuilder.equal(root.get("jobType"), jobType));
            }

            // Experience Level filter
            if (experienceLevel != null && !experienceLevel.trim().isEmpty()) {
                String expPattern = "%" + experienceLevel.trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("experienceLevel")), expPattern));
            }

            // Salary filter (minimum salary match)
            if (minimumSalary != null && minimumSalary > 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("salaryMax"), minimumSalary));
            }

            // Skills filter
            if (skills != null && !skills.trim().isEmpty()) {
                String skillsPattern = "%" + skills.trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("skills")), skillsPattern));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
