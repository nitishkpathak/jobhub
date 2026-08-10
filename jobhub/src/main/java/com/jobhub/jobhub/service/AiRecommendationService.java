package com.jobhub.jobhub.service;

import com.jobhub.jobhub.dto.AiRecommendationResponseDto;
import com.jobhub.jobhub.dto.JobRecommendationDto;
import com.jobhub.jobhub.dto.JobResponseDto;
import com.jobhub.jobhub.entity.Job;
import com.jobhub.jobhub.entity.User;
import com.jobhub.jobhub.exception.ResourceNotFoundException;
import com.jobhub.jobhub.repository.JobRepository;
import com.jobhub.jobhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AiRecommendationService {

    @Value("${ai.api.key:}")
    private String aiApiKey;

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobService jobService;

    public AiRecommendationService(UserRepository userRepository, JobRepository jobRepository, JobService jobService) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    public AiRecommendationResponseDto getRecommendations(String candidateEmail) {
        User candidate = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate account not found"));

        List<Job> allJobs = jobRepository.findAll();

        if (allJobs.isEmpty()) {
            return new AiRecommendationResponseDto(
                    "RULE_BASED_FALLBACK",
                    candidate.getSkills(),
                    0,
                    Collections.emptyList()
            );
        }

        // Try AI Engine if API Key exists, otherwise fallback to Rule-Based algorithm
        if (aiApiKey != null && !aiApiKey.isBlank()) {
            try {
                return callAiRecommendationEngine(candidate, allJobs);
            } catch (Exception e) {
                // Seamless Fallback on API failure
                return generateRuleBasedRecommendations(candidate, allJobs);
            }
        } else {
            return generateRuleBasedRecommendations(candidate, allJobs);
        }
    }

    // Rule-Based Smart Skill Matching Algorithm (Fallback)
    private AiRecommendationResponseDto generateRuleBasedRecommendations(User candidate, List<Job> allJobs) {
        String rawCandidateSkills = candidate.getSkills() != null ? candidate.getSkills() : "";
        Set<String> candidateSkillSet = parseSkills(rawCandidateSkills);

        List<JobRecommendationDto> recommendations = new ArrayList<>();

        for (Job job : allJobs) {
            Set<String> jobSkillSet = parseSkills(job.getSkills());

            List<String> matchingSkills = new ArrayList<>();
            List<String> missingSkills = new ArrayList<>();

            for (String requiredSkill : jobSkillSet) {
                if (containsSkillIgnoreCase(candidateSkillSet, requiredSkill)) {
                    matchingSkills.add(requiredSkill);
                } else {
                    missingSkills.add(requiredSkill);
                }
            }

            int baseScore = 0;
            if (!jobSkillSet.isEmpty()) {
                baseScore = (int) Math.round(((double) matchingSkills.size() / jobSkillSet.size()) * 100);
            }

            // Location Boost (+10% if candidate location matches job location)
            if (candidate.getLocation() != null && job.getLocation() != null &&
                    job.getLocation().toLowerCase().contains(candidate.getLocation().toLowerCase())) {
                baseScore = Math.min(100, baseScore + 10);
            }

            // Reason explanation
            StringBuilder reasonBuilder = new StringBuilder();
            if (!matchingSkills.isEmpty()) {
                reasonBuilder.append("Matches your core skills: ").append(String.join(", ", matchingSkills)).append(".");
            } else {
                reasonBuilder.append("Good potential match based on your background.");
            }

            if (!missingSkills.isEmpty()) {
                reasonBuilder.append(" Skill gap detected in: ").append(String.join(", ", missingSkills)).append(".");
            }

            JobResponseDto jobDto = jobService.mapToResponseDto(job);
            recommendations.add(new JobRecommendationDto(
                    jobDto,
                    baseScore,
                    matchingSkills,
                    missingSkills,
                    missingSkills, // Suggested learning path
                    reasonBuilder.toString()
            ));
        }

        // Sort recommendations by match percentage descending
        recommendations.sort(Comparator.comparingInt(JobRecommendationDto::getMatchPercentage).reversed());

        return new AiRecommendationResponseDto(
                "RULE_BASED_FALLBACK",
                rawCandidateSkills,
                recommendations.size(),
                recommendations
        );
    }

    // Call external LLM / AI API (Google Gemini / OpenAI compatible)
    private AiRecommendationResponseDto callAiRecommendationEngine(User candidate, List<Job> allJobs) {
        // AI API Call placeholder / logic executing via backend HTTP client
        return generateRuleBasedRecommendations(candidate, allJobs);
    }

    private Set<String> parseSkills(String skillsStr) {
        if (skillsStr == null || skillsStr.isBlank()) {
            return Collections.emptySet();
        }
        Set<String> set = new LinkedHashSet<>();
        String[] tokens = skillsStr.split("[,;/]+");
        for (String token : tokens) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty()) {
                set.add(trimmed);
            }
        }
        return set;
    }

    private boolean containsSkillIgnoreCase(Set<String> candidateSkills, String requiredSkill) {
        for (String skill : candidateSkills) {
            if (skill.equalsIgnoreCase(requiredSkill) || skill.toLowerCase().contains(requiredSkill.toLowerCase()) || requiredSkill.toLowerCase().contains(skill.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
