package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Class for Cucumber Testing of Blood Sugar Data feature.
 *
 * @author Zhongxiao Mei
 */
public class BloodSugarLevelStepDefs extends CucumberTest {

    private final String baseUrl = "http://localhost:8080/iTrust2";

    private void setTextField2 ( final By byval, final Object value ) {
        final WebElement elem = driver.findElement( byval );
        elem.clear();
        elem.sendKeys( value.toString() );
    }

    /**
     * Fills in the date field with the specified date.
     *
     * @param date
     *            The date to enter.
     */
    private void fillInDate2 ( final String dateField, final String date ) {
        driver.findElement( By.name( dateField ) ).clear();
        final WebElement dateElement = driver.findElement( By.name( dateField ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
    }

    /**
     * login as a patient
     */
    @Given ( "I login as a patient" )
    public void patientLogin () {
        attemptLogout();
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "patient" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
        waitForAngular();
    }

    /**
     * I go the track blood sugar data entry page, I add date, Fasting blood
     * sugar level, Breakfast blood sugar level, Lunch blood sugar level, Dinner
     * blood sugar level
     *
     * @param date
     *            date
     * @param fastingVal
     *            fastingVal
     * @param breakfastVal
     *            breakfastVal
     * @param lunchVal
     *            lunchVal
     * @param dinnerVal
     *            dinnerVal
     */
    @When ( "I go the track blood sugar data entry page, I add date: (.+), Fasting blood sugar level: (.+), Breakfast blood sugar level: (.+), Lunch blood sugar level: (.+), Dinner blood sugar level: (.+)" )
    public void addBloodSugarData ( final String date, final String fastingVal, final String breakfastVal,
            final String lunchVal, final String dinnerVal ) {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('addBloodSugarLevel').click();" );
        waitForAngular();
        fillInDate2( "date", date );

        waitForAngular();
        setTextField2( By.name( "fasting" ), fastingVal );

        waitForAngular();
        setTextField2( By.name( "breakfast" ), breakfastVal );

        waitForAngular();
        setTextField2( By.name( "lunch" ), lunchVal );

        waitForAngular();
        setTextField2( By.name( "dinner" ), dinnerVal );

        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();

    }

    /**
     * I go to view blood sugar level page, type in date, and select display by
     *
     * @param viewDate
     *            viewDate
     * @param by
     *            by
     */
    @And ( "I go to view blood sugar level page, type in date: (.+), and select display by: (.+)" )
    public void viewBloodSugarLevel ( final String viewDate, final String by ) {

        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('viewBloodSugarLevel').click();" );

        waitForAngular();
        fillInDate2( "date", viewDate );

        waitForAngular();
        final Select displayBy = new Select( driver.findElement( By.name( "dateType" ) ) );
        displayBy.selectByVisibleText( by );
        waitForAngular();
    }

    /**
     * I login as a HCP, go to View Patient Blood Sugar Table page, select
     * BillyBob, type in date, and select display by
     *
     * @param viewDate
     *            viewDate
     * @param by
     *            by
     */
    @And ( "I login as a HCP, go to View Patient Blood Sugar Table page, select BillyBob, type in date: (.+), and select display by: (.+)" )
    public void hcpViewPatientBSL ( final String viewDate, final String by ) {
        // login as HCP
        attemptLogout();
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "hcp" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
        waitForAngular();

        // go to View Patient Blood Sugar Table page
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('veiwBSLtable').click();" );

        waitForAngular();
        driver.findElement( By.cssSelector( "input[type=\"radio\"]" ) ).click();

        waitForAngular();
        fillInDate2( "date", viewDate );

        waitForAngular();
        final Select displayBy = new Select( driver.findElement( By.name( "dateType" ) ) );
        displayBy.selectByVisibleText( by );
        waitForAngular();

    }

    /**
     * I click Add Entry, I receive the message shows Your diary blood sugar
     * entry has been added successfully
     */
    @Then ( "I click Add Entry, I receive the message shows Your diary blood sugar entry has been added successfully" )
    public void addBloodSugarDataSuccess () {
        waitForAngular();
        try {
            assertTrue( driver.findElement( By.name( "success" ) ).getText()
                    .contains( "Your diary blood sugar entry has been added successfully." ) );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Blood Sugar Data Entry displayed
     *
     * @param date
     *            date
     * @param fastingVal
     *            fastingVal
     * @param breakfastVal
     *            breakfastVal
     * @param lunchVal
     *            lunchVal
     * @param dinnerVal
     *            dinnerVal
     */
    @Then ( "Blood Sugar Data Entry: (.+), (.+), (.+), (.+), and (.+) displayed" )
    public void bslDisplay ( final String date, final String fastingVal, final String breakfastVal,
            final String lunchVal, final String dinnerVal ) {
        try {
            assertNotNull( driver.findElement( By.id( "viewDate" ) ).getText() );
            assertNotNull( driver.findElement( By.id( "FBS" ) ).getText() );
            assertNotNull( driver.findElement( By.id( "Breakfast" ) ).getText() );
            assertNotNull( driver.findElement( By.id( "Lunch" ) ).getText() );
            assertNotNull( driver.findElement( By.id( "Dinner" ) ).getText() );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * The Patient's Blood Sugar Table is displayed
     */
    @Then ( "The Patient's Blood Sugar Table is displayed" )
    public void hcpViewBslDisplay () {
        try {
            driver.findElement( By.id( "date" ) ).getText().contains( "11/24/2019" );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * I click Add Entry, I receive the error message shows could not add blood
     * sugar entry
     */
    @Then ( "I click Add Entry, I receive the error message shows could not add blood sugar entry" )
    public void canNotAdd () {
        waitForAngular();
        try {
            assertTrue( driver.findElement( By.name( "errorMsg" ) ).getText()
                    .contains( "fasting blood sugar level must be 0 to 500 \n"
                            + "breakfast blood sugar level must be 0 to 500 \n"
                            + "lunch blood sugar level must be 0 to 500 \n"
                            + "dinner blood sugar level must be 0 to 500 \n" + "Could not add blood sugar entry." ) );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * There is no Blood Sugar Data displayed
     */
    @Then ( "There is no Blood Sugar Data displayed" )
    public void bslNoDisplay () {
        // no data
    }

}
