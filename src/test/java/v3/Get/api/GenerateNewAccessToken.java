package v3.Get.api;

import org.testng.annotations.Test;

import org.testng.Assert;
import static io.restassured.RestAssured.given;


import credentails.Credentails;
import credentails.CommonMethods;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Ignore;
@Ignore


public class GenerateNewAccessToken extends CommonMethods{
	@Test
	public void newaccesstoken() {
		
	    String[] tokens = PostAuth.getauth();
	    String accessToken = tokens[0];
	    String refreshToken = tokens[1];
	    
	    
	    RestAssured.baseURI =Credentails.baseurl;
	    
	    
	    
		Response response = given()
	    		.header("Content-Type","application/json")
	            .header("Authorization", "Bearer " + accessToken)
	            .header("x-refresh-token", refreshToken)
	            
	            .when()
	            .post("v3/auth/refresh-access")
	            .then()
	            .extract()
	            .response();
	    
		Assert.assertEquals(response.getStatusCode(), 200);
	    
	}
	@Test
	public void Unauthorized() {
		
	    String[] tokens = PostAuth.getauth();
	    String accessToken = tokens[0];
	    String refreshToken = tokens[1];
	    
	    
	    RestAssured.baseURI =Credentails.baseurl;
	    
	    
	    
		Response response = given()
	    		.header("Content-Type","application/json")
	            .header("Authorization", "Bearer " + refreshToken)
	            .header("x-refresh-token", accessToken)
	            
	            .when()
	            .post("v3/auth/refresh-access")
	            .then()
	            .extract()
	            .response();
	    
	    assertMessageAndStatuscode(response, "Unauthorized!", 401);

	}



}
