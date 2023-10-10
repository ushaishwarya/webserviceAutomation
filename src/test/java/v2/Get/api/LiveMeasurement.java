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

		
		RestAssured.baseURI=Credentails.baseurl;
		
		Response response= RestAssured.given()
		
			.header("systemid",Credentails.systemid)
			.header("userid",Credentails.userid)
			
		.when()
			.get("v2/measure");
					
		verify_key_and_value_for_LiveMeasurement(response);
		

	}
	@Test
	public void Unauthorized() {
    RestAssured.baseURI=Credentails.baseurl;

    Response response = RestAssured
        .given()
		.header("systemid","")
		.header("userid","")
        .when()
        .get("v2/measure")
        .then()
        .extract()
        .response();
    
    
    assertMessageAndStatuscode(response, "Unauthorized!", 401);

	}

	


}
