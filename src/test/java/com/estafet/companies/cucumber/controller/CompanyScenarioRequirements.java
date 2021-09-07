package com.estafet.companies.cucumber.controller;

import com.estafet.companies.model.Company;
import com.estafet.companies.repository.CompanyRepository;
import com.estafet.companies.utils.JSONParser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Executed before every Scenario in the .feature file
 */
public class CompanyScenarioRequirements
{
    @Autowired
    protected CompanyRepository companyRepository;

    @Autowired
    protected JSONParser parser;

    @Value("${springdoc.api-docs.path}")
    private String apiDocsUrl;

    private List<Company> testCompanyList;
    private Company companyToExtract;
    private Company extractedCompany;

    @Given("the webservice is running")
    public void theWebserviceIsRunning()
    {
        get("/" + apiDocsUrl).then().statusCode(is(not(404)));
    }

    /**
     * The company list is refreshed before every scenario
     *
     * @throws IOException if the 'AddCompanies.json' file is missing
     */
    @And("the storage contains companies")
    public void theStorageContainsCompanies() throws IOException
    {
        Resource companiesJsonFileResource = new ClassPathResource("JSON/AddCompanies.json");
        byte[] companiesByteArray = companiesJsonFileResource.getInputStream().readAllBytes();
        testCompanyList = parser.fromJsonToList(companiesByteArray, Company.class);

        companyRepository.deleteAll();
        companyRepository.saveAll(testCompanyList);

        assertEquals(testCompanyList.size(), companyRepository.count(), "Failed populating companies in DB");
    }

    @When("I request information about a company")
    public void iRequestInformationAboutACompany()
    {
        companyToExtract = testCompanyList.get(0);
        extractedCompany = companyRepository.findById(companyToExtract.getTaxNumber()).orElse(null);
    }

    @Then("I expect the information to be present in the storage")
    public void iShouldReceiveTheInformationFromTheService()
    {
        assertNotNull(extractedCompany, "Failed retrieving company from storage");
        assertEquals(companyToExtract, extractedCompany, "Failed confirming existence of specified company");
    }
}
