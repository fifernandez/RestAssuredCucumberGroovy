@Todos

Feature: Todos Endpoint
  As a developer
  I want to be sure
  That the Todos endpoint is working as expected


  @tmsLink=06 @severity=critical
  @prod @smoke
  Scenario: Verify status code returned is expected
    Given I do a get to the "todos" endpoint
    Then the returned status code is: "200"


  @tmsLink=07 @severity=critical
  @prod @smoke
  Scenario: Verify response schema is correct
    Given I do a get to the "todos" endpoint
    Then the response schema for the "todos-200" endpoint is correct


  @tmsLink=08 @severity=normal
  @prod @regression
  Scenario: Verify amount of returned items is expected
    Given I do a get to the "todos" endpoint
    Then the returned status code is: "200"
    And the response contains "200" items