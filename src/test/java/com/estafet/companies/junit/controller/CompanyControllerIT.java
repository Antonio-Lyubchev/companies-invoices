package com.estafet.companies.junit.controller;

import com.estafet.companies.controller.CompanyController;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.model.Company;
import com.estafet.companies.service.CompanyService;
import com.estafet.companies.utils.JSONParser;
import com.estafet.companies.utils.ModelMapperUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(ModelMapperUtils.class)
@WebMvcTest(CompanyController.class)
public class CompanyControllerIT
{
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JSONParser parser;

    @MockBean
    private CompanyService service;

    private List<Company> testCompanyList;
    private Company testNewCompany;

    @BeforeAll
    public void setup() throws IOException
    {
        Resource companiesJsonFileResource = new ClassPathResource("JSON/AddCompanies.json");
        byte[] companiesByteArray = companiesJsonFileResource.getInputStream().readAllBytes();
        testCompanyList = parser.fromJsonToList(companiesByteArray, Company.class);

        testNewCompany = new Company(4145234L, "company4", "addr4", "repr4");
    }

    @Test
    public void getAllCompanies() throws Exception
    {
        when(service.getAllCompanies()).thenReturn(testCompanyList);

        mockMvc.perform(get("/companies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(testCompanyList.stream().map(Company::getName).toArray())))
                .andExpect(jsonPath("$[*].taxNumber", containsInAnyOrder(testCompanyList.stream().map(Company::getTaxNumber).map(Long::intValue).toArray())))
                .andExpect(jsonPath("$[*].address", containsInAnyOrder(testCompanyList.stream().map(Company::getAddress).toArray())));

        mockMvc.perform(get("/companies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(parser.fromObjectListToJsonString(testCompanyList)));
    }

    @Test
    public void getCompanyByName() throws Exception
    {
        Company companyForTest = testCompanyList.get(1);
        when(service.getCompany(companyForTest.getTaxNumber())).thenReturn(companyForTest);

        mockMvc.perform(get("/companies/" + companyForTest.getTaxNumber()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(companyForTest.getName())))
                .andExpect(jsonPath("$.taxNumber", is(companyForTest.getTaxNumber().intValue())))
                .andExpect(jsonPath("$.address", is(companyForTest.getAddress())));
    }

    @Test
    public void addCompany() throws Exception
    {
        when(service.addCompany(Mockito.any(Company.class))).thenReturn(testNewCompany.getTaxNumber());
        when(service.getCompany(testNewCompany.getTaxNumber())).thenReturn(testNewCompany);

        mockMvc.perform(put("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parser.fromObjectToJsonString(testNewCompany)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(Long.toString(testNewCompany.getTaxNumber()))));
    }

    @Test
    void updateCompany() throws Exception
    {
        doNothing().when(service).updateCompany(eq(testNewCompany.getTaxNumber()), Mockito.any(Company.class));

        mockMvc.perform(post("/companies/" + testNewCompany.getTaxNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parser.fromObjectToJsonString(testNewCompany)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteCompany() throws Exception
    {
        final long fakeTaxId = 9999;
        doThrow(new EntityNotFoundException()).when(service).deleteCompany(fakeTaxId);

        mockMvc.perform(delete("/companies/" + fakeTaxId))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
