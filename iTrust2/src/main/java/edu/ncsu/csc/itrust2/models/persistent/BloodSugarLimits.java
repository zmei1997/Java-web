package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.criterion.Criterion;

import edu.ncsu.csc.itrust2.forms.patient.BloodSugarLimitsForm;

/**
 * Blood Sugar Limits
 *
 * @author Xianzhen Huang
 * @author Sichen Liu
 * @author Zhongxiao Mei
 *
 */
@Entity
@Table ( name = "BloodSugarLimits" )
public class BloodSugarLimits extends DomainObject<DailyBloodSugarLevelEntry> implements Serializable {

    /**
     * the static method to get entry by id
     *
     * @param id
     *            the id in database
     * @return return the daily sugar entry
     */
    public static BloodSugarLimits getById ( final Long id ) {
        try {
            return (BloodSugarLimits) getWhere( BloodSugarLimits.class, eqList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * get all entry by patient name
     *
     * @param patientName
     *            the username of the patient
     * @return return the list of entry
     */
    @SuppressWarnings ( "unchecked" )
    public static BloodSugarLimits getByPatient ( final String patientName ) {
        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "patient", patientName ) );

        final List<BloodSugarLimits> list = (List<BloodSugarLimits>) getWhere( BloodSugarLimits.class, criteria );
        if ( list.size() == 0 ) {
            throw new IllegalArgumentException( "no limit" );
        }
        return list.get( 0 );
    }

    /**
     * generated uid
     */
    private static final long serialVersionUID = 2763517634016680820L;

    /**
     * patient name
     */
    private String            patient;

    /**
     * The upper bound of blood sugar level for fasting
     */
    @Max ( 130 )
    @Min ( 80 )
    private float             upperBoundFasting;

    /**
     * The upper bound of blood sugar level for meal
     */
    @Max ( 180 )
    @Min ( 120 )
    private float             upperBoundMeal;

    /**
     * The id of this Bloodlevel daily entry
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private long              id;

    /**
     * create empty object
     */
    public BloodSugarLimits () {
    }

    /**
     * construct with form
     *
     * @param bsf
     *            the form used for construct
     */
    public BloodSugarLimits ( final BloodSugarLimitsForm bsf ) {
        setUpperBoundFasting( bsf.getUpperBoundFasting() );
        setUpperBoundMeal( bsf.getUpperBoundMeal() );
    }

    /**
     * update the limit by form
     *
     * @param bsf
     *            the form used to update
     */
    public void update ( final BloodSugarLimitsForm bsf ) {
        setUpperBoundFasting( bsf.getUpperBoundFasting() );
        setUpperBoundMeal( bsf.getUpperBoundMeal() );
    }

    /**
     * return the upperBoundFasting
     *
     * @return the upperBoundFasting
     */
    public float getUpperBoundFasting () {
        return upperBoundFasting;
    }

    /**
     * upperBoundFasting the upperBoundFasting to set
     *
     * @param upperBoundFasting
     *            the upperBoundFasting to set
     */
    public void setUpperBoundFasting ( final float upperBoundFasting ) {
        if ( upperBoundFasting > 130.f || upperBoundFasting < 80.f ) {
            throw new IllegalArgumentException( "Upper Bound for Fasting should be between 80 and 130 mg/dl" );
        }
        this.upperBoundFasting = upperBoundFasting;
    }

    /**
     * return the upperBoundMeal
     *
     * @return the upperBoundMeal
     */
    public float getUpperBoundMeal () {
        return upperBoundMeal;
    }

    /**
     * upperBoundMeal the upperBoundMeal to set
     *
     * @param upperBoundMeal
     *            the upperBoundMeal to set
     */
    public void setUpperBoundMeal ( final float upperBoundMeal ) {
        if ( upperBoundMeal > 180.f || upperBoundMeal < 120.f ) {
            throw new IllegalArgumentException( "Upper Bound for meal should be between 120 and 180 mg/dl" );
        }
        this.upperBoundMeal = upperBoundMeal;
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

    /**
     * return the patient
     *
     * @return the patient
     */
    public String getPatient () {
        return patient;
    }

    /**
     * patient the patient to set
     *
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }
}
