Feature: Tests on JustJoinIt website

  Scenario Outline: JustJoin It test
    Given Go to JustJoinIT website
    Then Select seniority
    And Select offers with sallary and sort by lowest
    And I search for offers in "<city>" city
    And I search for offers in "<category>" category
    Then I print out general information about offers in city "<city>"
    Examples:
      | city     | category |
      | Wrocław  | Testing  |
      | Warszawa | HTML     |
      | Kraków   | PHP      |