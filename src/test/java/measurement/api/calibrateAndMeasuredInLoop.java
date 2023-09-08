package measurement.api;

import java.util.Scanner;


import org.json.JSONObject;

import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.CommonMethods;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class calibrateAndMeasuredInLoop extends CommonMethods{
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];
	

	@Test(enabled = true)
	public static void CalibratedSuccessfully () throws InterruptedException {
        Thread.sleep(3000); // Time is in milliseconds

		RestAssured.baseURI=Credentails.v3;
		Response response= RestAssured.given()
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
			
			.queryParam("heightPref",Credentails.heightPref)
			.queryParam("unitPref","m")
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("/calibrate");
		
	    JSONObject jsonResponse = new JSONObject(response.getBody().asPrettyString());
	    Integer id = jsonResponse.getInt("id");
	    String message = jsonResponse.getString("additionalInfo");

		
	    System.out.println(id +":" + message);

	}

	@Test
	public static void measurements() throws InterruptedException {
		
		System.out.println("Measurement Count:");
		
		Scanner sc=new Scanner(System.in);
		
		int measurementcount=sc.nextInt();
		
		for(int i=1;i<=measurementcount;i++){  
			
        Thread.sleep(3000); 
		
		RestAssured.baseURI=Credentails.v3;
		
		Response response= RestAssured.given()
		
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
	    		.queryParam("sku", "suzume")
			
		.when()
			.get("/measure");
		
		
	    JSONObject jsonResponse = new JSONObject(response.getBody().asPrettyString());
	    Integer id = jsonResponse.getInt("id");
	    String message = jsonResponse.getString("additionalInfo");
	    
	    System.out.println("In Loop" + i + "" +id +":" + message);

		}
}
	
}
