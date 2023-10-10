package v2.Get.api;


import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.CommonMethods;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class MeasurementImagebyID extends CommonMethods {
    @Test(priority=1)
    public void VeridyTheIdByImage() {
    	
        RestAssured.baseURI = Credentails.baseurl;
        
        Response response = RestAssured
        		
                .given()
                	.pathParam("id", Credentails.ImageId)
                			.header("systemid", Credentails.systemid)
                					.header("userid", Credentails.userid)
                .when()
                	.get("v2/image/{id}")
                .then()
                	.extract()
                		.response();
        
        extractValuesFromImage(response);
    }
    @Test(priority=2)

    public void testNoImageFound() {
        
        RestAssured.baseURI = Credentails.baseurl;
        
        Response response = RestAssured
        		
                .given()
                	.pathParam("id",Credentails.InvalidImageId)
                		.header("systemid", Credentails.systemid)
                				.header("userid", Credentails.userid)
                .when()
                	.get("v2/image/{id}")
                .then()
                	.extract()
                		.response();

        imageIdAssert(response);        
    }
    @Test(priority=3)

	public void Unauthorized() {

    RestAssured.baseURI = Credentails.baseurl;
    
    Response response = RestAssured
    		
            .given()
            
            	.pathParam("id", Credentails.ImageId)
            		.header("systemid", "")
            				.header("userid", "")
            .when()
            	.get("v2/image/{id}")
            .then()
            	.extract()
            		.response();
    
    assertMessageAndStatuscode(response, "Unauthorized!", 401);


	}

}
