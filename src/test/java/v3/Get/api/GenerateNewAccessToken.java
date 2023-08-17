package v3.Get.api;

import static io.restassured.RestAssured.given;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GenerateNewAccessToken {
	@Test
	public void newaccesstoken() {
		
	    String[] tokens = PostAuth.getauth();
	    String accessToken = tokens[0];
	    String refreshToken = tokens[1];
	    
	    
	    RestAssured.baseURI =Credentails.v3;
	    
	    
	    
		Response response = given()
	    		.header("Content-Type","application/json")
	            .header("Authorization", "Bearer " + accessToken)
	            .header("x-refresh-token", refreshToken)
	            
	            .when()
	            .post("/auth/refresh-access")
	            .then()
	            .extract()
	            .response();
	    
		int statuscode=response.getStatusCode();
		Assert.assertEquals(statuscode, 200);
	    
	}
	@Test
	public void Unauthorized() {
		
	    String[] tokens = PostAuth.getauth();
	    String accessToken = tokens[0];
	    String refreshToken = tokens[1];
	    
	    
	    RestAssured.baseURI =Credentails.v3;
	    
	    
	    
		Response response = given()
	    		.header("Content-Type","application/json")
	            .header("Authorization", "Bearer " + refreshToken)
	            .header("x-refresh-token", accessToken)
	            
	            .when()
	            .post("/auth/refresh-access")
	            .then()
	            .extract()
	            .response();
	    
        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        
        String message = jsonResponse.getString("message");
        
        String excepted_message="Unauthorized!";

		Assert.assertEquals(excepted_message, message);
	    
		int statuscode=response.getStatusCode();
		
		Assert.assertEquals(statuscode, 401);

	}



}
