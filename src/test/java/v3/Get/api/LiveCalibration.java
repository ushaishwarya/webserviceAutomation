package v3.Get.api;

import org.testng.Assert;

import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.CommonMethods;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class LiveCalibration extends CommonMethods{
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];

	@Test
	public static void CalibratedSuccessfully () throws InterruptedException {
        Thread.sleep(3000); 

		RestAssured.baseURI=Credentails.v3;
		Response response= RestAssured.given()
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
			
			.queryParam("heightPref",Credentails.heightPref)
			.queryParam("unitPref","m")
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("/calibrate");
		
		
        JsonPath jsonPath = response.jsonPath();
        
        String additionalinfo=jsonPath.getString("additionalInfo");
        System.out.println("v3 calibration "+additionalinfo);

        
        Integer status=jsonPath.getInt("statusCode");
        
        Assert.assertEquals(additionalinfo, "System Calibrated Successfully.");
        
        Assert.assertEquals(status, 300);
        
        int statuscode=response.getStatusCode();
        
        Assert.assertEquals(statuscode, 200);
        


		
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
		
		RestAssured.baseURI=Credentails.v3;
		Response response= RestAssured.given()
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
			
			.queryParam("heightPref",heightPref)
			.queryParam("unitPref",unitpref)
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("/calibrate");
		
        
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
		
		RestAssured.baseURI=Credentails.v3;
		Response response= RestAssured.given()
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
			
			.queryParam("heightPref",heightPref)
			.queryParam("unitPref","meter")
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("/calibrate");
		
        
        assertMessageAndStatuscode(response, "Enter valid heightPref either 1.1, 1.5 or 2.2", 400);
        
		
	}

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
        .get("/calibrate")
        .then()
        .extract()
        .response();
    
    
    assertMessageAndStatuscode(response, "Unauthorized!", 401);

	}




}
