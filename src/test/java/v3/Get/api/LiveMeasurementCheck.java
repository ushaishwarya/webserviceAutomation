package v3.Get.api;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.Gson;

import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class LiveMeasurementCheck {
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];

	@Test
    public void testNoImageFound() {

	
    RestAssured.baseURI =Credentails.v3;
    // Create a Map to represent the dynamic payload
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("sku", Credentails.referenceboxsku);


    // Convert the Map to JSON using Gson
    Gson gson = new Gson();
    String jsonBody = gson.toJson(requestBody);
    

    Response response = given()
    		
    		.contentType(ContentType.JSON)
    		.body(jsonBody)
    		.header("System-Token",Credentails.systemid)
    		.header("Authorization","Bearer "+accessToken)

        
        
       .when()
        .post("/measurement-check");
    
    System.out.println(response.asPrettyString());
    JsonPath jsonPath = response.jsonPath();
    String sku=jsonPath.getString("sku");
    boolean status=jsonPath.getBoolean("status");
    int statusCode=jsonPath.getInt("statusCode");
    String additionalInfo=jsonPath.getString("additionalInfo");
    Double length=jsonPath.getDouble("length");
    double width=jsonPath.getDouble("width");
    double height=jsonPath.getDouble("height");
    String weightUnit=jsonPath.getString("weightUni");
    double actualWeight=jsonPath.getDouble("actualWeight");
    
    Assert.assertEquals(sku, Credentails.sku);
    Assert.assertEquals(status, Credentails.status);
    Assert.assertEquals(statusCode, Credentails.statusCode);
    Assert.assertEquals(additionalInfo, Credentails.additionalInfo);
    Assert.assertEquals(length, Credentails.length);
    Assert.assertEquals(width, Credentails.width);
    Assert.assertEquals(height, Credentails.height);
    Assert.assertEquals(weightUnit, Credentails.weightUni);
    Assert.assertEquals(actualWeight, Credentails.actualWeight);


    
   


    }

}
