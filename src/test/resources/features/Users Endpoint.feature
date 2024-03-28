@Users

Feature: Users Endpoint
  As a developer
  I want to be sure
  That the Users Endpoint is working as expected


  @tmsLink=09 @severity=critical
  @prod @smoke
  Scenario: Verify status code returned is expected
    Given I do a get to the "users" endpoint
    Then the returned status code is: "200"


  @tmsLink=10 @severity=critical
  @prod @smoke
  Scenario: Verify response schema is correct
    Given I do a get to the "users" endpoint
    Then the response schema for the "users-200" endpoint is correct


  @tmsLink=11 @severity=normal
  @prod @regression
  Scenario: Verify amount of returned items is expected
    Given I do a get to the "users" endpoint
    Then the returned status code is: "200"
    And the response contains "10" items


  @tmsLink=12 @severity=normal @issue=CAL-33
    @prod @regression
  Scenario Outline: Verify specific user is returned in the users response
    Given I do a get to the "users" endpoint
    Then the returned status code is: "200"
    And I see the "users" response contains the user: "<user>"

    Examples:
      | user   |
      | Kamren |
      | Bret   |


  @tmsLink=13 @severity=normal @issue=CAL-33
    @prod @regression
  Scenario Outline: Verify there is a user with a given zipcode
    Given I do a get to the "users" endpoint
    Then the returned status code is: "200"
    And in the "users" response there is a user with the zipcode: "<zipcode>"

    Examples:
      | zipcode    |
      | 76495-3109 |
      | 92998-3874 |