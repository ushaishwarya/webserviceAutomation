package measurement.api;

import org.testng.annotations.Test;

import credentails.CommonMethods;
import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ProdectionLiveCalibrationAndMeasure extends CommonMethods{
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];

	@Test
	public static void CalibratedSuccessfully () throws InterruptedException {
        Thread.sleep(3000); // Time is in milliseconds

		RestAssured.baseURI=Credentails.v1;
		Response response= RestAssured.given()
			.header("systemid",Credentails.systemid)
			.header("userid",Credentails.userid)
			
			.queryParam("heightPref",Credentails.heightPref)
			.queryParam("unitPref","m")
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("/calibrate");
		
		
		calibrationAssert(response); 
		
	}
	@Test
	public static void measurements() throws InterruptedException {
		
        Thread.sleep(3000); 

		
		RestAssured.baseURI=Credentails.v1;
		
		Response response= RestAssured.given()
		
			.header("systemid",Credentails.systemid)
			.header("userid",Credentails.userid)
			
		.when()
			.get("/measure");
		
		measurementAssert(response);
	}
	
	@Test
	public static void CalibratedSuccessfullyForV2 () throws InterruptedException {
        Thread.sleep(3000); // Time is in milliseconds

		RestAssured.baseURI=Credentails.v2;
		Response response= RestAssured.given()
			.header("systemid",Credentails.systemid)
			.header("userid",Credentails.userid)
			
			.queryParam("heightPref",Credentails.heightPref)
			.queryParam("unitPref","m")
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("/calibrate");
		
		
		calibrationAssert(response); 
		
	}
	@Test
	public static void measurementsforV2() throws InterruptedException {
		
        Thread.sleep(3000); 

		
		RestAssured.baseURI=Credentails.v2;
		
		Response response= RestAssured.given()
		
			.header("systemid",Credentails.systemid)
			.header("userid",Credentails.userid)
			
		.when()
			.get("/measure");
		
		measurementAssert(response);
	}
	
	@Test
	public static void CalibratedSuccessfullyforV3 () throws InterruptedException {
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
		
		
       calibrationAssert(response);        

		
	}
	@Test
	public static void measurementsforV3() throws InterruptedException {
        Thread.sleep(3000); // Time is in milliseconds

		
		
		RestAssured.baseURI=Credentails.v3;
		
		Response response= RestAssured.given()
		
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
			
		.when()
			.get("/measure");
			
		measurementAssert(response);
        


	}



}
