package com.laya.jobplacement.controller;

import com.laya.jobplacement.repository.StudentRepository;
import com.laya.jobplacement.repository.CompanyRepository;
import com.laya.jobplacement.repository.JobRepository;
import com.laya.jobplacement.repository.JobApplicationRepository;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
public class DashboardController {

    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;

    public DashboardController(
            StudentRepository studentRepository,
            CompanyRepository companyRepository,
            JobRepository jobRepository,
            JobApplicationRepository applicationRepository) {

        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    @GetMapping("/stats")
    public Map<String, Long> getStats() {

        return Map.of(
                "totalStudents", studentRepository.count(),
                "totalCompanies", companyRepository.count(),
                "totalJobs", jobRepository.count(),
                "totalApplications", applicationRepository.count()
        );
    }
}