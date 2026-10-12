package com.laya.jobplacement.service;

import com.laya.jobplacement.entity.JobApplication;
import com.laya.jobplacement.entity.Job;
import com.laya.jobplacement.entity.Student;
import com.laya.jobplacement.repository.JobApplicationRepository;
import com.laya.jobplacement.repository.JobRepository;
import com.laya.jobplacement.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final JobRepository jobRepository;

    public JobApplicationService(
            JobApplicationRepository applicationRepository,
            StudentRepository studentRepository,
            JobRepository jobRepository) {

        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.jobRepository = jobRepository;
    }

    public JobApplication apply(Long studentId, Long jobId) {

        Student student = studentRepository.findById(studentId).orElse(null);
        Job job = jobRepository.findById(jobId).orElse(null);

        if (student == null || job == null) {
            return null;
        }

        JobApplication application = new JobApplication();

        application.setStudent(student);
        application.setJob(job);
        application.setStatus("Applied");

        return applicationRepository.save(application);
    }

    public List<JobApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

    public JobApplication getApplicationById(Long id) {
        return applicationRepository.findById(id).orElse(null);
    }

    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
    public JobApplication updateStatus(Long id, String status) {

    JobApplication application =
            applicationRepository.findById(id).orElse(null);

    if (application == null) {
        return null;
    }

    application.setStatus(status);

    return applicationRepository.save(application);
}
}