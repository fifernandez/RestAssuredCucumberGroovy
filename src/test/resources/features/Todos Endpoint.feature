@Todos

Feature: Todos Endpoint
  As a developer
  I want to be sure
  That the Todos endpoint is working as expected


  @tmsLink=06 @severity=critical
    @smoke @regression @prod
  Scenario: Verify status code returned is expected
    Given I do a get to the "todos" endpoint
    Then the returned status code is: "200"
    And the schema for the "todos" endpoint with "200" response code is correct


  @tmsLink=07 @severity=normal
    @smoke @regression @prod
  Scenario: Verify amount of returned items is expected
    Given I do a get to the "todos" endpoint
    Then the returned status code is: "200"
    And the response contains "200" items