package edu.ncsu.csc.itrust2.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.patient.BloodSugarLimitsForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.BloodSugarLimits;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Provide REST API that deal with Blood Sugar Level Limits. Funtionality: Get
 * and Edit
 *
 * @author Xianzhen Huang
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIBSLLimitsController extends APIController {
    /**
     * add a new limits
     *
     * @param entry
     *            input from frontend
     * @param patient
     *            the patient name
     * @return the requested code
     */
    @PostMapping ( BASE_PATH + "/BSLlimits/{patient}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity addNewLimits ( @RequestBody final BloodSugarLimitsForm entry,
            @PathVariable final String patient ) {
        try {
            final BloodSugarLimits dEntry = new BloodSugarLimits( entry );
            dEntry.setPatient( patient );
            dEntry.save();

            LoggerUtil.log( TransactionType.BLOODSUGARLEVEL_LIMITS_CREATE, LoggerUtil.currentUser() );
            return new ResponseEntity( dEntry, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity( errorResponse( "Could not create BSL limits provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Updates the code with the specified ID to the value supplied.
     *
     * @param form
     *            The new values for the Code
     * @return The Response of the action
     */
    @PutMapping ( BASE_PATH + "/BSLlimits" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity updateLimits ( @RequestBody final BloodSugarLimitsForm form ) {
        final String patientname = form.getPatient();
        try {

            final BloodSugarLimits old = BloodSugarLimits.getByPatient( patientname );
            // if ( old == null ) {
            // return new ResponseEntity( "No limits with " + patientname,
            // HttpStatus.NOT_FOUND );
            // }
            // form.setId( id );
            old.update( form );
            old.save();
            User user = null;
            try {
                user = User.getByName( LoggerUtil.currentUser() );
            }
            catch ( final Exception e ) {
                // ignore, its was a test that wasn't authenticated properly.
            }
            LoggerUtil.log( TransactionType.BLOODSUGARLEVEL_LIMITS_EDIT, user.getUsername(),
                    user.getUsername() + " edited an ICD Code" );

            return new ResponseEntity( old, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Could not update Patient " + patientname
                    + "'s Blood Sugar Level Limits because of " + e.getMessage() ), HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Retrieves a list of patient DiaryEntries, either for the current patient
     * if the user has role PATIENT, or all of the patients if the user is an
     * HCP.
     *
     * @param patient
     *            Patient's name
     * @return patient's blood sugar level limits
     */
    @GetMapping ( BASE_PATH + "/BSLlimits/{patient}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity getLimitsPatient ( @PathVariable final String patient ) {
        LoggerUtil.log( TransactionType.BLOODSUGARLEVEL_LIMITS_VIEW, LoggerUtil.currentUser() );
        try {
            final BloodSugarLimits bsl = BloodSugarLimits.getByPatient( patient );
            return new ResponseEntity( bsl, HttpStatus.OK );
        }
        catch ( final IllegalArgumentException e ) {
            final BloodSugarLimits bsl = new BloodSugarLimits();
            bsl.setPatient( patient );
            bsl.setUpperBoundFasting( 100.f );
            bsl.setUpperBoundMeal( 140.f );
            bsl.save();
            return new ResponseEntity( bsl, HttpStatus.OK );
        }
    }

    /**
     * Retrieves a list of patient DiaryEntries, either for the current patient
     * if the user has role PATIENT, or all of the patients if the user is an
     * HCP.
     *
     * @return patient's blood sugar level limits
     */
    @GetMapping ( BASE_PATH + "/BSLlimitsPatient" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity getLimitsPatient () {
        // LoggerUtil.log( TransactionType.BLOODSUGARLEVEL_LIMITS_VIEW,
        // LoggerUtil.currentUser() );
        try {
            final BloodSugarLimits bsl = BloodSugarLimits.getByPatient( LoggerUtil.currentUser() );
            return new ResponseEntity( bsl, HttpStatus.OK );
        }
        catch ( final IllegalArgumentException e ) {
            final BloodSugarLimits bsl = new BloodSugarLimits();
            bsl.setPatient( LoggerUtil.currentUser() );
            bsl.setUpperBoundFasting( 100.f );
            bsl.setUpperBoundMeal( 140.f );
            bsl.save();
            return new ResponseEntity( bsl, HttpStatus.OK );
        }
    }

}
