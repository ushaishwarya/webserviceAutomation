package v3.Get.api;

import org.testng.annotations.Test;


import com.google.gson.Gson;

import org.testng.Assert;
import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;


import credentails.Credentails;
import credentails.CommonMethods;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class GenerateAPIAccessToken extends CommonMethods{
	 public static String accessToken;
	 public static String refreshToken;
	
	    @Test
	    public static void accesstokenandrefereshtoken() {
	   
	    	
	        RestAssured.baseURI =Credentails.baseurl;
	        // Create a Map to represent the dynamic payload
	        Map<String, Object> requestBody = new HashMap<>();
	        requestBody.put("user_id", Credentails.userid);
	        requestBody.put("secret_id",Credentails.secretid);


	        // Convert the Map to JSON using Gson
	        Gson gson = new Gson();
	        String jsonBody = gson.toJson(requestBody);
	        


	        Response response = given()
	        		
	        			.contentType(ContentType.JSON)
	        				.body(jsonBody)
	            
            
	           .when()
	            .post("v3/auth")
	            .then()
	            .extract()
	            .response();
	        
	            
	            Assert.assertEquals(response.getStatusCode(), 200);
	    }
	    @Test
	    public static void Unauthorized() {
	   
	    	
	        RestAssured.baseURI =Credentails.baseurl;
           String jsonBody = String.format("{\"user_id\":\"%s\",\"secret_id\":\"%s\"}","","");

	        Response response = given()
	        		
	        		.contentType(ContentType.JSON)
	        		.body(jsonBody)
	            
            
	           .when()
	            .post("v3/auth")
	        	.then()
	        	.extract()
	        	.response();
	        
	        assertMessageAndStatuscode(response, "Unauthorized!", 401);

	    }



}
