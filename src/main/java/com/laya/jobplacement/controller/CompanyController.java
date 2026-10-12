package com.laya.jobplacement.controller;

import com.laya.jobplacement.entity.Company;
import com.laya.jobplacement.service.CompanyService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "http://localhost:5173")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // GET ALL COMPANIES
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(
                companyService.getAllCompanies()
        );
    }

    // GET COMPANY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompany(@PathVariable Long id) {

        Company company = companyService.getCompanyById(id);

        if (company == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(company);
    }

    // CREATE COMPANY
    @PostMapping
    public ResponseEntity<?> createCompany(
            @RequestBody Company company,
            Authentication authentication) {

        if (!hasRole(authentication, "ROLE_RECRUITER")
                && !hasRole(authentication, "ROLE_ADMIN")) {

            return ResponseEntity
                    .status(403)
                    .body("Only recruiters and admins can create companies");
        }

        return ResponseEntity.ok(
                companyService.createCompany(company)
        );
    }

    // UPDATE COMPANY
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(
            @PathVariable Long id,
            @RequestBody Company company,
            Authentication authentication) {

        if (!hasRole(authentication, "ROLE_RECRUITER")
                && !hasRole(authentication, "ROLE_ADMIN")) {

            return ResponseEntity
                    .status(403)
                    .body("Only recruiters and admins can update companies");
        }

        Company updatedCompany =
                companyService.updateCompany(id, company);

        if (updatedCompany == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedCompany);
    }

    // DELETE COMPANY
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(
            @PathVariable Long id,
            Authentication authentication) {

        if (!hasRole(authentication, "ROLE_RECRUITER")
                && !hasRole(authentication, "ROLE_ADMIN")) {

            return ResponseEntity
                    .status(403)
                    .body("Only recruiters and admins can delete companies");
        }

        Company company =
                companyService.getCompanyById(id);

        if (company == null) {
            return ResponseEntity.notFound().build();
        }

        companyService.deleteCompany(id);

        return ResponseEntity.ok(
                "Company deleted successfully"
        );
    }

    private boolean hasRole(
            Authentication authentication,
            String role) {

        return authentication != null
                && authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals(role)
                );
    }
}