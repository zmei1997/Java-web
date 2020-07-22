#Author Zhongxiao Mei (zmei)
Feature: Blood Sugar Level
	As an Patient
	I want to add Blood Sugar Data
	So that I can keep track of Blood Sugar Data

Scenario Outline: add Blood Sugar Data Entry
Given I login as a patient
When I go the track blood sugar data entry page, I add date: <date>, Fasting blood sugar level: <fastingVal>, Breakfast blood sugar level: <breakfastVal>, Lunch blood sugar level: <lunchVal>, Dinner blood sugar level: <dinnerVal>
Then I click Add Entry, I receive the message shows Your diary blood sugar entry has been added successfully

Examples:
|   date   | fastingVal | breakfastVal | lunchVal | dinnerVal |
|11/19/2019|    100     |      100     |    100   |    100    |
|11/20/2019|    100     |      110     |    111   |    111    |

Scenario Outline: add Blood Sugar Data Entry invalid
Given I login as a patient
When I go the track blood sugar data entry page, I add date: <date>, Fasting blood sugar level: <fastingVal>, Breakfast blood sugar level: <breakfastVal>, Lunch blood sugar level: <lunchVal>, Dinner blood sugar level: <dinnerVal>
Then I click Add Entry, I receive the error message shows could not add blood sugar entry

Examples:
|   date   | fastingVal | breakfastVal | lunchVal | dinnerVal |
|11/05/2019|    -1      |       -1     |    -1    |   -1      |
|11/06/2019|    0       |       0      |    0     |   0       |


Scenario Outline: View Blood Sugar Data Entry by day
Given I login as a patient
When I go the track blood sugar data entry page, I add date: <date>, Fasting blood sugar level: <fastingVal>, Breakfast blood sugar level: <breakfastVal>, Lunch blood sugar level: <lunchVal>, Dinner blood sugar level: <dinnerVal>
Then I click Add Entry, I receive the message shows Your diary blood sugar entry has been added successfully
And I go to view blood sugar level page, type in date: <viewDate>, and select display by: <by>
Then Blood Sugar Data Entry: <date>, <fastingVal>, <breakfastVal>, <lunchVal>, and <dinnerVal> displayed

Examples:
|   date   | fastingVal | breakfastVal | lunchVal | dinnerVal |  viewDate  |    by   |
|10/19/2019|    100     |      100     |    100   |    100    | 10/19/2019 |   day   |
|10/20/2019|    100     |      110     |    111   |    111    | 10/20/2019 |   day   |
|03/04/2019|    100     |      100     |    100   |    100    | 03/03/2019 |   week  |
|03/20/2019|    100     |      110     |    111   |    111    | 03/15/2019 |   week  |
|04/19/2019|    100     |      100     |    100   |    100    | 04/01/2019 |   month |
|05/20/2019|    100     |      110     |    111   |    111    | 05/01/2019 |   month |

Scenario Outline: View Blood Sugar Data Entry, no Blood Sugar Data displayed
Given I login as a patient
When I go the track blood sugar data entry page, I add date: <date>, Fasting blood sugar level: <fastingVal>, Breakfast blood sugar level: <breakfastVal>, Lunch blood sugar level: <lunchVal>, Dinner blood sugar level: <dinnerVal>
Then I click Add Entry, I receive the message shows Your diary blood sugar entry has been added successfully
And I go to view blood sugar level page, type in date: <viewDate>, and select display by: <by>
Then There is no Blood Sugar Data displayed

Examples:
|   date   | fastingVal | breakfastVal | lunchVal | dinnerVal |  viewDate  |    by   |
|11/11/2019|    100     |      100     |    100   |    100    | 01/01/2019 |   day   |
|11/10/2019|    111     |      111     |    110   |    110    | 01/01/2019 |   week  |
|06/11/2019|    110     |      100     |    110   |    110    | 01/01/2019 |   month |

Scenario Outline: HCP view Blood Sugar Data Entry by day
Given I login as a patient
When I go the track blood sugar data entry page, I add date: <date>, Fasting blood sugar level: <fastingVal>, Breakfast blood sugar level: <breakfastVal>, Lunch blood sugar level: <lunchVal>, Dinner blood sugar level: <dinnerVal>
Then I click Add Entry, I receive the message shows Your diary blood sugar entry has been added successfully
And I go to view blood sugar level page, type in date: <viewDate>, and select display by: <by>
Then Blood Sugar Data Entry: <date>, <fastingVal>, <breakfastVal>, <lunchVal>, and <dinnerVal> displayed
And I login as a HCP, go to View Patient Blood Sugar Table page, select BillyBob, type in date: <viewDate>, and select display by: <by>
Then The Patient's Blood Sugar Table is displayed

Examples:
|   date   | fastingVal | breakfastVal | lunchVal | dinnerVal |  viewDate  |    by   |
|11/24/2019|    100     |      100     |    100   |    100    | 11/24/2019 |   day   |
