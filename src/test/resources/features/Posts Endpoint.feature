@Posts

Feature: Posts Endpoint
  As a developer
  I want to be sure
  That the Posts endpoint is working as expected


  @tmsLink=01 @severity=critical
  @prod @smoke
  Scenario: Verify status code returned is expected
    Given I do a get to the "posts" endpoint
    Then the returned status code is: "200"
    And the response schema for the "posts-200" endpoint is correct


  @tmsLink=02 @severity=critical
  @prod @smoke
  Scenario: Verify response schema is correct
    Given I do a get to the "posts" endpoint
    And the response schema for the "posts-200" endpoint is correct


  @tmsLink=03 @severity=normal
  @prod @regression
  Scenario: Verify amount of returned items is expected
    Given I do a get to the "posts" endpoint
    Then the returned status code is: "200"
    And the response contains "100" items


  @tmsLink=04 @severity=minor @issue=AAA-33
  @prod @regression
  Scenario: Example when response code is incorrect
    Given I do a get to the "posts" endpoint
    Then the returned status code is: "201"


  @tmsLink=05 @severity=minor @issue=BBB-33
  @prod @regression
  Scenario: Example when response schema is incorrect
    Given I do a get to the "posts" endpoint
    And the response schema for the "posts-wrong-schema" endpoint is correct