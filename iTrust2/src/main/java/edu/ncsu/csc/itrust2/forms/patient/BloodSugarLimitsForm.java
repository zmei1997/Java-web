package edu.ncsu.csc.itrust2.forms.patient;

import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.models.persistent.BloodSugarLimits;

public class BloodSugarLimitsForm {

    /**
     * The upper bound of blood sugar level for fasting
     */
    @NotEmpty
    private float upperBoundFasting;

    /**
     * The upper bound of blood sugar level for meal
     */
    @NotEmpty
    private float upperBoundMeal;

    @NotEmpty
    private String patient;
    
    /**
     * the empty constructor
     */
    public BloodSugarLimitsForm () {
    }

    /**
     * use limit to create form
     *
     * @param bsl
     *            the limit used
     */
    public BloodSugarLimitsForm ( final BloodSugarLimits bsl ) {
        setUpperBoundFasting( bsl.getUpperBoundFasting() );
        setUpperBoundMeal( bsl.getUpperBoundMeal() );
        setPatient( bsl.getPatient() );
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
        this.upperBoundMeal = upperBoundMeal;
    }

    /**
     * Set patient for limits form
     * @param string patient name
     */
    public void setPatient ( String string ) {
        this.patient = string;
        
    }
    /**
     * Return patient name
     * @return patient name 
     */
    public String getPatient () {
        return patient;
    }

}
