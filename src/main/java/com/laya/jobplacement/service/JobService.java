package com.laya.jobplacement.service;

import com.laya.jobplacement.entity.Job;
import com.laya.jobplacement.entity.JobApplication;
import com.laya.jobplacement.repository.JobApplicationRepository;
import com.laya.jobplacement.repository.JobRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;

    public JobService(
            JobRepository jobRepository,
            JobApplicationRepository applicationRepository) {

        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public Job updateJob(Long id, Job job) {

        Job existingJob =
                jobRepository.findById(id).orElse(null);

        if (existingJob == null) {
            return null;
        }

        existingJob.setJobTitle(job.getJobTitle());
        existingJob.setDescription(job.getDescription());
        existingJob.setLocation(job.getLocation());
        existingJob.setSalary(job.getSalary());
        existingJob.setEligibility(job.getEligibility());
        existingJob.setLastDate(job.getLastDate());
        existingJob.setCompany(job.getCompany());

        return jobRepository.save(existingJob);
    }

    public void deleteJob(Long id) {

        Job existingJob =
                jobRepository.findById(id).orElse(null);

        if (existingJob == null) {
            return;
        }

        // Delete applications linked to this job first
        List<JobApplication> applications =
                applicationRepository.findByJob_Id(id);

        if (!applications.isEmpty()) {
            applicationRepository.deleteAll(applications);
        }

        // Now delete the job
        jobRepository.delete(existingJob);
    }
}