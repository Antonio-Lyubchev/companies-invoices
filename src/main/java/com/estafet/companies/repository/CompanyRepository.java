package com.estafet.companies.repository;

import com.estafet.companies.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>
{
    List<Company> findByName(String name);

    Company findByTaxNumber(long taxId);
}