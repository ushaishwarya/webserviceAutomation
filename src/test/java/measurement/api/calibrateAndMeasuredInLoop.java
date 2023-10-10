package measurement.api;

import java.io.FileWriter;
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
	
	@Test(enabled=true)
	public static void CalibratedSuccessfully () throws InterruptedException {
        Thread.sleep(3000); // Time is in milliseconds

		RestAssured.baseURI=Credentails.baseurl;
		Response response= RestAssured.given()
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
			
			.queryParam("heightPref",Credentails.heightPref)
			.queryParam("unitPref","m")
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("v3/calibrate");
		
	    JSONObject jsonResponse = new JSONObject(response.getBody().asPrettyString());
	    Integer id = jsonResponse.getInt("id");
	    String message = jsonResponse.getString("additionalInfo");

        String filePath = Credentails.pathtostoreresponse;

	    System.out.println(id +":" + message);
	    
//        FileWriter fileWriter = new FileWriter(filePath);
//        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//
//        // Write the JSON response to the file
//        bufferedWriter.write(jsonResponse);
//
//        // Close the BufferedWriter to flush and save the data to the file
//        bufferedWriter.close();

        System.out.println("JSON response has been saved to " + filePath);


	}

	@Test(priority=2)
	public static void measurements() throws InterruptedException {
		
		System.out.println("Measurement Count:");
		
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(System.in);
		
		int measurementcount=sc.nextInt();
		
		for(int i=1;i<=measurementcount;i++){  
			
        Thread.sleep(3000); 
		
		RestAssured.baseURI=Credentails.baseurl;
		
		Response response= RestAssured.given()
		
	    		.header("System-Token",Credentails.systemid)
	    		.header("Authorization","Bearer "+accessToken)
	    		.queryParam("sku", "suzumitaki")
			
		.when()
			.get("v3/measure");
		
		
		
//	    JSONObject jsonResponse = new JSONObject(response.getBody().asPrettyString());
//	    Integer id = jsonResponse.getInt("id");
//	    String message = jsonResponse.getString("additionalInfo");
	    
//	    System.out.println("In Loop" + i  +":" + message);
		System.out.println(response.asPrettyString());

		}
}
	
}
