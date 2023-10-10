package v3.Get.api;

import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.CommonMethods;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ListMeasurementIDandDates extends CommonMethods{
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];

    @Test
    public void testScanIdListEndpoint() {
    	
        RestAssured.baseURI = Credentails.baseurl;

        Response response = RestAssured
        		.given()
        				.header("System-Token",Credentails.systemid)
        						.header("Authorization","Bearer "+accessToken)
                .when()
                		.get("v3/scanidlist")
                .then()
                		.extract()
                			.response();
        
       assertIdAndDate(response);

    }
    @Test
	public void Unauthorized() {

	    RestAssured.baseURI = Credentails.baseurl;
	    
	    Response response = RestAssured
	    		
	            .given()
	    				.header("System-Token","")
	    						.header("Authorization","Bearer "+"")
	            .when()
                	.get("v3/scanidlist")
	            .then()
	            	.extract()
	            		.response();
	    
	    assertMessageAndStatuscode(response, "Unauthorized!", 401);

		}
    @Test
	public void Notfound() {

	    RestAssured.baseURI = Credentails.baseurl;
	    
	    Response response = RestAssured
	            .given()
	    				.header("System-Token",Credentails.systemid)
	    					.header("Authorization","Bearer "+accessToken)
	            .when()
                	.get("/")
	            .then()
	            	.extract()
	            		.response();
	    
	    assertMessageAndStatuscode(response, "Not found", 404);


		}



}
