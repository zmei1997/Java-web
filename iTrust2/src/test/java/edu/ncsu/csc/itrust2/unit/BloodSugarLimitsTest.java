package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.patient.BloodSugarLimitsForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.BloodSugarLimits;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * test for blood sugar limit
 *
 * @author sliu20
 *
 */
public class BloodSugarLimitsTest {

    /** Patient to be used for testing */
    final User patient = new User( "patient", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
            Role.ROLE_PATIENT, 1 );

    /**
     * setup
     */
    @Before
    public void setUp () {
        DomainObject.deleteAll( BloodSugarLimits.class );
    }

    /**
     * test for the bsl
     */
    @Test
    public void testBloodSugarLimits () {
        final BloodSugarLimits bsl = new BloodSugarLimits();

        bsl.setPatient( "patient" );
        bsl.setUpperBoundFasting( 100.f );
        bsl.setUpperBoundMeal( 140.f );
        bsl.save();

        final BloodSugarLimits copy = BloodSugarLimits.getById( bsl.getId() );
        assertEquals( bsl.getUpperBoundFasting(), copy.getUpperBoundFasting(), 0.01 );
        assertEquals( bsl.getUpperBoundMeal(), copy.getUpperBoundMeal(), 0.01 );
        assertEquals( bsl.getPatient(), copy.getPatient() );

        final BloodSugarLimits copy2 = BloodSugarLimits.getByPatient( "patient" );
        final BloodSugarLimitsForm bslf = new BloodSugarLimitsForm( copy2 );
        assertEquals( bsl.getUpperBoundFasting(), bslf.getUpperBoundFasting(), 0.01 );
        assertEquals( bsl.getUpperBoundMeal(), bslf.getUpperBoundMeal(), 0.01 );
        copy2.setId( (long) 1 );
        assertNotEquals( copy2.getId(), bsl.getId() );
        assertNull( BloodSugarLimits.getById( bsl.getId() - 1 ) );

        final BloodSugarLimitsForm bslf2 = new BloodSugarLimitsForm();
        bslf2.setUpperBoundFasting( 100.f );
        bslf2.setUpperBoundMeal( 140.f );
        final BloodSugarLimits bsl2 = new BloodSugarLimits( bslf2 );
        assertEquals( bsl2.getUpperBoundFasting(), copy.getUpperBoundFasting(), 0.01 );
        assertEquals( bsl2.getUpperBoundMeal(), copy.getUpperBoundMeal(), 0.01 );

        bslf2.setUpperBoundFasting( 110.f );
        bslf2.setUpperBoundMeal( 150.f );
        bsl2.update( bslf2 );
        assertEquals( bsl2.getUpperBoundFasting(), bslf2.getUpperBoundFasting(), 0.01 );
        assertEquals( bsl2.getUpperBoundMeal(), bslf2.getUpperBoundMeal(), 0.01 );
    }

    /**
     * Tests giving invalid values for setter methods.
     */
    @Test
    public void testBloodSugarLimitsInvalid () {
        final BloodSugarLimits entry = new BloodSugarLimits();
        try {
            entry.setUpperBoundFasting( -1 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Upper Bound for Fasting should be between 80 and 130 mg/dl", e.getMessage() );
        }
        try {
            entry.setUpperBoundFasting( 501 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Upper Bound for Fasting should be between 80 and 130 mg/dl", e.getMessage() );
        }
        try {
            entry.setUpperBoundMeal( -1 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Upper Bound for meal should be between 120 and 180 mg/dl", e.getMessage() );
        }
        try {
            entry.setUpperBoundMeal( 501 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Upper Bound for meal should be between 120 and 180 mg/dl", e.getMessage() );
        }
    }
}
