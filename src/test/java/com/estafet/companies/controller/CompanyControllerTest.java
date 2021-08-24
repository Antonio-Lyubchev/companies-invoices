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

    @Test
    public void getAllCompanies() throws Exception
    {
        when(service.getAllCompanies()).thenReturn(Arrays.asList(
                new Company("company1", "taxId1", "addr1", "repr1"),
                new Company("company2", "taxId2", "addr2", "repr2"),
                new Company("company3", "taxId3", "addr3", "repr3")
        ));

        mockMvc.perform(get("/companies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("company1", "company2", "company3")))
                .andExpect(jsonPath("$[*].taxId", containsInAnyOrder("taxId1", "taxId2", "taxId3")))
                .andExpect(jsonPath("$[*].address", containsInAnyOrder("addr1", "addr2", "addr3")));

        mockMvc.perform(get("/companies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[ {\n" +
                        "  \"name\" : \"company1\",\n" +
                        "  \"taxId\" : \"taxId1\",\n" +
                        "  \"address\" : \"addr1\",\n" +
                        "  \"representative\" : \"repr1\"\n" +
                        "}, {\n" +
                        "  \"name\" : \"company2\",\n" +
                        "  \"taxId\" : \"taxId2\",\n" +
                        "  \"address\" : \"addr2\",\n" +
                        "  \"representative\" : \"repr2\"\n" +
                        "}, {\n" +
                        "  \"name\" : \"company3\",\n" +
                        "  \"taxId\" : \"taxId3\",\n" +
                        "  \"address\" : \"addr3\",\n" +
                        "  \"representative\" : \"repr3\"\n" +
                        "} ]"));
    }

    @Test
    public void getCompanyByName() throws Exception, InvalidInputException, EntityNotFoundException
    {
        when(service.getCompany("taxId2")).thenReturn(new Company("company2", "taxId2", "addr2", "repr2"));

        mockMvc.perform(get("/companies/taxId2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("company2")))
                .andExpect(jsonPath("$.taxId", is("taxId2")))
                .andExpect(jsonPath("$.address", is("addr2")));
    }

    @Test
    public void addCompany() throws InvalidInputException, Exception
    {
        Company newCompany = new Company("company5", "taxId5", "addr5", "repr5");
        when(service.addCompany(Mockito.any(Company.class))).thenReturn(newCompany.getTaxId());

        mockMvc.perform(put("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\"name\": \"company5\",\n" +
                                "\"taxId\": \"tax5\",\n" +
                                "\"address\": \"addr5\",\n" +
                                "\"representative\": \"repr5\"\n" +
                                "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(newCompany.getTaxId())));
    }

    @Test
    void updateCompany() throws InvalidInputException, Exception
    {
        Company newCompany = new Company("company7", "taxId7", "addr7", "repr7");
        when(service.updateCompany(eq(newCompany.getTaxId()), Mockito.any(Company.class))).thenReturn(newCompany.getTaxId());

        mockMvc.perform(post("/companies/taxId7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\"name\": \"company7\",\n" +
                                "\"taxId\": \"taxId7\",\n" +
                                "\"address\": \"addr7\",\n" +
                                "\"representative\": \"repr7\"\n" +
                                "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(newCompany.getTaxId())));
    }

    @Test
    void deleteCompany() throws ApiException, Exception
    {
        doThrow(new EntityNotFoundException()).when(service).deleteCompany("taxId9999");

        mockMvc.perform(delete("/companies/taxId9999"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
