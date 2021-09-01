Feature: Testing a REST API

  Scenario: Retrieve company data from the web service
    Given the webservice storage contains companies
    When users want to get information on company with tax id 123
    Then the server should handle it and return a success status
    And the user receives the requested data for company with id 123