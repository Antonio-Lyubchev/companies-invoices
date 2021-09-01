package com.estafet.companies.cucumber.controller;

import com.estafet.companies.model.Company;
import com.estafet.companies.repository.CompanyRepository;
import com.estafet.companies.utils.JSONParser;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GetCompanyByIdIT
{
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JSONParser parser;

    private Response response;
    private List<Company> testCompanyList;

    @Before
    public void populateDb() throws IOException
    {
        Resource companiesJsonFileResource = new ClassPathResource("JSON/AddCompanies.json");
        byte[] companiesByteArray = companiesJsonFileResource.getInputStream().readAllBytes();
        testCompanyList = parser.fromJsonToList(companiesByteArray, Company.class);

        companyRepository.saveAll(testCompanyList);
    }

    @Given("the webservice storage contains companies")
    public void theWebserviceStorageContainsCompanies()
    {
        assertFalse(companyRepository.findAll().isEmpty(), "No companies present in DB");
    }

    @When("users want to get information on company with tax id {long}")
    public void usersGetInformationOnACompany(long companyTaxNumber)
    {
        response = given().log().all().when().get("/companies/" + companyTaxNumber);
    }

    @Then("the server should handle it and return a success status")
    public void theServerShouldHandleItAndReturnASuccessStatus()
    {
        response.then().statusCode(is(200));
    }

    @And("the user receives the requested data for company with id {long}")
    public void theUserReceivesTheRequestedDataForCompanyWithId(long companyTaxNumber)
    {
        Company company = testCompanyList
                .stream().
                filter(c -> c.getTaxNumber() == companyTaxNumber)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed retrieving company from list. Is it populated?"));

        response.then().body("taxNumber", equalTo(company.getTaxNumber().intValue()))
                .body("name", equalTo(company.getName()))
                .body("address", equalTo(company.getAddress()))
                .body("representative", equalTo(company.getRepresentative()));
    }
}
