package com.estafet.companies.cucumber.controller;

import com.estafet.companies.model.Company;
import com.estafet.companies.repository.CompanyRepository;
import com.estafet.companies.utils.JSONParser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

/**
 * For every scenario in the .feature file, the class is reinstantiated (fields don't need to be reset)
 */
public class CompanyScenarios
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

    @When("I request data about all the companies")
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

    @Given("I have company information that I want to store")
    public void iHaveCompanyInformationThatIWantToStore()
    {
        testCompany = new Company(111L, "testName", "testAddress", "testRepresentative");
    }

    @When("I try to store the company in the service")
    public void iTryToStoreTheCompanyInTheService()
    {
        mostRecentResponse = given()
                .contentType(ContentType.JSON)
                .and()
                .body(parser.fromObjectToJsonString(testCompany))
                .put("/companies")
                .andReturn();
    }

    @And("the company should be stored successfully")
    public void theCompanyShouldBeStoredSuccessfully()
    {
        Optional<Company> companyOptional = companyRepository.findById(testCompany.getTaxNumber());
        assertTrue(companyOptional.isPresent(), "Failed to retrieve the company that should've been stored in DB");
        assertEquals(companyOptional.get(), testCompany, "Stored company differs from initially sent");
    }

    @Given("I have a company present in the web service")
    public void iHaveCompanyPresentInTheWebService()
    {
        assertTrue(companyRepository.count() > 0, "Service storage is empty");
        testCompany = companyRepository.findAll().get(0);
    }

    @When("I try update the company's information")
    public void iTryUpdateTheCompanySInformation()
    {
        mostRecentResponse = given()
                .contentType(ContentType.JSON)
                .and()
                .body(parser.fromObjectToJsonString(testCompany))
                .post("/companies/" + testCompany.getTaxNumber())
                .andReturn();
    }

    @When("I try to delete the company from storage")
    public void iTryToDeleteTheCompanyFromStorage()
    {
        mostRecentResponse = delete("/companies/" + testCompany.getTaxNumber());
    }

    @And("the company should be removed from storage successfully")
    public void theCompanyShouldBeRemovedFromStorageSuccessfully()
    {
        assertFalse(companyRepository.existsById(testCompany.getTaxNumber()));
    }
}
