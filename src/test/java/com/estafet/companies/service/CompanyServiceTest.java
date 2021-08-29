package com.estafet.companies.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CompanyServiceTest
{
    @Autowired
    private CompanyService companyService;

    @Test
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords()
    {
        //List<Company> companyList = companyService.getAllCompanies();

        //assertEquals(0, companyList.size());
    }
}