package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.patient.DailyBloodSugarLevelForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.DailyBloodSugarLevelEntry;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * test for dbsl entry
 *
 * @author sliu20
 *
 */
public class DailyBloodSugarLevelEntryTest {

    /** Patient to be used for testing */
    final User patient = new User( "patient", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
            Role.ROLE_PATIENT, 1 );

    /**
     * setup
     */
    @Before
    public void setUp () {
        DomainObject.deleteAll( DailyBloodSugarLevelEntry.class );
    }

    /**
     * Testing DailyBloodSugarLevelEntry with a default constructor and with a
     * DailyBloodSugarLevelEntryForm passed into the constructor. Also tests all
     * Setters and getById.
     */
    @Test
    public void testDiaryEntry () {
        final DailyBloodSugarLevelEntry entry = new DailyBloodSugarLevelEntry();
        final LocalDate entryDate = LocalDate.parse( "2018-09-03" );

        entry.setDate( entryDate );
        entry.setFasting( 100 );
        entry.setMeal1( 100 );
        entry.setMeal2( 100 );
        entry.setMeal3( 100 );
        // entry.setUpperBoundFasting( 100.f );
        // entry.setUpperBoundMeal( 140.f );

        entry.setPatient( "patient" );
        entry.save();

        final DailyBloodSugarLevelEntry copy = DailyBloodSugarLevelEntry.getById( entry.getId() );
        assertEquals( entry.getId(), copy.getId() );
        assertEquals( entry.getDate(), copy.getDate() );
        assertEquals( entry.getFasting(), copy.getFasting(), 0.01 );
        assertEquals( entry.getMeal1(), copy.getMeal1(), 0.01 );
        assertEquals( entry.getMeal2(), copy.getMeal2(), 0.01 );
        assertEquals( entry.getMeal3(), copy.getMeal3(), 0.01 );
        // assertEquals( entry.getUpperBoundFasting(),
        // copy.getUpperBoundFasting(), 0.01 );
        // assertEquals( entry.getUpperBoundMeal(), copy.getUpperBoundMeal(),
        // 0.01 );
        assertEquals( entry.getPatient(), copy.getPatient() );

        final List<DailyBloodSugarLevelEntry> copyList = DailyBloodSugarLevelEntry.getByPatient( "patient" );
        final DailyBloodSugarLevelEntry copy2 = copyList.get( 0 );
        assertEquals( entry.getId(), copy2.getId() );
        assertEquals( entry.getDate(), copy2.getDate() );
        assertEquals( entry.getFasting(), copy2.getFasting(), 0.01 );
        assertEquals( entry.getMeal1(), copy2.getMeal1(), 0.01 );
        assertEquals( entry.getMeal2(), copy2.getMeal2(), 0.01 );
        assertEquals( entry.getMeal3(), copy2.getMeal3(), 0.01 );
        // assertEquals( entry.getUpperBoundFasting(),
        // copy2.getUpperBoundFasting(), 0.01 );
        // assertEquals( entry.getUpperBoundMeal(), copy2.getUpperBoundMeal(),
        // 0.01 );
        assertEquals( entry.getPatient(), copy2.getPatient() );

        final DailyBloodSugarLevelForm dbsf = new DailyBloodSugarLevelForm();
        dbsf.setDate( entryDate.toString() );
        dbsf.setFasting( 100 );
        dbsf.setMeal1( 100 );
        dbsf.setMeal2( 100 );
        dbsf.setMeal3( 100 );
        // dbsf.setUpperBoundFasting( 100.f );
        // dbsf.setUpperBoundMeal( 140.f );

        final DailyBloodSugarLevelEntry entry2 = new DailyBloodSugarLevelEntry( dbsf );
        entry2.setPatient( "patient" );
        entry2.save();

        assertNotEquals( entry.getId(), entry2.getId() );
        assertEquals( entry.getDate(), entry2.getDate() );
        assertEquals( entry.getFasting(), entry2.getFasting(), 0.01 );
        assertEquals( entry.getMeal1(), entry2.getMeal1(), 0.01 );
        assertEquals( entry.getMeal2(), entry2.getMeal2(), 0.01 );
        assertEquals( entry.getMeal3(), entry2.getMeal3(), 0.01 );
        // assertEquals( entry.getUpperBoundFasting(),
        // entry2.getUpperBoundFasting(), 0.01 );
        // assertEquals( entry.getUpperBoundMeal(), entry2.getUpperBoundMeal(),
        // 0.01 );
        assertEquals( entry.getPatient(), entry2.getPatient() );

        // assertEquals( entry.getUpperBoundFasting(),
        // dbsf.getUpperBoundFasting(), 0.01 );
        // assertEquals( entry.getUpperBoundMeal(), dbsf.getUpperBoundMeal(),
        // 0.01 );

        final DailyBloodSugarLevelForm dbsf2 = new DailyBloodSugarLevelForm( entry );
        assertEquals( entry.getDate(), dbsf2.getDate() );
        assertEquals( entry.getFasting(), dbsf2.getFasting(), 0.01 );
        assertEquals( entry.getMeal1(), dbsf2.getMeal1(), 0.01 );
        assertEquals( entry.getMeal2(), dbsf2.getMeal2(), 0.01 );
        assertEquals( entry.getMeal3(), dbsf2.getMeal3(), 0.01 );
        assertFalse( dbsf2.isExceeded() );
    }

    /**
     * Tests giving invalid values for setter methods.
     */
    @Test
    public void testDiaryEntryInvalid () {
        final DailyBloodSugarLevelEntry entry = new DailyBloodSugarLevelEntry();
        try {
            entry.setDate( LocalDate.parse( "9000-09-03" ) );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Date must be before current date", e.getMessage() );
        }
        try {
            entry.setFasting( -1 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "fasting must be a positive integer 0-500", e.getMessage() );
        }
        try {
            entry.setFasting( 501 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "fasting must be a positive integer 0-500", e.getMessage() );
        }
        try {
            entry.setMeal1( -1 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "meal1 must be a positive integer 0-500", e.getMessage() );
        }
        try {
            entry.setMeal1( 501 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "meal1 must be a positive integer 0-500", e.getMessage() );
        }
        try {
            entry.setMeal2( -1 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "meal2 must be a positive integer 0-500", e.getMessage() );
        }
        try {
            entry.setMeal2( 501 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "meal2 must be a positive integer 0-500", e.getMessage() );
        }
        try {
            entry.setMeal3( -1 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "meal3 must be a positive integer 0-500", e.getMessage() );
        }
        try {
            entry.setMeal3( 501 );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "meal3 must be a positive integer 0-500", e.getMessage() );
        }
        entry.setId( (long) 1 );
        final DailyBloodSugarLevelEntry copy = DailyBloodSugarLevelEntry.getById( entry.getId() + 1 );
        assertNull( copy );

    }
}
