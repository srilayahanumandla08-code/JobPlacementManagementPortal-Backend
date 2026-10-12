package com.laya.jobplacement.controller;

import com.laya.jobplacement.entity.JobApplication;
import com.laya.jobplacement.service.JobApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin
public class JobApplicationController {

    private final JobApplicationService applicationService;

    public JobApplicationController(JobApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<?> apply(@RequestBody Map<String, Long> request) {

        JobApplication application = applicationService.apply(
                request.get("studentId"),
                request.get("jobId")
        );

        if (application == null) {
            return ResponseEntity.badRequest()
                    .body("Invalid student or job ID");
        }

        return ResponseEntity.ok(application);
    }

    @GetMapping
    public List<JobApplication> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplication(@PathVariable Long id) {

        JobApplication application =
                applicationService.getApplicationById(id);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(application);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {

        JobApplication application =
                applicationService.getApplicationById(id);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        applicationService.deleteApplication(id);

        return ResponseEntity.ok("Application deleted successfully");
    }
    @PutMapping("/{id}/status")
public ResponseEntity<?> updateStatus(
        @PathVariable Long id,
        @RequestBody Map<String, String> request) {

    JobApplication application =
            applicationService.updateStatus(id, request.get("status"));

    if (application == null) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(application);
}
}