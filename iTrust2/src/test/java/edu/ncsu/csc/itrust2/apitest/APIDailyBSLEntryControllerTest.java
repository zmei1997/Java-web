package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import edu.ncsu.csc.itrust2.forms.patient.DailyBloodSugarLevelForm;
import edu.ncsu.csc.itrust2.models.persistent.DailyBloodSugarLevelEntry;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Test for the REST API functionality for interacting with Blood Sugar level
 *
 * @author Zhihang Yuan
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIDailyBSLEntryControllerTest {

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
            final List<DailyBloodSugarLevelEntry> bsl = DailyBloodSugarLevelEntry.getByPatient( "patient" );
            for ( int i = 0; i < bsl.size(); i++ ) {
                bsl.get( i ).delete();
            }
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
    @WithMockUser ( username = "patient", roles = { "PATIENT" } )
    public void testBSLAPIvalidEntry () throws Exception {
        final DailyBloodSugarLevelForm form = new DailyBloodSugarLevelForm();
        // mvc.perform( get( "/api/v1//BSLdiary/2019-10-10/2019-10-12" )
        // ).andExpect( status().isOk() );
        form.setDate( "2019-10-10" );
        form.setMeal1( 100 );
        form.setFasting( 100 );
        form.setMeal2( 100 );
        form.setMeal3( 100 );
        mvc.perform( post( "/api/v1//BSLdiary" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andExpect( status().isOk() );

        // patient get
        mvc.perform( get( "/api/v1//BSLdiary/PatientAll" ) ).andExpect( status().isOk() );
        // more detailed test

        form.setDate( "2019-10-11" );
        form.setMeal1( 100 );
        form.setFasting( 100 );
        form.setMeal2( 100 );
        form.setMeal3( 100 );
        mvc.perform( post( "/api/v1//BSLdiary" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andExpect( status().isOk() );

        form.setDate( "2019-10-12" );
        form.setMeal1( 100 );
        form.setFasting( 100 );
        form.setMeal2( 100 );
        form.setMeal3( 100 );
        mvc.perform( post( "/api/v1//BSLdiary" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andExpect( status().isOk() );
        
        mvc.perform( get( "/api/v1//BSLdiary/2019-10-10/WEEK" ) ).andExpect( status().isOk() );
        
        // not test csv for now
        // final String path = "src/test/resources/testFiles/testingFile.csv";
        // final File file = new File( path );
        // mvc.perform( post( "/api/v1//BSLdiarycsv/patient" ).contentType(
        // MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( file ) ) ).andExpect(
        // status().isOk() );
        // mvc.perform( get( "/api/v1//BSLdiary/patient" ) ).andExpect(
        // status().isOk() );

        // test get API

        // =======
        // mvc.perform( get( "/api/v1//BSLdiary/patient" ) ).andExpect(
        // status().isOk() );
        // final String path = "src/test/resources/testFiles/testingFile.csv";
        // final File file = new File( path );
        // mvc.perform( post( "/api/v1//BSLdiarycsv" ).contentType(
        // MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( file ) ) ).andExpect(
        // status().isOk() );
        // mvc.perform( get( "/api/v1//BSLdiary/patient" ) ).andExpect(
        // status().isOk() );
        //
        // form.setFasting( -1.f );
        // mvc.perform( post( "/api/v1//BSLdiary" ).contentType(
        // MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( form ) ) ).andExpect(
        // status().isBadRequest() );
        // >>>>>>> refs/heads/Frontend-subteam1
    }

    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testBSLAPIvalidEntryHCP () throws Exception {
        // hcp get
        mvc.perform( get( "/api/v1//BSLdiary/HCPAll/patient" ) ).andExpect( status().isOk() );
        mvc.perform( get( "/api/v1//BSLdiary/patient/2019-10-10/2019-10-12" ) ).andExpect( status().isOk() );
        // get by time
        // mvc.perform( get( "/api/v1//BSLdiary/patient/2019-10-10/2019-10-12" )
        // ).andExpect( status().isOk() );

    }
}
