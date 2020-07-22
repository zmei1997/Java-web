package edu.ncsu.csc.itrust2.forms.patient;

import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.models.persistent.DailyBloodSugarLevelEntry;

/**
 * the daily blood sugar level form
 *
 * @author sliu20
 *
 */
public class DailyBloodSugarLevelForm {

    /**
     * Empty constructor for the user to fill out
     */
    public DailyBloodSugarLevelForm () {
    }

    /**
     * Constructor that will read an entry and transfer it into a form format
     *
     * @param dbe
     *            the entry used
     */
    public DailyBloodSugarLevelForm ( final DailyBloodSugarLevelEntry dbe ) {
        setDate( dbe.getDate() );
        setFasting( dbe.getFasting() );
        setMeal1( dbe.getMeal1() );
        setMeal2( dbe.getMeal2() );
        setMeal3( dbe.getMeal3() );
        setExceeded( false );
    }
    /**
     * Constructor for read a csv file
     * @param string date read from csv
     * @param string2 fasting data read from csv
     * @param string3 meal breakfast  data read from csv
     * @param string4 meal lunch data read from csv
     * @param string5 meal dinner data read from csv
     */
    public DailyBloodSugarLevelForm ( String string, String string2, String string3, String string4, String string5 ) {
        setDate(string);
        setFasting( Float.parseFloat(string2) );
        setMeal1(  Float.parseFloat(string3) );
        setMeal2(  Float.parseFloat(string4) );
        setMeal3(  Float.parseFloat(string5) );
        setExceeded( false );
    }

    /**
     * The date as milliseconds since epoch for the entry
     */
    private String  date;

    /**
     * The amount of blood sugar level in fasting
     */
    @NotEmpty
    private float   fasting;

    /**
     * The amount of blood sugar level for meal 1
     */
    @NotEmpty
    private float   meal1;

    /**
     * The amount of blood sugar level for meal 2
     */
    @NotEmpty
    private float   meal2;

    /**
     * The amount of blood sugar level for meal 3
     */
    @NotEmpty
    private float   meal3;

    /**
     * The upper bound of blood sugar level for meal
     */
    private boolean isExceeded;

    /**
     * return the date
     *
     * @return the date
     */
    public String getDate () {
        return date;
    }

    /**
     * date the date to set
     *
     * @param date
     *            the date to set
     */
    public void setDate ( final String date ) {
        this.date = date;
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
        this.meal3 = meal3;
    }

    /**
     * return the isExceeded
     *
     * @return the isExceeded
     */
    public boolean isExceeded () {
        return isExceeded;
    }

    /**
     * * isExceeded the isExceeded to set
     *
     * @param exceeded
     *            the exceeded
     */
    public void setExceeded ( final boolean exceeded ) {
        this.isExceeded = exceeded;
    }

}
