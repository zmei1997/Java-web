package edu.ncsu.csc.itrust2.cucumber;

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
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;

/**
 * Class for Cucumber Testing of Personal Representatives feature.
 *
 * @author tadicke3
 * @author Zhongxiao Mei
 */
public class LabProcedureStepDefs extends CucumberTest {

    private final String baseUrl = "http://localhost:8080/iTrust2";

    private void setTextField ( final By byval, final Object value ) {
        final WebElement elem = driver.findElement( byval );
        elem.clear();
        elem.sendKeys( value.toString() );
    }

    /**
     * Fills in the date and time fields with the specified date and time.
     *
     * @param date
     *            The date to enter.
     * @param time
     *            The time to enter.
     */
    private void fillInDateTime ( final String dateField, final String date, final String timeField,
            final String time ) {
        fillInDate( dateField, date );
        fillInTime( timeField, time );
    }

    /**
     * Fills in the date field with the specified date.
     *
     * @param date
     *            The date to enter.
     */
    private void fillInDate ( final String dateField, final String date ) {
        driver.findElement( By.name( dateField ) ).clear();
        final WebElement dateElement = driver.findElement( By.name( dateField ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
    }

    /**
     * Fills in the time field with the specified time.
     *
     * @param time
     *            The time to enter.
     */
    private void fillInTime ( final String timeField, String time ) {
        // Zero-pad the time for entry
        if ( time.length() == 7 ) {
            time = "0" + time;
        }

        driver.findElement( By.name( timeField ) ).clear();
        final WebElement timeElement = driver.findElement( By.name( timeField ) );
        timeElement.sendKeys( time.replace( ":", "" ).replace( " ", "" ) );
    }

    /**
     * Login as HCP Shelly Vang.
     */
    @Given ( "I log in to iTrust2 as an HCP" )
    public void loginAsShelly () {
        attemptLogout();

        HibernateDataGenerator.generateTestLOINC();

        driver.get( baseUrl );
        waitForAngular();

        setTextField( By.name( "username" ), "svang" );
        setTextField( By.name( "password" ), "123456" );
        driver.findElement( By.className( "btn" ) ).click();

        waitForAngular();
    }

    /**
     * Login as lab tech Larry Teacher.
     */
    @Given ( "I log in to iTrust2 as a Lab Tech" )
    public void livingLikeLarry () {
        attemptLogout();

        driver.get( baseUrl );
        waitForAngular();

        setTextField( By.name( "username" ), "larrytech" );
        setTextField( By.name( "password" ), "123456" );
        driver.findElement( By.className( "btn" ) ).click();

        waitForAngular();
    }

    /**
     * Login as admin Al Minister.
     */
    @Given ( "I log in to iTrust2 as an Admin" )
    public void loginAsAl () {
        attemptLogout();

        driver.get( baseUrl );
        waitForAngular();

        setTextField( By.name( "username" ), "alminister" );
        setTextField( By.name( "password" ), "123456" );
        driver.findElement( By.className( "btn" ) ).click();

        waitForAngular();
    }

    /**
     * Create and fill out new Office Visit for patient Nellie Sanderson.
     */
    @When ( "I create a new Office Visit" )
    public void createOfficeVisit () {

        waitForAngular();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('documentOfficeVisit').click();" );

        waitForAngular();
        setTextField( By.name( "notes" ), "Billy has been experiencing symptoms of a cold or flu" );
        waitForAngular();

        driver.findElement( By.id( "patient" ) ).click();
        driver.findElement( By.name( "GENERAL_CHECKUP" ) ).click();
        driver.findElement( By.name( "hospital" ) ).click();

        fillInDateTime( "date", "10/17/2018", "time", "9:30 AM" );

        waitForAngular();
        setTextField( By.name( "height" ), "62.3" );

        waitForAngular();
        setTextField( By.name( "weight" ), "125" );

        waitForAngular();
        setTextField( By.name( "systolic" ), "110" );

        waitForAngular();
        setTextField( By.name( "diastolic" ), "75" );

        waitForAngular();
        setTextField( By.name( "hdl" ), "65" );

        waitForAngular();
        setTextField( By.name( "ldl" ), "102" );

        waitForAngular();
        setTextField( By.name( "tri" ), "147" );

        waitForAngular();
        final WebElement houseSmokeElement = driver.findElement(
                By.cssSelector( "input[value=\"" + HouseholdSmokingStatus.NONSMOKING.toString() + "\"]" ) );
        houseSmokeElement.click();

        waitForAngular();
        final WebElement patientSmokeElement = driver
                .findElement( By.cssSelector( "input[value=\"" + PatientSmokingStatus.FORMER.toString() + "\"]" ) );
        patientSmokeElement.click();
    }

    /**
     * Add lab procedure to Office Visit, assigned to Larry Teacher.
     */
    @And ( "I add a lab procedure to that visit" )
    public void addLabProc () {
        // add the lab proc
        waitForAngular();
        driver.findElement( By.name( "manual count of white blood cells in cerebral spinal fluid specimen" ) ).click();
        final Select pri = new Select( driver.findElement( By.name( "priority" ) ) );
        pri.selectByVisibleText( "High" );

        waitForAngular();
        driver.findElement( By.id( "radio-larrytech" ) ).click();
        driver.findElement( By.name( "addProcedure" ) ).click();

        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();
    }

    /**
     * Navigate Lab Tech from home page to assigned procedures.
     */
    @When ( "I navigate to Assigned Procedures" )
    public void navigateToAssigned () {
        waitForAngular();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('LTprocedures').click();" );

        waitForAngular();
    }

    /**
     * Update the status of your newest procedure to in-progress.
     */
    @When ( "I change my newest procedures status to In-Progress" )
    public void changeToInProgress () {
        waitForAngular();

        driver.findElement( By.id( "update-806-0" ) ).click();
        waitForAngular();

        final Select status = new Select( driver.findElement( By.id( "selectStatus" ) ) );
        status.selectByVisibleText( "IN_PROGRESS" );
        driver.findElement( By.id( "updateStatus" ) ).click();
    }

    /**
     * Navigate Admin from home page to procedures.
     */
    @When ( "I navigate to Admin Procedures" )
    public void navigateToAdminProc () {
        waitForAngular();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('manageLOINCCodes').click();" );

        waitForAngular();
    }

    /**
     * Create and fill-out a new lab procedure
     */
    @When ( "I add a new lab procedure" )
    public void addNewProc () {
        waitForAngular();
        setTextField( By.name( "iCode" ), "35548-7" );
        setTextField( By.name( "iComName" ), "Allergen, Fungi/Mold, Fus Monilifor, IgG" );
        setTextField( By.name( "iComponent" ), "Allergen, Fungi/Mold, F moniliforme IgG" );
        setTextField( By.name( "iProperty" ), "mcg/mL" );

        driver.findElement( By.id( "submitLOINC" ) ).click();
    }

    /**
     * Verify success message of "Office Visit created successfully".
     */
    @Then ( "I recieve a message that office visit details were changed successfully" )
    public void successOffice () {
        waitForAngular();

        // confirm that the message is displayed
        try {
            assertTrue( driver.findElement( By.name( "success" ) ).getText()
                    .contains( "Office visit created successfully" ) );

        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Verify success message of "Lab Procedure updated successfully".
     */
    @Then ( "I recieve a message that lab procedure details were changed successfully" )
    public void successInProgress () {
        waitForAngular();

        // confirm that the message is displayed
        try {
            assertTrue(
                    driver.findElement( By.id( "succUpd" ) ).getText().contains( "Successfully updated procedure" ) );

        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Verify success message of "Successfully added code".
     */
    @Then ( "I recieve a message that the procedure list was changed successfully" )
    public void successAdmin () {
        waitForAngular();

        try {
            assertTrue( driver.findElement( By.id( "succP" ) ).getText().contains( "Successfully added code" ) );

        }
        catch ( final Exception e ) {
            fail( "Success message: " + driver.findElement( By.id( "succP" ) ).getText() + "; Failure message: "
                    + driver.findElement( By.id( "errP" ) ).getText() );
        }
    }

    /**
     * I login as admin
     */
    @Given ( "I login as admin" )
    public void adminAddCodes () {
        attemptLogout();
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "admin" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
        waitForAngular();
    }

    /**
     * When I add ICD Codes, Description
     *
     * @param icd
     *            icd
     * @param description
     *            description
     */
    @When ( "I add ICD Codes: (.+), Description: (.+)" )
    public void addICDCode ( final String icd, final String description ) {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('manageICDCodes').click();" );
        waitForAngular();
        final WebElement icdCode = driver.findElement( By.name( "code" ) );
        icdCode.clear();
        icdCode.sendKeys( icd );
        final WebElement descript = driver.findElement( By.name( "description" ) );
        descript.clear();
        descript.sendKeys( description );
        final WebElement submiticd = driver.findElement( By.name( "submit" ) );
        submiticd.click();
        waitForAngular();
    }

    /**
     * I add LOINC Codes, Common Name, Component, Property
     *
     * @param loinc
     *            loinc
     * @param commonName
     *            commonName
     * @param component
     *            component
     * @param property
     *            property
     */
    @And ( "I add LOINC Codes: (.+), Common Name: (.+), Component: (.+), Property: (.+)" )
    public void addLoingCode ( final String loinc, final String commonName, final String component,
            final String property ) {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('manageLOINCCodes').click();" );
        waitForAngular();
        final WebElement loincCode = driver.findElement( By.name( "iCode" ) );
        loincCode.clear();
        loincCode.sendKeys( loinc );
        final WebElement loincCommonName = driver.findElement( By.name( "iComName" ) );
        loincCommonName.clear();
        loincCommonName.sendKeys( commonName );
        final WebElement loincComponent = driver.findElement( By.name( "iComponent" ) );
        loincComponent.clear();
        loincComponent.sendKeys( component );
        final WebElement loincProperty = driver.findElement( By.name( "iProperty" ) );
        loincProperty.clear();
        loincProperty.sendKeys( property );
        final WebElement submitloinc = driver.findElement( By.id( "submitLOINC" ) );
        submitloinc.click();
        waitForAngular();
    }

    /**
     * Then I recieve a message that codes added successfully
     */
    @Then ( "I recieve a message that codes added successfully" )
    public void codeAdded () {
        waitForAngular();

        try {
            assertTrue( driver.findElement( By.id( "succP" ) ).getText().contains( "Successfully added code" ) );

        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * I login as HCP
     */
    @Given ( "I login as HCP" )
    public void hcpLogin () {
        attemptLogout();
        driver.get( baseUrl );
        final WebElement usernamehcp = driver.findElement( By.name( "username" ) );
        usernamehcp.clear();
        usernamehcp.sendKeys( "hcp" );
        final WebElement passwordhcp = driver.findElement( By.name( "password" ) );
        passwordhcp.clear();
        passwordhcp.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
        waitForAngular();
    }

    /**
     * I create another Office Visit
     */
    @When ( "I create another Office Visit" )
    public void addAnotherOv () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('documentOfficeVisit').click();" );
        waitForAngular();

        final WebElement notes = driver.findElement( By.name( "notes" ) );
        notes.clear();
        notes.sendKeys( "Patient appears pretty much alive" );

        final WebElement patient = driver.findElement( By.name( "name" ) );
        patient.click();
        final WebElement type = driver.findElement( By.name( "GENERAL_CHECKUP" ) );
        type.click();

        final WebElement hospital = driver.findElement( By.name( "hospital" ) );
        hospital.click();

        fillInDateTime( "date", "12/19/2027", "time", "9:30 AM" );

        waitForAngular();

        final WebElement heightElement = driver.findElement( By.name( "height" ) );
        heightElement.clear();
        heightElement.sendKeys( "120" );

        final WebElement weightElement = driver.findElement( By.name( "weight" ) );
        weightElement.clear();
        weightElement.sendKeys( "120" );

        final WebElement systolicElement = driver.findElement( By.name( "systolic" ) );
        systolicElement.clear();
        systolicElement.sendKeys( "100" );

        final WebElement diastolicElement = driver.findElement( By.name( "diastolic" ) );
        diastolicElement.clear();
        diastolicElement.sendKeys( "100" );

        final WebElement hdlElement = driver.findElement( By.name( "hdl" ) );
        hdlElement.clear();
        hdlElement.sendKeys( "90" );

        final WebElement ldlElement = driver.findElement( By.name( "ldl" ) );
        ldlElement.clear();
        ldlElement.sendKeys( "100" );

        final WebElement triElement = driver.findElement( By.name( "tri" ) );
        triElement.clear();
        triElement.sendKeys( "100" );

        final WebElement houseSmokeElement = driver.findElement(
                By.cssSelector( "input[value=\"" + HouseholdSmokingStatus.NONSMOKING.toString() + "\"]" ) );
        houseSmokeElement.click();

        final WebElement patientSmokeElement = driver
                .findElement( By.cssSelector( "input[value=\"" + PatientSmokingStatus.NEVER.toString() + "\"]" ) );
        patientSmokeElement.click();

    }

    /**
     * I add another lab procedure to that visit, assigned to labtech.
     *
     * @param procedureCode
     *            procedureCode
     */
    @When ( "I add another lab procedure: (.+) to that visit" )
    public void addAnotherLabProc ( final String procedureCode ) {

        waitForAngular();
        final WebElement addprocedureCode = driver.findElement( By.name( procedureCode ) );
        addprocedureCode.click();

        final Select priorityEle = new Select( driver.findElement( By.name( "priority" ) ) );
        priorityEle.selectByVisibleText( "Low" );

        waitForAngular();
        driver.findElement( By.id( "radio-labtech" ) ).click();
        driver.findElement( By.name( "addProcedure" ) ).click();

        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();
    }

    /**
     * I logout and login as Lab Tech, go to Lab Procedures
     *
     * @param labName
     *            labName
     * @param password
     *            password
     */
    @And ( "I logout and login as Lab Tech, username: (.+) , password: (.+) , go to Lab Procedures" )
    public void labTechlogin ( final String labName, final String password ) throws Exception {
        attemptLogout();
        driver.get( baseUrl );
        final WebElement usernameLabtech = driver.findElement( By.name( "username" ) );
        usernameLabtech.clear();
        usernameLabtech.sendKeys( labName );
        final WebElement passwordLabtech = driver.findElement( By.name( "password" ) );
        passwordLabtech.clear();
        passwordLabtech.sendKeys( password );
        final WebElement submit2 = driver.findElement( By.className( "btn" ) );
        submit2.click();
        waitForAngular();

        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('LTprocedures').click();" );
        waitForAngular();
    }

    /**
     * I click update and type in Blood Sugar Level
     *
     * @param status
     *            status
     * @param levelOfbs
     *            level of blood sugar
     */
    @And ( "I click update status: (.+) and type in Blood Sugar Level: (.+)" )
    public void updateBSL ( final String status, final String levelOfbs ) throws Exception {
        final WebElement updateButtum = driver.findElement( By.cssSelector( "input[type=\"button\"]" ) );
        updateButtum.click();
        waitForAngular();

        final Select statusElement = new Select( driver.findElement( By.id( "selectStatus" ) ) );
        statusElement.selectByVisibleText( status );
        waitForAngular();

        final WebElement bslValue = driver.findElement( By.name( "bsl" ) );
        bslValue.clear();
        bslValue.sendKeys( levelOfbs );
        waitForAngular();
        driver.findElement( By.id( "updateStatus" ) ).click();
        waitForAngular();
    }

    /**
     * I update procedure to COMPLETED and type in Blood Sugar Level
     *
     * @param status
     *            status
     * @param levelOfbs
     *            level of blood sugar
     * @param comments
     *            comments
     *
     */
    @And ( "I click update status: (.+), type in Blood Sugar Level: (.+), and write comments:(.+)" )
    public void updateBSLCompleted ( final String status, final String levelOfbs, final String comments )
            throws Exception {
        final WebElement updateButtum2 = driver.findElement( By.cssSelector( "input[type=\"button\"]" ) );
        updateButtum2.click();
        waitForAngular();

        final Select statusElement = new Select( driver.findElement( By.id( "selectStatus" ) ) );
        statusElement.selectByVisibleText( status );
        waitForAngular();

        final WebElement bslValue = driver.findElement( By.name( "bsl" ) );
        bslValue.clear();
        bslValue.sendKeys( levelOfbs );
        waitForAngular();

        final WebElement commentIn = driver.findElement( By.name( "commentsInput" ) );
        commentIn.clear();
        commentIn.sendKeys( comments );
        waitForAngular();

        driver.findElement( By.id( "updateStatus" ) ).click();
        waitForAngular();
    }

    /**
     * I recieve a message that Successfully updated procedure
     */
    @Then ( "I recieve a message that Successfully updated procedure" )
    public void updateBSLSuccess () {
        waitForAngular();

        // confirm that the message is displayed
        try {
            assertTrue(
                    driver.findElement( By.id( "succUpd" ) ).getText().contains( "Successfully updated procedure" ) );

        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * I logout and login as HCP again.
     */
    @And ( "I logout and login as HCP again" )
    public void hcpLoginAgain () {
        attemptLogout();
        driver.get( baseUrl );
        final WebElement usernamehcp2 = driver.findElement( By.name( "username" ) );
        usernamehcp2.clear();
        usernamehcp2.sendKeys( "hcp" );
        final WebElement passwordhcp2 = driver.findElement( By.name( "password" ) );
        passwordhcp2.clear();
        passwordhcp2.sendKeys( "123456" );
        final WebElement submit3 = driver.findElement( By.className( "btn" ) );
        submit3.click();
        waitForAngular();
    }

    /**
     * I go to edit office visit page, select the patient, select Diagnosis Code
     * and confirm Diabetes Diagnosis
     *
     * @param diagnosisCode
     *            diagnosisCode
     */
    @And ( "I go to edit office visit page, select the patient, select Diagnosis Code: (.+) and confirm Diabetes Diagnosis" )
    public void hcpConfirm ( final String diagnosisCode ) {
        waitForAngular();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('editOfficeVisit').click();" );
        waitForAngular();
        driver.findElement( By.cssSelector( "input[type=\"radio\"]" ) ).click();
        waitForAngular();
        driver.findElement( By.name( diagnosisCode ) ).click();
        waitForAngular();
        driver.findElement( By.name( "confirm" ) ).click();
        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();
        waitForAngular();
    }

    /**
     * I receive the message shows Office visit edited successfully
     */
    @Then ( "I receive the message shows Office visit edited successfully" )
    public void updateDiagnosisSuccess () {
        waitForAngular();

        try {
            driver.findElement( By.name( "success" ) ).getText().contains( "Office visit edited successfully" );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * I go to set blood sugar limit for a patient, Upper Bound of Fasting,
     * Upper Bound After Meal
     *
     * @param upperFasting
     *            Upper Bound of Fasting
     * @param upperMeal
     *            Upper Bound After Meal
     */
    @And ( "I go to set blood sugar limit for a patient, Upper Bound of Fasting: (.+), Upper Bound After Meal: (.+)" )
    public void setBloodSugarLimit ( final String upperFasting, final String upperMeal ) {
        waitForAngular();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('setBSL').click();" );
        waitForAngular();
        driver.findElement( By.cssSelector( "input[type=\"radio\"]" ) ).click();
        waitForAngular();
        final WebElement upperOfFasting = driver.findElement( By.name( "upperBoundFasting" ) );
        upperOfFasting.clear();
        upperOfFasting.sendKeys( upperFasting );
        final WebElement upperOfMeal = driver.findElement( By.name( "upperBoundMeal" ) );
        upperOfMeal.clear();
        upperOfMeal.sendKeys( upperMeal );
        final WebElement submitBSLLimit = driver.findElement( By.name( "confirm" ) );
        submitBSLLimit.click();
        waitForAngular();
    }

    /**
     * I receive the message shows blood sugar Limit set successfully
     */
    @Then ( "I receive the message shows blood sugar Limit set successfully" )
    public void setBloodSugarLimitSuccess () {
        waitForAngular();
        try {
            assertTrue( driver.findElement( By.name( "success" ) ).getText()
                    .contains( "blood sugar Limit set successfully" ) );
        }
        catch ( final Exception e ) {
            fail();
        }
    }
}
