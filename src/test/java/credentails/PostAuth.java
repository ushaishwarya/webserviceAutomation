package credentails;



import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PostAuth {
	 public static String accessToken;
	 public static String refreshToken;
	    public static String[] getauth() {
	   
	    	
	        RestAssured.baseURI =Credentails.v3;
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
	            .post("auth");

	        accessToken = response.jsonPath().getString("access_token");
	        refreshToken = response.jsonPath().getString("refresh_token");
	        
	       
			return new String[] {accessToken, refreshToken};
	       
			
	    }


}
