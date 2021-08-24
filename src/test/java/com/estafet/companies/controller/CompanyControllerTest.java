package com.estafet.companies.controller;

import com.estafet.companies.exception.ApiException;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;
import com.estafet.companies.service.CompanyService;
import com.estafet.companies.utils.JSONParser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyController.class)
public class CompanyControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService service;

    private final List<Company> testCompanyList = Arrays.asList(
            new Company("company1", "taxId1", "addr1", "repr1"),
            new Company("company2", "taxId2", "addr2", "repr2"),
            new Company("company3", "taxId3", "addr3", "repr3")
    );

    private final Company testNewCompany = new Company("company4", "taxId4", "addr4", "repr4");

    @Test
    public void getAllCompanies() throws Exception
    {
        when(service.getAllCompanies()).thenReturn(testCompanyList);

        mockMvc.perform(get("/companies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(testCompanyList.stream().map(Company::getName).toArray())))
                .andExpect(jsonPath("$[*].taxId", containsInAnyOrder(testCompanyList.stream().map(Company::getTaxId).toArray())))
                .andExpect(jsonPath("$[*].address", containsInAnyOrder(testCompanyList.stream().map(Company::getAddress).toArray())));

        mockMvc.perform(get("/companies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(JSONParser.fromObjectListToJsonString(testCompanyList)));
    }

    @Test
    public void getCompanyByName() throws Exception, InvalidInputException, EntityNotFoundException
    {
        Company companyForTest = testCompanyList.get(1);
        when(service.getCompany(companyForTest.getTaxId())).thenReturn(companyForTest);

        mockMvc.perform(get("/companies/" + companyForTest.getTaxId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(companyForTest.getName())))
                .andExpect(jsonPath("$.taxId", is(companyForTest.getTaxId())))
                .andExpect(jsonPath("$.address", is(companyForTest.getAddress())));
    }

    @Test
    public void addCompany() throws InvalidInputException, Exception
    {
        when(service.addCompany(Mockito.any(Company.class))).thenReturn(testNewCompany.getTaxId());

        mockMvc.perform(put("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONParser.fromObjectToJsonString(testNewCompany)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testNewCompany.getTaxId())));
    }

    @Test
    void updateCompany() throws InvalidInputException, Exception
    {
        when(service.updateCompany(eq(testNewCompany.getTaxId()), Mockito.any(Company.class))).thenReturn(testNewCompany.getTaxId());

        mockMvc.perform(post("/companies/" + testNewCompany.getTaxId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONParser.fromObjectToJsonString(testNewCompany)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testNewCompany.getTaxId())));
    }

    @Test
    void deleteCompany() throws ApiException, Exception
    {
        final String fakeTaxId = "taxId9999";
        doThrow(new EntityNotFoundException()).when(service).deleteCompany(fakeTaxId);

        mockMvc.perform(delete("/companies/" + fakeTaxId))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
