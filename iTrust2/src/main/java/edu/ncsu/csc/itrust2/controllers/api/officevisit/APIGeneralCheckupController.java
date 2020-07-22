package edu.ncsu.csc.itrust2.controllers.api.officevisit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.controllers.api.APIController;
import edu.ncsu.csc.itrust2.forms.hcp.GeneralCheckupForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.GeneralCheckup;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class that provides REST API endpoints for the GeneralCheckup model. In all
 * requests made to this controller, the {id} provided is a Long that is the
 * primary key id of the office visit requested.
 *
 * @author Kai Presler-Marshall
 * @author Jack MacDonald
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIGeneralCheckupController extends APIController {

    /**
     * Retrieves the GeneralCheckup specified by the id provided.
     *
     * @param id
     *            The (numeric) ID of the OfficeVisit desired
     * @return response
     */
    @GetMapping ( BASE_PATH + "/generalcheckups/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_PATIENT')" )
    public ResponseEntity getGeneralCheckup ( @PathVariable ( "id" ) final Long id ) {
        final GeneralCheckup visit = GeneralCheckup.getById( id );
        if ( null == visit ) {
            return new ResponseEntity( errorResponse( "No office visit found for id " + id ), HttpStatus.NOT_FOUND );
        }
        else {
            final User self = User.getByName( LoggerUtil.currentUser() );
            if ( null != self && self.isDoctor() ) {
                LoggerUtil.log( TransactionType.GENERAL_CHECKUP_HCP_VIEW, LoggerUtil.currentUser(),
                        visit.getPatient().getUsername() );
            }
            else {
                LoggerUtil.log( TransactionType.GENERAL_CHECKUP_PATIENT_VIEW, LoggerUtil.currentUser() );
            }
            return new ResponseEntity( visit, HttpStatus.OK );
        }
    }

    /**
     * Deletes the GeneralCheckup with the id provided. This will remove all
     * traces from the system and cannot be reversed.
     *
     * @param id
     *            The id of the OfficeVisit to delete
     * @return response
     */
    @DeleteMapping ( BASE_PATH + "/generalcheckups/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_OD', 'ROLE_OPH', 'ROLE_PATIENT')" )
    public ResponseEntity deleteGeneralCheckup ( @PathVariable final Long id ) {
        final GeneralCheckup visit = GeneralCheckup.getById( id );
        if ( null == visit ) {
            return new ResponseEntity( errorResponse( "No office visit found for " + id ), HttpStatus.NOT_FOUND );
        }
        try {
            visit.delete();
            LoggerUtil.log( TransactionType.GENERAL_CHECKUP_DELETE, LoggerUtil.currentUser() );
            return new ResponseEntity( id, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity( errorResponse( "Could not delete " + id + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Creates and saves a new GeneralCheckup from the RequestBody provided.
     *
     * @param visitForm
     *            The office visit to be validated and saved
     * @return response
     */

    @PostMapping ( BASE_PATH + "/generalcheckups" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH')" )
    public ResponseEntity createGeneralCheckup ( @RequestBody final GeneralCheckupForm visitForm ) {
        try {
            final GeneralCheckup visit = new GeneralCheckup( visitForm );

            if ( null != GeneralCheckup.getById( visit.getId() ) ) {
                return new ResponseEntity(
                        errorResponse( "Office visit with the id " + visit.getId() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
            visit.save();
            LoggerUtil.log( TransactionType.GENERAL_CHECKUP_CREATE, LoggerUtil.currentUser(),
                    visit.getPatient().getUsername() );
            return new ResponseEntity( visit, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the OfficeVisit provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Updates the OfficeVisit with the id provided by overwriting it with the
     * new OfficeVisit that is provided. If the ID provided does not match the
     * ID set in the OfficeVisit provided, the update will not take place
     *
     * @param id
     *            The ID of the OfficeVisit to be updated
     * @param form
     *            The updated OfficeVisit to save
     * @return response
     */
    @PutMapping ( BASE_PATH + "/generalcheckups/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_PATIENT')" )
    public ResponseEntity updateGeneralCheckup ( @PathVariable final Long id,
            @RequestBody final GeneralCheckupForm form ) {
        try {
            final GeneralCheckup visit = new GeneralCheckup( form );
            if ( null != visit.getId() && !id.equals( visit.getId() ) ) {
                return new ResponseEntity(
                        errorResponse( "The ID provided does not match the ID of the OfficeVisit provided" ),
                        HttpStatus.CONFLICT );
            }
            final GeneralCheckup dbVisit = GeneralCheckup.getById( id );
            if ( null == dbVisit ) {
                return new ResponseEntity( errorResponse( "No visit found for name " + id ), HttpStatus.NOT_FOUND );
            }
            // It is possible that the HCP did not update the BHM but only the
            // other fields (date, time, etc) thus we need to check if the old
            // BHM is different from the new BHM before logging
            if ( !dbVisit.getBasicHealthMetrics().equals( visit.getBasicHealthMetrics() ) ) {
                LoggerUtil.log( TransactionType.GENERAL_CHECKUP_EDIT, form.getHcp(), form.getPatient(), form.getHcp()
                        + " updated basic health metrics for " + form.getPatient() + " from " + form.getDate() );
            }
            visit.save(); /* Will overwrite existing request */
            LoggerUtil.log( TransactionType.GENERAL_CHECKUP_EDIT, LoggerUtil.currentUser() );
            return new ResponseEntity( visit, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not update " + form.toString() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * This is used as a marker for the system to know that the HCP has viewed
     * the visit
     *
     * @param id
     *            The id of the office visit being viewed
     * @param form
     *            The office visit being viewed
     * @return OK if the office visit is found, NOT_FOUND otherwise
     */
    @PostMapping ( BASE_PATH + "/generalcheckups/hcp/view/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH')" )
    public ResponseEntity viewGeneralCheckup ( @PathVariable final Long id,
            @RequestBody final GeneralCheckupForm form ) {
        final GeneralCheckup dbVisit = GeneralCheckup.getById( id );
        if ( null == dbVisit ) {
            return new ResponseEntity( errorResponse( "No visit found for name " + id ), HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.GENERAL_CHECKUP_HCP_VIEW, form.getHcp(), form.getPatient(),
                form.getHcp() + " viewed basic health metrics for " + form.getPatient() + " from " + form.getDate() );
        return new ResponseEntity( HttpStatus.OK );
    }

    /**
     * This is used as a marker for the system to know that the patient has
     * viewed the visit
     *
     * @param id
     *            The id of the office visit being viewed
     * @param form
     *            The office visit being viewed
     * @return OK if the office visit is found, NOT_FOUND otherwise
     */
    @PostMapping ( BASE_PATH + "/generalcheckups/patient/view/{id}" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity viewGeneralCheckupPatient ( @PathVariable final Long id,
            @RequestBody final GeneralCheckupForm form ) {
        final GeneralCheckup dbVisit = GeneralCheckup.getById( id );
        if ( null == dbVisit ) {
            return new ResponseEntity( errorResponse( "No visit found for name " + id ), HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.GENERAL_CHECKUP_PATIENT_VIEW, form.getHcp(), form.getPatient(),
                form.getPatient() + " viewed their basic health metrics from " + form.getDate() );
        return new ResponseEntity( HttpStatus.OK );
    }

}
