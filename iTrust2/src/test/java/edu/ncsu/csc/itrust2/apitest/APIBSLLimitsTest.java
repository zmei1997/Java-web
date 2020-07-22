package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.patient.BloodSugarLimitsForm;
import edu.ncsu.csc.itrust2.models.persistent.BloodSugarLimits;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Test for the REST API functionality for interacting with Blood Sugar level
 * limits
 *
 * @author Xianzhen Huang(xhuang14)
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIBSLLimitsTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        try {
            final BloodSugarLimits bsl = BloodSugarLimits.getByPatient( "patient" );
            bsl.delete();

            final BloodSugarLimits bsl1 = BloodSugarLimits.getByPatient( "patient1" );
            bsl1.delete();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( e.getMessage(), "no limit" );
        }

    }

    /**
     * Tests API's post function
     *
     * @throws Exception
     *             throw exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testBSLAPIvalidEntry () throws Exception {

        try {
            @SuppressWarnings ( "unused" )
            final BloodSugarLimits bsl1 = BloodSugarLimits.getByPatient( "patient1" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( e.getMessage(), "no limit" );
        }

        mvc.perform( get( "/api/v1//BSLlimits/patient1" ) ).andExpect( status().isOk() );
        final BloodSugarLimits bsl1 = BloodSugarLimits.getByPatient( "patient1" );
        assertEquals( bsl1.getPatient(), "patient1" );

        // post
        final BloodSugarLimitsForm form = new BloodSugarLimitsForm();
        form.setUpperBoundFasting( 100 );
        form.setUpperBoundMeal( 150 );
        form.setPatient( "patient" );
        mvc.perform( post( "/api/v1//BSLlimits/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andExpect( status().isOk() );
        // passed

        // put
        form.setUpperBoundFasting( 120 );

        mvc.perform( put( "/api/v1//BSLlimits" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andExpect( status().isOk() );

        // get
        mvc.perform( get( "/api/v1//BSLlimits/patient" ) ).andExpect( status().isOk() );

        // check
        final BloodSugarLimits a = BloodSugarLimits.getByPatient( "patient" );

        assertEquals( 120, a.getUpperBoundFasting(), 0.01 );
        assertEquals( 150, a.getUpperBoundMeal(), 0.01 );

        // mvc.perform( get( "/api/v1/survey/patient" ) ).andExpect(
        // status().isBadRequest() );

    }

    /**
     * Tests API's post function
     *
     * @throws Exception
     *             throw exception
     */
    @Test
    @WithMockUser ( username = "patient", roles = { "PATIENT" } )
    public void testBSLAPIPatientEntry () throws Exception {
        mvc.perform( get( "/api/v1//BSLlimitsPatient" ) ).andExpect( status().isOk() );

    }

}
