package com.estafet.companies;

import com.estafet.companies.controller.CompanyController;
import com.estafet.companies.controller.InvoiceController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest
{
    @Autowired
    private CompanyController companyController;
    @Autowired
    private InvoiceController invoiceController;

    @Test
    public void contextLoads()
    {
        assertThat(companyController).isNotNull();
        assertThat(invoiceController).isNotNull();
    }
}