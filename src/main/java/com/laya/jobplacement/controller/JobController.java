package com.laya.jobplacement.controller;

import com.laya.jobplacement.entity.Job;
import com.laya.jobplacement.service.JobService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // STUDENT, RECRUITER, ADMIN
    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    // STUDENT, RECRUITER, ADMIN
    @GetMapping("/{id}")
    public ResponseEntity<?> getJob(@PathVariable Long id) {

        Job job = jobService.getJobById(id);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(job);
    }

    // RECRUITER, ADMIN
    @PostMapping
    public ResponseEntity<?> createJob(
            @RequestBody Job job,
            Authentication authentication) {

        if (!hasRole(authentication, "ROLE_RECRUITER")
                && !hasRole(authentication, "ROLE_ADMIN")) {

            return ResponseEntity.status(403)
                    .body("Only recruiters and admins can create jobs");
        }

        return ResponseEntity.ok(jobService.createJob(job));
    }

    // RECRUITER, ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(
            @PathVariable Long id,
            @RequestBody Job job,
            Authentication authentication) {

        if (!hasRole(authentication, "ROLE_RECRUITER")
                && !hasRole(authentication, "ROLE_ADMIN")) {

            return ResponseEntity.status(403)
                    .body("Only recruiters and admins can update jobs");
        }

        Job updatedJob = jobService.updateJob(id, job);

        if (updatedJob == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedJob);
    }

    // RECRUITER, ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(
            @PathVariable Long id,
            Authentication authentication) {

        if (!hasRole(authentication, "ROLE_RECRUITER")
                && !hasRole(authentication, "ROLE_ADMIN")) {

            return ResponseEntity.status(403)
                    .body("Only recruiters and admins can delete jobs");
        }

        Job job = jobService.getJobById(id);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        jobService.deleteJob(id);

        return ResponseEntity.ok("Job deleted successfully");
    }

    private boolean hasRole(Authentication authentication, String role) {

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals(role));
    }
}