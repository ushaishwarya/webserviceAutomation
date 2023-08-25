package v2.Get.api;

import org.testng.annotations.Test;


import org.testng.Assert;
import org.json.JSONObject;

import credentails.Credentails;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LatestMeasurementImage {
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
    
    int statuscode=response.getStatusCode();
    Assert.assertEquals(statuscode, 200);

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
    
    int statuscode=response.getStatusCode();
    Assert.assertEquals(statuscode, 401);
    
    JSONObject jsonResponse = new JSONObject(response.getBody().asString());
    String message = jsonResponse.getString("message");
    String excepted_message="Unauthorized!";
    Assert.assertEquals(excepted_message, message);

	}


}
