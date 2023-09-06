package v2.Get.api;


import org.testng.annotations.Test;

import credentails.CommonMethods;
import credentails.Credentails;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LiveMeasurement extends CommonMethods {
	@Test
	public static void measurements() throws InterruptedException {
        Thread.sleep(3000); // Time is in milliseconds

		
		RestAssured.baseURI=Credentails.v2;
		
		Response response= RestAssured.given()
		
			.header("systemid",Credentails.systemid)
			.header("userid",Credentails.userid)
			
		.when()
			.get("/measure");
			
		measurementAssert(response);

	}
	@Test
	public void Unauthorized() {
    RestAssured.baseURI=Credentails.v2;

    Response response = RestAssured
        .given()
		.header("systemid","")
		.header("userid","")
        .when()
        .get("/measure")
        .then()
        .extract()
        .response();
    
    
    assertMessageAndStatuscode(response, "Unauthorized!", 401);

	}

	


}
