package v3.Get.api;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class LiveCalibration {
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];

	@Test
	@Parameters({"heightPref","envelopeMod"})
	public static void CalibratedSuccessfully (String heightPref,String envelopeMod) throws InterruptedException {
        Thread.sleep(3000); // Time is in milliseconds

		RestAssured.baseURI=Credentails.v3;
		Response response= RestAssured.given()
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
			
			.queryParam("heightPref",heightPref)
			.queryParam("unitPref","m")
			.queryParam("envelopeMod",envelopeMod)
			
		.when()
			.get("/calibrate");
		
//		System.out.println(response.asPrettyString());
		
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
	@Parameters("envelopeMod")
	public static void badrequest(String envelopeMod) {
		
	
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
			.queryParam("envelopeMod",envelopeMod)
			
		.when()
			.get("/calibrate");
		
//		System.out.println(response.asPrettyString());
		
        JsonPath jsonPath = response.jsonPath();
        
        String additionalinfo=jsonPath.getString("message");
        
        Assert.assertEquals(additionalinfo, "Please send the preferable height and unit");
        
        int statuscode=response.getStatusCode();
        
        Assert.assertEquals(statuscode, 400);
        
		
	}
}
	@Test
	@Parameters("envelopeMod")
	public static void invalidheight(String envelopeMod) {
		
	
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
			.queryParam("envelopeMod",envelopeMod)
			
		.when()
			.get("/calibrate");
		
//		System.out.println(response.asPrettyString());
		
        JsonPath jsonPath = response.jsonPath();
        
        String additionalinfo=jsonPath.getString("message");
        
        Assert.assertEquals(additionalinfo, "Enter valid heightPref either 1.1, 1.5 or 2.2");
        
        int statuscode=response.getStatusCode();
        
        Assert.assertEquals(statuscode, 400);
        
		
	}

	}


}
