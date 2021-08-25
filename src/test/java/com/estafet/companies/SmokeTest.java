package com.estafet.companies;

import com.estafet.companies.configuration.ModelMapperConfiguration;
import com.estafet.companies.controller.CompanyController;
import com.estafet.companies.controller.InvoiceController;
import com.estafet.companies.utils.JSONParser;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(ModelMapperConfiguration.class)
public class SmokeTest
{
    @Autowired
    private CompanyController companyController;
    @Autowired
    private InvoiceController invoiceController;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JSONParser jsonParser;

    @Test
    public void contextLoads()
    {
        assertThat(companyController).isNotNull();
        assertThat(invoiceController).isNotNull();
        assertThat(modelMapper).isNotNull();
        assertThat(jsonParser).isNotNull();
    }
}