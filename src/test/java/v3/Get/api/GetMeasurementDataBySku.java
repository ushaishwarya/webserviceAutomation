package v3.Get.api;

import org.testng.annotations.Test;

import org.testng.Assert;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import credentails.Credentails;
import credentails.CommonMethods;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;


public class GetMeasurementDataBySku extends CommonMethods{
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];

	@Test(priority=1)
    public void assertTheExteranlJsonToResponseBodyAndStatusCode() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File jsonFile = new File(Credentails.filepath);
            JsonNode arrayNode = objectMapper.readTree(jsonFile);

            Set<String> valuesSet = new HashSet<>();

            if (arrayNode.isArray()) {
                for (JsonNode objectNode : arrayNode) {
                    JsonNode idNode = objectNode.get("barcode");
                    if (idNode.isTextual()) {
                        valuesSet.add(idNode.asText());
                    }
                }
            }
            
            RestAssured.baseURI = Credentails.v3;

            for (String id : valuesSet) {
                Response response = RestAssured.given()
                	.queryParam("id", id)
        			.header("Content-Type","application/json")
       			.header("System-Token",Credentails.systemid)
        			.header("Authorization","Bearer "+accessToken)
                    
                   .when()
                   .get("/sku")
                   .then()
                    .extract()
                   .response();

               List<String> skuList = response.jsonPath().getList("sku", String.class);

                for (String sku : skuList) {
                    Assert.assertEquals(sku,id, "Barcode does not match the expected value.");

                }
                

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        

    	
}
	
    @Test(priority=2)
    public void verifystandardFormate() throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                File jsonFile = new File(Credentails.filepath);
                JsonNode arrayNode = objectMapper.readTree(jsonFile);

                Set<String> valuesSet = new HashSet<>();

                if (arrayNode.isArray()) {
                    for (JsonNode objectNode : arrayNode) {
                        JsonNode idNode = objectNode.get("barcode");
                        if (idNode.isTextual()) {
                            valuesSet.add(idNode.asText());
                        }
                    }
                }


                RestAssured.baseURI = Credentails.v3;                

                for (String id : valuesSet) {
                    Response response = RestAssured.given()
                            .queryParam("id", id)
                            .header("Content-Type", "application/json")
                            .header("System-Token", Credentails.systemid)
                            .header("Authorization", "Bearer " + accessToken)
                            .queryParam("isStandardDateFormat", "true")
                            .queryParam("isStandardDimensionUnitFormat", "true")
                            .queryParam("isStandardWeightUnitFormat", "true")

                            .when()
                            .get("/sku")
                            .then()
                            .extract()
                            .response();

                Standard(response);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    

    
@Test(priority=3)
public void verifylegacyFormate() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();

    try {
        File jsonFile = new File(Credentails.filepath);
        JsonNode arrayNode = objectMapper.readTree(jsonFile);

        Set<String> valuesSet = new HashSet<>();

        if (arrayNode.isArray()) {
            for (JsonNode objectNode : arrayNode) {
                JsonNode idNode = objectNode.get("barcode");
                if (idNode.isTextual()) {
                    valuesSet.add(idNode.asText());
                }
            }
        }

        RestAssured.baseURI = Credentails.v3;                

        for (String id : valuesSet) {
            Response response = RestAssured.given()
                    .queryParam("id", id)
                    .header("Content-Type", "application/json")
                    .header("System-Token", Credentails.systemid)
                    .header("Authorization", "Bearer " + accessToken)
                    .queryParam("isStandardDateFormat", "false")
                    .queryParam("isStandardDimensionUnitFormat", "false")
                    .queryParam("isStandardWeightUnitFormat", "false")

                    .when()
                    .get("/sku")
                    .then()
                    .extract()
                    .response();
            
            legacy(response);

        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}
@Test(priority=4)
public void unauthorizedWithMultipeScenarious() throws IOException {

  RestAssured.baseURI = Credentails.v1;

  int numIterations = 3;

  List<Map<String, Object>> jsonDataList = readJsonFile1(Credentails.filepath);

  String sku = String.valueOf(jsonDataList.get(0).get("barcode"));

  for (int i = 1; i < numIterations; i++) {
      String systemId = Credentails.systemid;
      String token = accessToken;

      if (i == 1) {
          systemId = "";
          
      } else if (i == 2) {
      	token = "";
      	
      } else if (i == 3) {
    	  
          systemId = "";
          token = "";

      }

      Response response = RestAssured.given()
              .queryParam("id", sku)
  			.header("Content-Type","application/json")
  			.header("System-Token",systemId)
  			.header("Authorization","Bearer "+token)
          .when()
              .get("/sku")
        .then()
        .extract()
        .response();

	    assertMessageAndStatuscode(response, "Unauthorized!", 401);

      
  }
  
}
public static List<Map<String, Object>> readJsonFile1(String filePath) throws IOException {
  ObjectMapper objectMapper = new ObjectMapper();
  return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
}

@Test(priority=5)
public void badRequestWithMultipeScenarious() throws IOException {

  RestAssured.baseURI = Credentails.v3;
  int numIterations = 2;
  for (int i = 1; i <= numIterations; i++) {
      String id = ""; 
      String expectedMessage = ""; 

      if (i == 1) {
          id = "12erdfd";
          expectedMessage = "No Measurement available for the given barcode";
      } else if (i == 2) {
          id = "";
          expectedMessage = "Please Provide barcode value";
      }

      Response response = RestAssured.given()
              .queryParam("id", id)
    			.header("Content-Type","application/json")
      			.header("System-Token",Credentails.systemid)
      			.header("Authorization","Bearer "+accessToken)
          .when()
              .get("/sku")
          .then()
              .extract()
              .response();
      
      JSONObject jsonResponse = new JSONObject(response.getBody().asString());
      String message = jsonResponse.getString("message");
      int statusCode = response.getStatusCode();

      Assert.assertTrue(statusCode == 400 || statusCode == 404, "Correct status code not returned (400 or 404)");

      Assert.assertEquals(message, expectedMessage, "Message does not match the expected value.");
      
  }
}






}

