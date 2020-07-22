#Author tadicke3
Feature: Lab procedures
	As an HCP
	I want to add lab procedures to an office visit
	So that I can keep track of what lab procedures a patient needs
	
Scenario: Add lab procedure to office visit
Given I log in to iTrust2 as an HCP
When I create a new Office Visit
And I add a lab procedure to that visit
Then I recieve a message that office visit details were changed successfully

Scenario: View assigned lab procedures
Given I log in to iTrust2 as a Lab Tech
When I navigate to Assigned Procedures
And I change my newest procedures status to In-Progress
Then I recieve a message that lab procedure details were changed successfully

Scenario: Add new lab procedure
Given I log in to iTrust2 as an Admin
When I navigate to Admin Procedures
And I add a new lab procedure
Then I recieve a message that the procedure list was changed successfully

Scenario Outline: admin add procedure codes
Given I login as admin
When I add ICD Codes: <icd>, Description: <description>
And I add LOINC Codes: <loinc>, Common Name: <commonName>, Component: <component>, Property: <property>
Then I recieve a message that codes added successfully

Examples:
	| icd | description | loinc   | commonName | component | property |
	| A1A | a1a         | 12345-1 | A1A        | aaaa      | aaaaa    |
	| A1B | a1b         | 12345-2 | A1B        | bbbb      | bbbbb    |
	| A1C | a1c         | 12345-3 | A1C        | cccc      | ccccc    |

Scenario Outline: update in progress procedure and blood sugar level successfully
Given I login as HCP
When I create another Office Visit
When I add another lab procedure: <procedureCode> to that visit
Then I recieve a message that office visit details were changed successfully
And I logout and login as Lab Tech, username: <labName> , password: <password> , go to Lab Procedures
And I click update status: <status> and type in Blood Sugar Level: <levelOfbs>
Then I recieve a message that Successfully updated procedure
And I logout and login as HCP again
And I go to edit office visit page, select the patient, select Diagnosis Code: <diagnosisCode> and confirm Diabetes Diagnosis
Then I receive the message shows Office visit edited successfully
And I go to set blood sugar limit for a patient, Upper Bound of Fasting: <upperFasting>, Upper Bound After Meal: <upperMeal>
Then I receive the message shows blood sugar Limit set successfully

	Examples:
	| procedureCode | labName    | password | status      | levelOfbs | diagnosisCode | upperFasting | upperMeal |
	|      A1A      | labtech    | 123456   | IN_PROGRESS | 120       | A1A           | 110          | 150       |
	|      A1B      | labtech    | 123456   | IN_PROGRESS | 150       | A1B           | 115          | 130       |
	|      A1C      | labtech    | 123456   | IN_PROGRESS | 300       | A1C           | 90           | 120       |


Scenario Outline: update procedure completed, blood sugar level and comments successfully
Given I login as HCP
When I create another Office Visit
When I add another lab procedure: <procedureCode> to that visit
Then I recieve a message that office visit details were changed successfully
And I logout and login as Lab Tech, username: <labName> , password: <password> , go to Lab Procedures
And I click update status: <status>, type in Blood Sugar Level: <levelOfbs>, and write comments: <comments>
Then I recieve a message that Successfully updated procedure
	
	Examples:
	| procedureCode | labName    | password | status      | levelOfbs | comments |
	|      A1B      | labtech    | 123456   | COMPLETED   | 150       |     Need to eat less sugar     |
	|      A1C      | labtech    | 123456   | COMPLETED   | 300       |     Need further diagnosis     |
