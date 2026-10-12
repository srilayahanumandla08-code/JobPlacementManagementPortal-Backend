package com.laya.jobplacement.service;

import com.laya.jobplacement.entity.Job;
import com.laya.jobplacement.entity.Student;
import com.laya.jobplacement.repository.JobRepository;
import com.laya.jobplacement.repository.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobMatchingService {

    private final StudentRepository studentRepository;
    private final JobRepository jobRepository;

    public JobMatchingService(
            StudentRepository studentRepository,
            JobRepository jobRepository) {

        this.studentRepository = studentRepository;
        this.jobRepository = jobRepository;
    }

    public List<Map<String, Object>> getRecommendedJobs(Long studentId) {

        Student student = studentRepository
                .findById(studentId)
                .orElse(null);

        if (student == null) {
            return new ArrayList<>();
        }

        List<Job> jobs = jobRepository.findAll();

        List<String> studentSkills =
                normalizeSkills(student.getSkills());

        List<Map<String, Object>> recommendations =
                new ArrayList<>();

        for (Job job : jobs) {

            List<String> jobSkills =
                    extractJobSkills(job);

            if (jobSkills.isEmpty()) {
                continue;
            }

            int matchedSkills = 0;

            List<String> missingSkills =
                    new ArrayList<>();

            for (String skill : jobSkills) {

                if (studentSkills.contains(skill)) {
                    matchedSkills++;
                } else {
                    missingSkills.add(skill);
                }
            }

            int matchPercentage =
                    (matchedSkills * 100) / jobSkills.size();

            Map<String, Object> result =
                    new HashMap<>();

            result.put("jobId", job.getId());
            result.put("jobTitle", job.getJobTitle());
            result.put("location", job.getLocation());
            result.put("salary", job.getSalary());
            result.put("matchPercentage", matchPercentage);
            result.put("missingSkills", missingSkills);

            recommendations.add(result);
        }

        recommendations.sort((a, b) ->
                Integer.compare(
                        (Integer) b.get("matchPercentage"),
                        (Integer) a.get("matchPercentage")
                )
        );

        return recommendations;
    }

    private List<String> normalizeSkills(String skills) {

        if (skills == null || skills.isBlank()) {
            return new ArrayList<>();
        }

        return Arrays.stream(skills.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();
    }

    private List<String> extractJobSkills(Job job) {

        Set<String> skills = new LinkedHashSet<>();

        String text = "";

        if (job.getJobTitle() != null) {
            text += " " + job.getJobTitle();
        }

        if (job.getDescription() != null) {
            text += " " + job.getDescription();
        }

        text = text.toLowerCase();

        // Check longer phrases first
        if (text.contains("spring boot")) {
            skills.add("spring boot");
        }

        if (text.contains("machine learning")) {
            skills.add("machine learning");
        }

        if (text.contains("deep learning")) {
            skills.add("deep learning");
        }

        if (text.contains("data science")) {
            skills.add("data science");
        }

        if (text.contains("javascript")) {
            skills.add("javascript");
        }

        if (text.contains("python")) {
            skills.add("python");
        }

        if (text.contains("java")) {
            skills.add("java");
        }

        if (text.contains("sql")) {
            skills.add("sql");
        }

        if (text.contains("mysql")) {
            skills.add("mysql");
        }

        if (text.contains("react")) {
            skills.add("react");
        }

        if (text.contains("html")) {
            skills.add("html");
        }

        if (text.contains("css")) {
            skills.add("css");
        }

        if (text.contains("c++")) {
            skills.add("c++");
        }

        if (text.contains("mongodb")) {
            skills.add("mongodb");
        }

        if (text.contains("github")) {
            skills.add("github");
        }

        if (text.contains("docker")) {
            skills.add("docker");
        }

        if (text.contains("git")) {
            skills.add("git");
        }

        if (text.contains("ai")) {
            skills.add("ai");
        }

        return new ArrayList<>(skills);
    }
}