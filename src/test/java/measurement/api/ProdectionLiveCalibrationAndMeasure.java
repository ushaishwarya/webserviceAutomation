package measurement.api;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.Gson;

import credentails.Credentails;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ProdectionLiveCalibrationAndMeasure{

	public static String accesstoken;

	public static String userid="520c1e79-3663-4db1-97a4-a8393cadba83";
	public static String secretid="1751623a-0cee-40ba-ba13-5648c71425af";
	public static String systemid="1684524307039d3s98zg70HxaPzI-K0sj11VqiyS";
	
	public static String heightPref="1.1";
	public static String envelopeMod="false";
	public static String referenceboxsku="suzume";
	
	public static String baseurl="https://forge.vmeasure.ai:6600/api/";
	
	
	public static String auth() {
		
		RestAssured.baseURI =baseurl;
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("user_id", userid);
		requestBody.put("secret_id",secretid);


		Gson gson = new Gson();
		String jsonBody = gson.toJson(requestBody);


		Response response = given()

				.contentType(ContentType.JSON)
				.body(jsonBody)


				.when()
				.post("/v3/auth");

		accesstoken = response.jsonPath().getString("access_token");
		return  accesstoken;

	}
	
	public static void calibrationAssert(Response response) {

		//Assert Key
		String exceptedkeystore=response.asPrettyString();
		
		String[] exceptedkey= {"id","statusCode","height","angle","calibratedOn","status","dimensionUnit","additionalInfo"};
		for(String key:exceptedkey) {
			assertTrue(exceptedkeystore.contains(key),"Key"+key+"key does not get in Live calibration response");
		}
		
		//Extract value from key
		JsonPath jsonPath = response.jsonPath();
		
		int responsestatuscode=response.getStatusCode();

		
		String id=jsonPath.getString("id");
			String additionalinfo=jsonPath.getString("additionalInfo");
				Integer livecalibrationstatus=jsonPath.getInt("statusCode");
				
		//Assert excepted and actual value
		Assert.assertEquals(responsestatuscode, 200);
			Assert.assertEquals(additionalinfo, "System Calibrated Successfully.");
				Assert.assertEquals(livecalibrationstatus, 300);

		//To Print
		System.out.println("calibration id "+id);
		System.out.println("calibration Message"+additionalinfo);


	}
	public static void measurementAssert(Response response) {

		//Assert key
		String exceptedkeystore=response.asPrettyString();
		String[] exceptedkey= {"id","scannedOn","status","statusCode","dimensionUnit","weightUnit","length","width","height","actualWeight","auxiliaryActualWeight","additionalInfo","volumetricWeight","auxiliaryVolumetricWeight","realVolume","cubicVolume","remainingMeasurementCheckCount"};
		
		for(String key:exceptedkey) {
			assertTrue(exceptedkeystore.contains(key),"keys"+ key +"key does not get in Live Measurement response");
			System.out.println(key);

		}

		//Extract value from key
		JsonPath jsonPath = response.jsonPath();
		
		int responsestatuscode=response.getStatusCode();
			String id=jsonPath.getString("id");
				String additionalinfo=jsonPath.getString("additionalInfo");
					boolean statuspassorfail=jsonPath.getBoolean("status");
						Integer measurementstatus=jsonPath.getInt("statusCode");


		//Assert excepted and actual value
//		Assert.assertEquals(responsestatuscode, 200);
//			Assert.assertEquals(additionalinfo, "Object Measured Successfully");
//				Assert.assertEquals(statuspassorfail, true);
//					Assert.assertEquals(measurementstatus, 400);

		//To Print
		System.out.println("Measurement id "+id);
			System.out.println("Measurement info"+additionalinfo);


	}

	public static void v1_calibration () throws InterruptedException {
        Thread.sleep(3000); 

		RestAssured.baseURI=baseurl;
		Response response= RestAssured.given()
			.header("systemid",systemid)
			.header("userid",userid)
			
			.queryParam("heightPref",heightPref)
			.queryParam("unitPref","m")
			.queryParam("envelopeMod",envelopeMod)
			
		.when()
			.get("v1/calibrate");
		
		
		calibrationAssert(response); 
		
		//sysout is used for debug process
		//System.out.println(response.asPrettyString());
		
	}
	public static void v1_measurements() throws InterruptedException {
		
        Thread.sleep(3000); 

		
		RestAssured.baseURI=baseurl;
		
		Response response= RestAssured.given()
		
			.header("systemid",systemid)
			.header("userid",userid)
			
		.when()
			.get("v1/measure");
		
		measurementAssert(response);
		//sysout is used for debug process

		//System.out.println(response.asPrettyString());

	}
	
	public static void v2_calibration () throws InterruptedException {
        Thread.sleep(3000); // Time is in milliseconds

		RestAssured.baseURI=baseurl;
		Response response= RestAssured.given()
			.header("systemid",systemid)
			.header("userid",userid)
			
			.queryParam("heightPref",heightPref)
			.queryParam("unitPref","m")
			.queryParam("envelopeMod",envelopeMod)
			
		.when()
			.get("v2/calibrate");
		
		
		calibrationAssert(response); 
		//sysout is used for debug process
		//System.out.println(response.asPrettyString());

		
	}
	public static void v2_measurements() throws InterruptedException {
		
        Thread.sleep(3000); 

		
		RestAssured.baseURI=baseurl;
		
		Response response= RestAssured.given()
		
			.header("systemid",systemid)
			.header("userid",userid)
			
		.when()
			.get("v2/measure");
		
		measurementAssert(response);
		
		//sysout is used for debug process
		
		//System.out.println(response.asPrettyString());

	}
	
	public static void v3_calibration () throws InterruptedException {
        Thread.sleep(3000); 

		RestAssured.baseURI=baseurl;
		Response response= RestAssured.given()
	    		.header("System-Token",systemid)
	    		.header("Authorization","Bearer "+accesstoken)
			
			.queryParam("heightPref",heightPref)
			.queryParam("unitPref","m")
			.queryParam("envelopeMod",envelopeMod)
			
		.when()
			.get("v3/calibrate");
		
		
       calibrationAssert(response); 
		//sysout is used for debug process

//		System.out.println(response.asPrettyString());


		
	}
	@Test(priority=6)
	public static void v3_measurements() throws InterruptedException  {
		
        Thread.sleep(3000); 

		RestAssured.baseURI=baseurl;
		
		Response response= RestAssured.given()
		
	    		.header("System-Token",systemid)
	    		.header("Authorization","Bearer "+accesstoken)
			
		.when()
			.get("v3/measure");
			
		measurementAssert(response);
		
		//sysout is used for debug process

//		System.out.println(response.asPrettyString());

        


	}
	@Test(priority=7)
    public void liveMeasurementCheck200status() {

	
    RestAssured.baseURI =Credentails.baseurl;
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("sku",referenceboxsku);


    Gson gson = new Gson();
    String jsonBody = gson.toJson(requestBody);
    

    Response response = given()
    		
    		.contentType(ContentType.JSON)
    		.body(jsonBody)
    		.header("System-Token",systemid)
    		.header("Authorization","Bearer "+accesstoken)

        
        
       .when()
        .post("v3/measurement-check");
    
    System.out.println(response.asPrettyString());
    
	}



}
