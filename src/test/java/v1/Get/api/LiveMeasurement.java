package v1.Get.api;

import org.testng.Assert;
import org.testng.annotations.Test;

import credentails.Credentails;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class LiveMeasurement {
	@Test
	public static void measurements() throws InterruptedException {
		
        Thread.sleep(3000); // Time is in milliseconds

		
		RestAssured.baseURI=Credentails.v1;
		
		Response response= RestAssured.given()
		
			.header("systemid",Credentails.systemid)
			.header("userid",Credentails.userid)
			
		.when()
			.get("/measure");
			
        JsonPath jsonPath = response.jsonPath();
        
        String additionalinfo=jsonPath.getString("additionalInfo");
        System.out.println("v1 measurement "+additionalinfo);

        Assert.assertEquals(additionalinfo, "Object Measured Successfully");
        
        
        Integer status=jsonPath.getInt("statusCode");
        Assert.assertEquals(status, 400);
        
        boolean statustorf=jsonPath.getBoolean("status");
        Assert.assertEquals(statustorf, true);


        int statuscode=response.getStatusCode();
        Assert.assertEquals(statuscode, 200);
        
	}

}
