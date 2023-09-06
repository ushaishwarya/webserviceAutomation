package v3.Get.api;


import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;



import credentails.Credentails;
import credentails.CommonMethods;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.testng.annotations.Ignore;
@Ignore

public class DeleteAccessandRefreshToken extends CommonMethods {
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
    
    
    assertMessageAndStatuscode(response, "Successfully Deleted.", 200);

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
    
    assertMessageAndStatuscode(response, "Unauthorized!", 401);

    
}

	
	
	
}
