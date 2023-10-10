package v3.Get.api;


import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.CommonMethods;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class MeasurementImagebyID extends CommonMethods{
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];

    @Test(priority=1)
    public void idByImage() {
    	
        RestAssured.baseURI = Credentails.baseurl;
        
        Response response = RestAssured
        		
                .given()
                	.pathParam("id",Credentails.ImageId)
                		.header("System-Token",Credentails.systemid)
                				.header("Authorization","Bearer "+accessToken)
                .when()
                	.get("v3/image/{id}")
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
                	.pathParam("id", Credentails.InvalidImageId)
            			.header("System-Token",Credentails.systemid)
            				.header("Authorization","Bearer "+accessToken)
                .when()
                	.get("v3/image/{id}")
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
            	.pathParam("id", Credentails.InvalidImageId)
            			.header("System-Token","")
            					.header("Authorization","Bearer "+"")
            .when()
            		.get("v3/image/{id}")
            .then()
            	.extract()
            		.response();
    
    assertMessageAndStatuscode(response, "Unauthorized!", 401);

	}


}
