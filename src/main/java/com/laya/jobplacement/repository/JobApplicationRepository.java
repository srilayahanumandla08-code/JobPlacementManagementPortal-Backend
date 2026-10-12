package com.laya.jobplacement.repository;

import com.laya.jobplacement.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByJob_Id(Long jobId);
}