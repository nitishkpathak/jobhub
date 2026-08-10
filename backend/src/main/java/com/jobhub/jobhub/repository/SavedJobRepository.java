package com.jobhub.jobhub.repository;

import com.jobhub.jobhub.entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);

    List<SavedJob> findByCandidateId(Long candidateId);

    Optional<SavedJob> findByCandidateIdAndJobId(Long candidateId, Long jobId);

    void deleteByCandidateIdAndJobId(Long candidateId, Long jobId);

    long countByCandidateId(Long candidateId);
}
