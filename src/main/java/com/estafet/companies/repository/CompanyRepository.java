package com.estafet.companies.repository;

import com.estafet.companies.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>
{
    @Query("select c from Company c " +
            "where lower(c.taxNumber) like lower(concat('%', :taxNumber, '%'))")
    List<Company> searchByTaxNumber(@Param("taxNumber") String taxNumber);

    @Query("select (count(c) > 0) from Company c where upper(c.taxNumber) = upper(?1)")
    boolean existsByTaxNumber(@NonNull String taxNumber);

    /*
    @Query("select case when count(c)> 0 then true else false end from Company c where lower(c.tax_number) like lower(:taxNumber)")
    boolean existsByTaxNumber(@Param("taxNumber") String taxNumber);*/
}