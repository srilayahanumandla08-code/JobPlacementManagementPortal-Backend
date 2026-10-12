package com.laya.jobplacement.repository;

import com.laya.jobplacement.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}