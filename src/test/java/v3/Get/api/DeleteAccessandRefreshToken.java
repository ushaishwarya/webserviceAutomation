package v3.Get.api;


import org.testng.annotations.Test;
import org.testng.Assert;
import static io.restassured.RestAssured.given;

import org.json.JSONObject;


import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;


public class DeleteAccessandRefreshToken {
	@Test
	public void deletetoken() {
	String[] tokens = PostAuth.getauth();
    String accessToken = tokens[0];
    String refreshToken = tokens[1];
    
	RestAssured.baseURI=Credentails.v3;
	
    Response response = given()
		.header("Content-Type","application/json")
		.header("X-Refresh-Token",refreshToken)
		.header("Authorization","Bearer "+accessToken)
	
		.when()
			.delete("/auth/revoke-access")
		.then()
		.extract()
		.response();
    
    int statuscode=response.getStatusCode();
    
    Assert.assertEquals(statuscode, 200);
    
    JSONObject jsonResponse = new JSONObject(response.getBody().asString());
    
    String message = jsonResponse.getString("message");
    
    String excepted_Message="Successfully Deleted.";
    
    Assert.assertEquals(excepted_Message, message);
}
	@Test
	public void Unauthorized() {
	String[] tokens = PostAuth.getauth();
    String accessToken = tokens[0];
    String refreshToken = tokens[1];
    
	RestAssured.baseURI=Credentails.v3;
	
    Response response = given()
		.header("Content-Type","application/json")
		.header("X-Refresh-Token",accessToken)
		.header("Authorization","Bearer "+refreshToken)
	
		.when()
			.delete("/auth/revoke-access")
		.then()
		.extract()
		.response();
    
    int statuscode=response.getStatusCode();
    
    Assert.assertEquals(statuscode, 401);
    
    JSONObject jsonResponse = new JSONObject(response.getBody().asString());
    
    String message = jsonResponse.getString("message");
    
    String excepted_Message="Unauthorized!";
    
    Assert.assertEquals(excepted_Message, message);
}

	
	
	
}
