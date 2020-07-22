package edu.ncsu.csc.itrust2.controllers.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.patient.DailyBloodSugarLevelForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.DailyBloodSugarLevelEntry;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Provide REST API that deal with Blood Sugar Level Limits. Funtionality: Get
 * and Edit
 *
 * @author Xianzhen Huang
 * @author Zhongxiao Mei
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIDailyBSLEntryController extends APIController {

    // part 01 -- DailyEntry
    /**
     * Creates a new DailyBSL object and saves it to the DB
     *
     * @param entry
     *            the form being used to create a DailyBSL object
     * @return a response containing results of creating a new entry
     */
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    @PostMapping ( BASE_PATH + "/BSLdiary" )
    public ResponseEntity addEntry ( @RequestBody final DailyBloodSugarLevelForm entry ) {
        try {
            final DailyBloodSugarLevelEntry dEntry = new DailyBloodSugarLevelEntry( entry );
            dEntry.setPatient( LoggerUtil.currentUser() );
            dEntry.save();

            LoggerUtil.log( TransactionType.CREATE_BSL_DAILY_ENTRY, LoggerUtil.currentUser() );
            return new ResponseEntity( dEntry, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not create BSL Daily Entry provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    // load csv file by Patient
    // Cite: help from https://stackabuse.com/reading-and-writing-csvs-in-java/
    /**
     * Create the data entry by using the csv file.
     *
     * @param csvfile
     *            the csvfile
     * @return a response containing results of creating a new entry
     */
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    @PostMapping ( BASE_PATH + "/BSLdiarycsv" )
    public ResponseEntity addEntry ( @RequestBody final File csvfile ) {
        try {
            @SuppressWarnings ( "resource" )
            final BufferedReader csvReader = new BufferedReader( new FileReader( csvfile ) );
            String row;
            // check whether this is a last line of csv
            while ( ( row = csvReader.readLine() ) != null ) {
                // each line is a new entry
                final String[] line = row.split( "," );
                // create a entry with loaded data from csv file, all data in
                // string format, use form to make transition
                final DailyBloodSugarLevelForm entry = new DailyBloodSugarLevelForm( line[0], line[1], line[2], line[3],
                        line[4] );

                // load converted data from form into database
                final DailyBloodSugarLevelEntry dEntry = new DailyBloodSugarLevelEntry( entry );

                // set current patient into database
                dEntry.setPatient( LoggerUtil.currentUser() );

                // save the entry
                dEntry.save();

            }
            return new ResponseEntity( HttpStatus.OK );
        }
        catch ( final Exception e ) {

            // print out error information
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not create BSL Daily Entry by CSV file provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    // delete api may have problem due to date system --- start of not used
    /*
     ** Retrieves a list of patient DiaryEntries, either for the current patient
     * if the user has role PATIENT, or all of the patients if the user is an
     * HCP.
     * @return a list of patient's food diary entries
     * @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
     * @DeleteMapping ( BASE_PATH + "/BSLdiary/" + "{patient}" ) public
     * ResponseEntity deleteEntriesPatient ( String date, String patient) {
     * //find entry try { final DailyBloodSugarLevelEntry entry =
     * DailyBloodSugarLevelEntry.getByDate(); if (entry == null ) {
     * LoggerUtil.log( TransactionType.DRUG_DELETE, LoggerUtil.currentUser(),
     * "Could not find drug with date" ); return new ResponseEntity(
     * errorResponse( "No drug found with date"), HttpStatus.NOT_FOUND ); }
     * entry.delete(); LoggerUtil.log( TransactionType.DRUG_DELETE,
     * LoggerUtil.currentUser(), "Deleted drug with date "); return new
     * ResponseEntity( HttpStatus.OK ); } catch ( final Exception e ) {
     * LoggerUtil.log( TransactionType.DRUG_DELETE, LoggerUtil.currentUser(),
     * "Failed to delete drug" ); return new ResponseEntity( errorResponse(
     * "Could not delete drug: " + e.getMessage() ), HttpStatus.BAD_REQUEST ); }
     * }
     */
    // ------------not used

    // get single day's entry information
    /**
     * Retrieves a list of patient DiaryEntries, either for the current patient
     * if the user has role PATIENT, or all of the patients if the user is an
     * HCP.
     *
     * 
     * @return a list of patient's Daily BSL entries
     * 
     */

    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    @GetMapping ( BASE_PATH + "/BSLdiary/PatientAll" )
    public ResponseEntity getByPatient () {

        LoggerUtil.log( TransactionType.VIEW_BSL_DAILY_ENTRY, LoggerUtil.currentUser() );
        return new ResponseEntity( DailyBloodSugarLevelEntry.getByPatient( LoggerUtil.currentUser() ), HttpStatus.OK );
    }

    /**
     * Retrieves a list of patient DiaryEntries, either for the current patient
     * if the user has role PATIENT, or all of the patients if the user is an
     * HCP.
     * 
     * @param patient
     *            patient's name
     * @return a list of patient's Daily BSL entries
     */
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    @GetMapping ( BASE_PATH + "/BSLdiary/HCPAll/{patient}" )
    public ResponseEntity getByPatient ( @PathVariable final String patient ) {
        LoggerUtil.log( TransactionType.VIEW_BSL_DAILY_ENTRY, LoggerUtil.currentUser() );
        return new ResponseEntity( DailyBloodSugarLevelEntry.getByPatient( patient ), HttpStatus.OK );
    }

    /**
     * Get a list with matched argument from the DataBase
     *
     * @param patient
     *            the owner of required list
     *
     *            String time format: yyyy-mm-dd
     * @param startTime
     *            start date of the required list
     * @param timeLength
     *            the length of the search date: day/week/month
     *
     * @return list of DailyBloodSugarLevelEntry object
     */
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    @GetMapping ( BASE_PATH + "/BSLdiary/{patient}/{startTime}/{timeLength}" )
    public ResponseEntity getBSLbyDateRange ( @PathVariable final String patient, @PathVariable final String startTime,
            @PathVariable final String timeLength ) {
        // if startTime == endTime->day
        LocalDate time1 = LocalDate.parse( startTime );
        LocalDate time2 = null;
        switch ( timeLength ) {
            case "DAY":
                time2 = time1.plusDays( 1 );
                break;
            case "WEEK":
                time2 = time1.plusWeeks( 1 );
                break;
            case "MONTH":
                time2 = time1.plusMonths( 1 );
                break;
            default:
                time2 = time1.plusDays( 1 );
                break;
        }
        // if( time2 == null) return ;
        time2 = time2.minusDays( 1 );
        final List<DailyBloodSugarLevelEntry> result = new ArrayList<DailyBloodSugarLevelEntry>();
        do {
            LoggerUtil.log( TransactionType.VIEW_BSL_DAILY_ENTRY, LoggerUtil.currentUser() );
            final DailyBloodSugarLevelEntry temp = DailyBloodSugarLevelEntry.getByTime( patient, time1 );
            if ( temp != null ) {
                result.add( temp ); // exit the
            }
            time1 = time1.plusDays( 1 );
        }
        while ( !time1.isAfter( time2 ) );
        return new ResponseEntity( result, HttpStatus.OK );
        // all
        // LoggerUtil.log( TransactionType.VIEW_BSL_DAILY_ENTRY,
        // LoggerUtil.currentUser() );
        // return new ResponseEntity( DailyBloodSugarLevelEntry.getByPatient(
        // patient ), HttpStatus.OK );
    }

    /**
     * Get a list with matched argument from the DataBase
     *
     *
     *
     * String time format: yyyy-mm-dd
     * 
     * @param startTime
     *            start date of the required list
     * @param timeLength
     *            the length of the search date: day/week/month
     *
     * @return list of DailyBloodSugarLevelEntry object
     */
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    @GetMapping ( BASE_PATH + "/BSLdiary/{startTime}/{timeLength}" )
    public ResponseEntity getBSLbyDateRange ( @PathVariable final String startTime,
            @PathVariable final String timeLength ) {
        // if startTime == endTime->day
        // search '-' to check whether to use yyyy-mm-dd or yyyy-m-dd or
        // yyyy-mm-d or yyyy-m-d
        LocalDate time1 = LocalDate.parse( startTime );
        LocalDate time2 = null;
        switch ( timeLength ) {
            case "DAY":
                time2 = time1.plusDays( 1 );
                break;
            case "WEEK":
                time2 = time1.plusWeeks( 1 );
                break;
            case "MONTH":
                time2 = time1.plusMonths( 1 );
                break;
            default:
                time2 = time1.plusDays( 1 );
                break;
        }
        // if( time2 == null) return ;
        time2 = time2.minusDays( 1 );
        final List<DailyBloodSugarLevelEntry> result = new ArrayList<DailyBloodSugarLevelEntry>();
        do {
            LoggerUtil.log( TransactionType.VIEW_BSL_DAILY_ENTRY, LoggerUtil.currentUser() );
            final DailyBloodSugarLevelEntry temp = DailyBloodSugarLevelEntry.getByTime( LoggerUtil.currentUser(),
                    time1 );
            if ( temp != null ) {
                result.add( temp ); // exit the
            }

            time1 = time1.plusDays( 1 );
        }
        while ( !time1.isAfter( time2 ) );
        return new ResponseEntity( result, HttpStatus.OK );
        // all
        // LoggerUtil.log( TransactionType.VIEW_BSL_DAILY_ENTRY,
        // LoggerUtil.currentUser() );
        // return new ResponseEntity( DailyBloodSugarLevelEntry.getByPatient(
        // patient ), HttpStatus.OK );
    }

}
