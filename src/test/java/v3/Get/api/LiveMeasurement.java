package v3.Get.api;

import org.testng.annotations.Test;

import credentails.CommonMethods;
import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;
public class LiveMeasurement extends CommonMethods {
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];

	@Test
	public static void measurements() throws InterruptedException {
        Thread.sleep(3000); // Time is in milliseconds

		
		
		RestAssured.baseURI=Credentails.baseurl;
		
		Response response= RestAssured.given()
		
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
			
		.when()
			.get("v3/measure");
			
		verify_key_and_value_for_LiveMeasurement(response);
        


	}
	
	@Test
	public void Unauthorized() {
    RestAssured.baseURI=Credentails.baseurl;

    Response response = RestAssured
        .given()
		.header("Content-Type","application/json")
		.header("System-Token","")
		.header("Authorization","Bearer "+"")
        .when()
        .get("v3/measure")
        .then()
        .extract()
        .response();
    
    
    assertMessageAndStatuscode(response, "Unauthorized!", 401);

	}




}
