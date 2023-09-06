package v2.Get.api;

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

import credentails.CommonMethods;
import credentails.Credentails;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Ignore;
@Ignore

public class GetMeasurementDataBySku extends CommonMethods {
	@Test
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
            
            RestAssured.baseURI = Credentails.v2;

            for (String id : valuesSet) {
                Response response = RestAssured.given()
                	.queryParam("id", id)
                    .header("systemid", Credentails.systemid)
                    .header("userid", Credentails.userid)
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
  public void unauthorizedWithMultipeScenarious() throws IOException {

      RestAssured.baseURI = Credentails.v2;

      int numIterations = 3;

      List<Map<String, Object>> jsonDataList = readJsonFile1(Credentails.filepath);

      String sku = String.valueOf(jsonDataList.get(0).get("barcode"));

      for (int i = 1; i < numIterations; i++) {
          String systemId = Credentails.systemid;
          String userId = Credentails.userid;

          if (i == 1) {
              systemId = "";
          } else if (i == 2) {
              userId = "";
          } else if (i == 4) {
              systemId = "";
              userId = "";
          }

          Response response = RestAssured.given()
                  .queryParam("id", sku)
                  .header("systemid", systemId)
                  .header("userid", userId)
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
  
  @Test(priority=3)
  public void badRequestWithMultipeScenarious() throws IOException {

      RestAssured.baseURI = Credentails.v2;
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
                  .header("systemid", Credentails.systemid)
                  .header("userid", Credentails.userid)
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
