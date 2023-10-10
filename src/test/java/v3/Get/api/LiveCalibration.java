package v3.Get.api;



import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.CommonMethods;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LiveCalibration extends CommonMethods{
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];

	@Test
	public static void CalibratedSuccessfully () throws InterruptedException {
        Thread.sleep(3000); 

		RestAssured.baseURI=Credentails.baseurl;
		Response response= RestAssured.given()
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
			
			.queryParam("heightPref",Credentails.heightPref)
			.queryParam("unitPref","m")
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("v3/calibrate");
		
		
		verify_key_and_value_for_LiveCalibration(response);        


		
	}
	@Test
	public static void badrequest() {
		
	
        for (int i = 1; i < 2; i++) {
        	
    		String heightPref="";
    		String unitpref="";

            if (i == 1) {
            	unitpref = "";
            } else if (i == 2) {
            	unitpref="wdsse";
            	
           }
		
		RestAssured.baseURI=Credentails.baseurl;
		Response response= RestAssured.given()
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
			
			.queryParam("heightPref",heightPref)
			.queryParam("unitPref",unitpref)
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("v3/calibrate");
		
        
        assertMessageAndStatuscode(response, "Please send the preferable height and unit", 400);

		
	}
}
	@Test
	public static void invalidheight() {
		
	
        for (int i = 1; i < 2; i++) {
        	
    		String heightPref="";

            if (i == 1) {
            	heightPref = "1222";
            	
           }
		
		RestAssured.baseURI=Credentails.baseurl;
		Response response= RestAssured.given()
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
			
			.queryParam("heightPref",heightPref)
			.queryParam("unitPref","meter")
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("v3/calibrate");
		
        
        assertMessageAndStatuscode(response, "Enter valid heightPref either 1.1, 1.5 or 2.2", 400);
        
		
	}

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
        .get("v3/calibrate")
        .then()
        .extract()
        .response();
    
    
    assertMessageAndStatuscode(response, "Unauthorized!", 401);

	}




}
