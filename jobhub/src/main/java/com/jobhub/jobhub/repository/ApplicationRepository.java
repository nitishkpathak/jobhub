package com.jobhub.jobhub.repository;

import com.jobhub.jobhub.entity.Application;
import com.jobhub.jobhub.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);

    List<Application> findByCandidateId(Long candidateId);

    List<Application> findByJobId(Long jobId);

    List<Application> findByJobIdIn(List<Long> jobIds);

    long countByCandidateId(Long candidateId);

    long countByCandidateIdAndStatus(Long candidateId, ApplicationStatus status);

    long countByJobIdIn(List<Long> jobIds);

    long countByJobIdInAndStatus(List<Long> jobIds, ApplicationStatus status);
}
