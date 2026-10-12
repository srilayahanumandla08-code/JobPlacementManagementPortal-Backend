package com.laya.jobplacement.controller;

import com.laya.jobplacement.service.JobMatchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matching")
@CrossOrigin
public class JobMatchingController {

    private final JobMatchingService matchingService;

    public JobMatchingController(JobMatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getRecommendedJobs(
            @PathVariable Long studentId) {

        List<Map<String, Object>> recommendations =
                matchingService.getRecommendedJobs(studentId);

        return ResponseEntity.ok(recommendations);
    }
}