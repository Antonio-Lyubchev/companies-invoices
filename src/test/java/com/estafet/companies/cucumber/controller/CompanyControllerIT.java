package com.estafet.companies.cucumber.controller;

import com.estafet.companies.model.Company;
import com.estafet.companies.repository.CompanyRepository;
import com.estafet.companies.utils.JSONParser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class CompanyControllerIT
{
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JSONParser parser;

    private Response mostRecentResponse;
    private List<Company> testCompanyList;
    private Company testCompany;

    @Given("the webservice storage contains company with tax id {long}")
    public void theWebserviceStorageContainsCompanyWithId(long companyTaxNumber)
    {
        assertFalse(companyRepository.findAll().isEmpty(), "No companies present in DB");

        Optional<Company> companyOptional = companyRepository.findById(companyTaxNumber);
        assertFalse(companyOptional.isEmpty(), "Company with id " + companyTaxNumber + " not found in DB");

        this.testCompany = companyOptional.get();
    }

    @When("I request data about the company")
    public void usersGetInformationOnACompany()
    {
        mostRecentResponse = given().when().get("/companies/" + testCompany.getTaxNumber());
    }

    @Then("the server should handle it and return a success status")
    public void theServerShouldHandleItAndReturnASuccessStatus()
    {
        mostRecentResponse.then().statusCode(is(200));
    }

    @And("I should receive the requested data for the company")
    public void theUserReceivesTheRequestedDataForCompany()
    {
        mostRecentResponse.then().body("taxNumber", equalTo(testCompany.getTaxNumber().intValue()))
                .body("name", equalTo(testCompany.getName()))
                .body("address", equalTo(testCompany.getAddress()))
                .body("representative", equalTo(testCompany.getRepresentative()));
    }

    @Given("the webservice storage contains at least {int} companies")
    public void theWebserviceStorageContainsAtLeastCompanies(int numberOfCompanies)
    {
        List<Company> existingCompanies = companyRepository.findAll();
        assertNotNull(existingCompanies, "Failed retrieving companies from storage");
        assertTrue(existingCompanies.size() >= numberOfCompanies, "Not enough companies for test");
        testCompanyList = existingCompanies;
    }

    @When("I request data about all the companies.")
    public void iRequestDataAboutAllTheCompanies()
    {
        mostRecentResponse = get("/companies");
    }

    @And("I should receive data for more than {int} companies")
    public void iShouldReceiveDataForAllTheCompanies(int numberOfCompanies) throws IOException
    {
        List<Company> companies = parser.fromJsonToList(mostRecentResponse.asByteArray(), Company.class);
        assertNotNull(companies, "Failed parsing company list");
        assertEquals(numberOfCompanies, companies.size(), "Number of companies retrieved differs from expected");
        assertEquals(testCompanyList, companies, "Companies retrieved differ from ones inserted");
    }
}
