package v3.Get.api;

import org.testng.Assert;
import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class LiveMeasurement {
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];

	@Test
	public static void measurements() throws InterruptedException {
        Thread.sleep(3000); // Time is in milliseconds

		
		
		RestAssured.baseURI=Credentails.v3;
		
		Response response= RestAssured.given()
		
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
			
		.when()
			.get("/measure");
			
        JsonPath jsonPath = response.jsonPath();
        
        String additionalinfo=jsonPath.getString("additionalInfo");
        
        System.out.println("v3 measurement "+additionalinfo);

        Assert.assertEquals(additionalinfo, "Object Measured Successfully");
        
        Integer status=jsonPath.getInt("statusCode");
        Assert.assertEquals(status, 400);
        
        boolean statustorf=jsonPath.getBoolean("status");
        Assert.assertEquals(statustorf, true);


        int statuscode=response.getStatusCode();
        Assert.assertEquals(statuscode, 200);
        


	}


}
