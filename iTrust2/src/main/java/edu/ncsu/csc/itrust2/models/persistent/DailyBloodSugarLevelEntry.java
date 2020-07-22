package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.criterion.Criterion;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.itrust2.adapters.LocalDateAdapter;
import edu.ncsu.csc.itrust2.forms.patient.DailyBloodSugarLevelForm;

/**
 * Daily Blood Sugar Level Entry
 *
 * @author Xianzhen Huang
 * @author Zhongxiao Mei
 * @author Sichen Liu
 *
 */
@Entity
@Table ( name = "DailyBloodSugarLevelEntry" )
public class DailyBloodSugarLevelEntry extends DomainObject<DailyBloodSugarLevelEntry> implements Serializable {

    /**
     * the static method to get entry by id
     *
     * @param id
     *            the id in database
     * @return return the daily sugar entry
     */
    public static DailyBloodSugarLevelEntry getById ( final Long id ) {
        try {
            return (DailyBloodSugarLevelEntry) getWhere( DailyBloodSugarLevelEntry.class, eqList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * get all entry by patient name
     *
     * @param patient
     *            the username of the patient
     * @return return the list of entry
     */
    @SuppressWarnings ( "unchecked" )
    public static List<DailyBloodSugarLevelEntry> getByPatient ( final String patient ) {
        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "patient", patient ) );

        return (List<DailyBloodSugarLevelEntry>) getWhere( DailyBloodSugarLevelEntry.class, criteria );
    }

    /**
     * get all entry by two time for current patient
     *
     * @param patient
     *            the username of the patient
     * @param time
     *            time
     * @return return the list of entry
     *
     */
    // @SuppressWarnings ( "unchecked" )
    public static DailyBloodSugarLevelEntry getByTime ( final String patient, final LocalDate time ) {
        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "patient", patient ) );
        criteria.add( eq( "date", time ) );
        DailyBloodSugarLevelEntry a = new DailyBloodSugarLevelEntry();
        try {
            a = (DailyBloodSugarLevelEntry) getWhere( DailyBloodSugarLevelEntry.class, criteria ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
        return a;
        // return (DailyBloodSugarLevelEntry) getWhere(
        // DailyBloodSugarLevelEntry.class, criteria ).get( 0 );
    }

    /**
     * unique id
     */
    private static final long serialVersionUID = -6270989967562929101L;
    /**
     * The date as milliseconds since epoch of this DiaryEntry
     */

    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = LocalDateConverter.class )
    @JsonAdapter ( LocalDateAdapter.class )
    private LocalDate         date;

    /**
     * the patient
     */
    private String            patient;

    /**
     * The id of this Bloodlevel daily entry
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private long              id;

    /**
     * the fasting amount
     */
    @Max ( 500 )
    @Min ( 0 )
    private float             fasting;

    /**
     * the bs level for meal1
     */
    @Max ( 500 )
    @Min ( 0 )
    private float             meal1;

    /**
     * the bs level for meal2
     */
    @Max ( 500 )
    @Min ( 0 )
    private float             meal2;

    /**
     * the bs level for meal3
     */
    @Max ( 500 )
    @Min ( 0 )
    private float             meal3;

    // Supporting methods: ---------------------------------------------
    /**
     * default constructor
     */
    public DailyBloodSugarLevelEntry () {
        // empty space
    }

    /**
     * use form to construct
     *
     * @param dbsf
     *            the form to construct
     */
    public DailyBloodSugarLevelEntry ( final DailyBloodSugarLevelForm dbsf ) {
        setDate( LocalDate.parse( dbsf.getDate() ) );
        // setPatient( dbsf.getPatient() );
        // setHCP( dbsf.getHCP() );
        setFasting( dbsf.getFasting() );
        setMeal1( dbsf.getMeal1() );
        setMeal2( dbsf.getMeal2() );
        setMeal3( dbsf.getMeal3() );
    }

    /**
     * return the date
     *
     * @return the date
     */
    public String getDate () {
        return date.toString();
    }

    /**
     * the date to set
     *
     * @param date
     *            the date to set
     */
    public void setDate ( final LocalDate date ) {
        if ( date.isAfter( LocalDate.now() ) ) {
            throw new IllegalArgumentException( "Date must be before current date" );
        }
        this.date = date;
    }

    /**
     * return the patient
     *
     * @return the patient
     */
    public String getPatient () {
        return patient;
    }

    /**
     * the patient to set
     *
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    /**
     * return the fasting
     *
     * @return the fasting
     */
    public float getFasting () {
        return fasting;
    }

    /**
     * fasting the fasting to set
     *
     * @param fasting
     *            the fasting to set
     */
    public void setFasting ( final float fasting ) {
        if ( fasting < 0 || fasting > 500 ) {
            throw new IllegalArgumentException( "fasting must be a positive integer 0-500" );
        }
        this.fasting = fasting;
    }

    /**
     * return the meal1
     *
     * @return the meal1
     */
    public float getMeal1 () {
        return meal1;
    }

    /**
     * meal1 the meal1 to set
     *
     * @param meal1
     *            the meal1 to set
     */
    public void setMeal1 ( final float meal1 ) {
        if ( meal1 < 0 || meal1 > 500 ) {
            throw new IllegalArgumentException( "meal1 must be a positive integer 0-500" );
        }
        this.meal1 = meal1;
    }

    /**
     * return the meal2
     *
     * @return the meal2
     */
    public float getMeal2 () {
        return meal2;
    }

    /**
     * meal2 the meal2 to set
     *
     * @param meal2
     *            the meal2 to set
     */
    public void setMeal2 ( final float meal2 ) {
        if ( meal2 < 0 || meal2 > 500 ) {
            throw new IllegalArgumentException( "meal2 must be a positive integer 0-500" );
        }
        this.meal2 = meal2;
    }

    /**
     * return the meal3
     *
     * @return the meal3
     */
    public float getMeal3 () {
        return meal3;
    }

    /**
     * meal3 the meal3 to set
     *
     * @param meal3
     *            the meal3 to set
     */
    public void setMeal3 ( final float meal3 ) {
        if ( meal3 < 0 || meal3 > 500 ) {
            throw new IllegalArgumentException( "meal3 must be a positive integer 0-500" );
        }
        this.meal3 = meal3;
    }

    /**
     * Get the ID of this DiaryEntry
     *
     * @return the ID of this DiaryEntry
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the id of this DiaryEntry
     *
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

}
