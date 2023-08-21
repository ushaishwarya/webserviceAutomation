package v3.Get.api;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.json.JSONObject;


import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Ignore;
@Ignore


public class LatestMeasurementImage {
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];

	@Test
	public void Latestmeasurementimage() {
    RestAssured.baseURI=Credentails.v3;

    Response response = RestAssured
        .given()
		.header("Content-Type","application/json")
		.header("System-Token",Credentails.systemid)
		.header("Authorization","Bearer "+accessToken)
        .when()
        .get("/image")
        .then()
        .extract()
        .response();
    
    int statuscode=response.getStatusCode();
    Assert.assertEquals(statuscode, 200);

	}
	
	@Test
	public void Unauthorized() {
    RestAssured.baseURI=Credentails.v3;

    Response response = RestAssured
        .given()
		.header("Content-Type","application/json")
		.header("System-Token","")
		.header("Authorization","Bearer "+"")
        .when()
        .get("/image")
        .then()
        .extract()
        .response();
    
    int statuscode=response.getStatusCode();
    Assert.assertEquals(statuscode, 401);
    
    JSONObject jsonResponse = new JSONObject(response.getBody().asString());
    String message = jsonResponse.getString("message");
    String excepted_message="Unauthorized!";
    Assert.assertEquals(excepted_message, message);

	}


}
