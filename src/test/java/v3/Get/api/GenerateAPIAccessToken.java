package v3.Get.api;

import org.testng.annotations.Test;
import org.testng.Assert;
import static io.restassured.RestAssured.given;

import org.json.JSONObject;

import credentails.Credentails;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Ignore;
@Ignore


public class GenerateAPIAccessToken {
	 public static String accessToken;
	 public static String refreshToken;
	
	    @Test
	    public static void accesstokenandrefereshtoken() {
	   
	    	
	        RestAssured.baseURI =Credentails.v3;
           String jsonBody = String.format("{\"user_id\":\"%s\",\"secret_id\":\"%s\"}", Credentails.userid, Credentails.secretid);

	        Response response = given()
	        		
       	.contentType(ContentType.JSON)
       	.body(jsonBody)
	            
            
	           .when()
	            .post("auth")
	            .then()
	            .extract()
	            .response();
	            
	            int statuscode=response.getStatusCode();
	            
	            Assert.assertEquals(statuscode, 200);
	    }
	    @Test
	    public static void Unauthorized() {
	   
	    	
	        RestAssured.baseURI =Credentails.v3;
           String jsonBody = String.format("{\"user_id\":\"%s\",\"secret_id\":\"%s\"}","","");

	        Response response = given()
	        		
	        		.contentType(ContentType.JSON)
	        		.body(jsonBody)
	            
            
	           .when()
	            .post("auth")
	        	.then()
	        	.extract()
	        	.response();
            JSONObject jsonResponse = new JSONObject(response.getBody().asString());
            
            String message = jsonResponse.getString("message");

	            int statuscode=response.getStatusCode();
	            String excepted_message="Unauthorized!";
	             		
	            Assert.assertEquals(statuscode, 401);
	            Assert.assertEquals(excepted_message, message);
	    }



}
