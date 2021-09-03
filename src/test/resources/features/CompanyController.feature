Feature: Store and retrieve company data from service
  In order to store and access company data
  As an end user
  I want to interact with a web service to store and retrieve the mentioned data

  Background: Webservice is running and has data
    Given the webservice is running
    And the storage contains companies
    When I request information about a company
    Then I expect the information to be present in the storage

  Scenario: Retrieve company data from the web service
    Given the webservice storage contains company with tax id 123
    When I request data about the company
    Then the server should handle it and return a success status
    And I should receive the requested data for the company

  Scenario: Retrieve company data for all companies from the web service
    Given the webservice storage contains at least 3 companies
    When I request data about all the companies
    Then the server should handle it and return a success status
    And I should receive data for more than 3 companies

  Scenario: Publish company data to the service
    Given I have company information that I want to store
    When I try to store the company in the service
    Then the server should handle it and return a success status
    And the company should be stored successfully

  Scenario: Update an existing company in storage
    Given I have a company present in the web service
    When I try update the company's information
    Then the server should handle it and return a success status
    And the company should be stored successfully

  Scenario: Delete an existing company from storage
    Given I have a company present in the web service
    When I try to delete the company from storage
    Then the server should handle it and return a success status
    And the company should be removed from storage successfully