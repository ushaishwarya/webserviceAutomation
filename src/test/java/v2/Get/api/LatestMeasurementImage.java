package v2.Get.api;

import org.testng.annotations.Test;



import org.testng.Assert;

import credentails.CommonMethods;
import credentails.Credentails;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LatestMeasurementImage extends CommonMethods {
	@Test
	public void Latestmeasurementimage() {
    RestAssured.baseURI=Credentails.v2;

    Response response = RestAssured
        .given()
        .header("systemid", Credentails.systemid)
        .header("userid", Credentails.userid)
        .when()
        .get("/image")
        .then()
        .extract()
        .response();
    
    Assert.assertEquals(response.getStatusCode(), 200);

	}
	
	@Test
	public void Unauthorized() {
    RestAssured.baseURI=Credentails.v2;

    Response response = RestAssured
        .given()
        .header("systemid", "")
        .header("userid", "")
        .when()
        .get("/image")
        .then()
        .extract()
        .response();
    

    assertMessageAndStatuscode(response, "Unauthorized!", 401);
	}
	
	@Test
	public void notFound() {
    RestAssured.baseURI=Credentails.v2;

    Response response = RestAssured
    		
        .given()
        		.header("systemid", Credentails.systemid)
        			.header("userid", Credentails.userid)
        .when()
        	.get("/ima")
        .then()
        		.extract()
        			.response();
    
    assertMessageAndStatuscode(response, "Not found", 404);

	}



}
