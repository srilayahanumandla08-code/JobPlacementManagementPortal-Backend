package com.laya.jobplacement.service;

import com.laya.jobplacement.entity.Company;
import com.laya.jobplacement.entity.Job;
import com.laya.jobplacement.entity.JobApplication;
import com.laya.jobplacement.repository.CompanyRepository;
import com.laya.jobplacement.repository.JobApplicationRepository;
import com.laya.jobplacement.repository.JobRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;

    public CompanyService(
            CompanyRepository companyRepository,
            JobRepository jobRepository,
            JobApplicationRepository applicationRepository) {

        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, Company company) {

        Company existingCompany =
                companyRepository.findById(id).orElse(null);

        if (existingCompany == null) {
            return null;
        }

        existingCompany.setName(company.getName());
        existingCompany.setLocation(company.getLocation());
        existingCompany.setIndustry(company.getIndustry());
        existingCompany.setWebsite(company.getWebsite());
        existingCompany.setContactEmail(company.getContactEmail());

        return companyRepository.save(existingCompany);
    }

    public void deleteCompany(Long id) {

        Company company =
                companyRepository.findById(id).orElse(null);

        if (company == null) {
            return;
        }

        // Find all jobs belonging to this company
        List<Job> companyJobs = jobRepository.findAll()
                .stream()
                .filter(job ->
                        job.getCompany() != null &&
                        job.getCompany().getId().equals(id))
                .toList();

        // Delete applications linked to those jobs
        for (Job job : companyJobs) {

            List<JobApplication> applications =
                    applicationRepository.findByJob_Id(job.getId());

            if (!applications.isEmpty()) {
                applicationRepository.deleteAll(applications);
            }
        }

        // Delete the company's jobs
        if (!companyJobs.isEmpty()) {
            jobRepository.deleteAll(companyJobs);
        }

        // Finally delete the company
        companyRepository.delete(company);
    }
}