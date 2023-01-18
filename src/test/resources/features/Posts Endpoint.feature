@Posts

Feature: Posts Endpoint
  As a developer
  I want to be sure
  That the Posts endpoint is working as expected


  @tmsLink=04 @severity=critical @q
    @smoke @regression @prod
  Scenario: Verify status code returned is expected
    Given I do a get to the "posts" endpoint
    Then the returned status code is: "200"
    And the schema for the "posts" endpoint with "200" response code is correct


  @tmsLink=05 @severity=normal
    @smoke @regression @prod
  Scenario: Verify amount of returned items is expected
    Given I do a get to the "posts" endpoint
    Then the returned status code is: "200"
    And the response contains "100" items