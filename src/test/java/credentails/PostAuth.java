package credentails;



import static io.restassured.RestAssured.given;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PostAuth {
	 public static String accessToken;
	 public static String refreshToken;
	
	    public static String[] getauth() {
	   
	    	
	        RestAssured.baseURI =Credentails.v3;
           String jsonBody = String.format("{\"user_id\":\"%s\",\"secret_id\":\"%s\"}", Credentails.userid, Credentails.secretid);

	        Response response = given()
	        		
       	.contentType(ContentType.JSON)
       	.body(jsonBody)
	            
	            
	           .when()
	            .post("auth");

	        accessToken = response.jsonPath().getString("access_token");
	        refreshToken = response.jsonPath().getString("refresh_token");
	        
	       
			return new String[] {accessToken, refreshToken};
	       
			
	    }


}
