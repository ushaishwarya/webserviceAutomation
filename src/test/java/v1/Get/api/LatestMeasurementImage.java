package v1.Get.api;

import org.testng.annotations.Test;


import org.testng.Assert;

import credentails.Credentails;
import credentails.CommonMethods;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LatestMeasurementImage extends CommonMethods{
	@Test
	public void Latestmeasurementimage() {
    RestAssured.baseURI=Credentails.baseurl;

    Response response = RestAssured
    		
        .given()
        		.header("systemid", Credentails.systemid)
        			.header("userid", Credentails.userid)
        .when()
        	.get("v1/image")
        .then()
        		.extract()
        			.response();
    
    Assert.assertEquals(response.getStatusCode(), 200);

	}
	
	@Test
	public void Unauthorized() {
    RestAssured.baseURI=Credentails.baseurl;

    Response response = RestAssured
    		
        .given()
        		.header("systemid", "")
        				.header("userid", "")
        .when()
        		.get("v1/image")
        .then()
        		.extract()
        			.response();
    
    
    assertMessageAndStatuscode(response, "Unauthorized!", 401);

	}
	
	@Test
	public void notFound() {
    RestAssured.baseURI=Credentails.baseurl;

    Response response = RestAssured
    		
        .given()
        		.header("systemid", Credentails.systemid)
        			.header("userid", Credentails.userid)
        .when()
        	.get("v1/ima")
        .then()
        		.extract()
        			.response();
    
    assertMessageAndStatuscode(response, "Not found", 404);

	}

}
