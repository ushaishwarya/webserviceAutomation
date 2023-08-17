package v2.Get.api;

import org.testng.annotations.Test;
import org.testng.Assert;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import credentails.Credentails;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetMeasurementDataById {
    @Test(priority=1)
    public void assertTheExteranlJsonToResponseBodyAndStatusCode() throws IOException {
        String externalJsonFilePath = Credentails.filepath;

        List<Map<String, Object>> jsonDataList = readJsonFile(externalJsonFilePath);

        int totalResponseTime = 0;
        long minResponseTime = Long.MAX_VALUE;
        long maxResponseTime = Long.MIN_VALUE;

        for (Map<String, Object> jsonData : jsonDataList) {
            String id = jsonData.get("id").toString();
            

            
            Response response = callApiV2(id);
            long responseTime = response.getTime();
            
            
            minResponseTime = Math.min(minResponseTime, responseTime);
            maxResponseTime = Math.max(maxResponseTime, responseTime);

            totalResponseTime += responseTime;
            
            compareResponseWithExternalData(response, jsonData);
        }

        int numberOfCalls = jsonDataList.size();
        long averageResponseTime = totalResponseTime / numberOfCalls;

        System.out.println("Number of Calls: " + numberOfCalls);
        System.out.println("Average Response Time in milliseconds: " + averageResponseTime);
        System.out.println("Minimum Response Time in milliseconds: " + minResponseTime);
        System.out.println("Maximum Response Time in milliseconds: " + maxResponseTime);
    }
    // To Made a request
    public static Response callApiV2(String id) {
        RestAssured.baseURI = Credentails.v2;

        Response response = RestAssured.given()
                .pathParam("id", id)
                .header("systemid", Credentails.systemid)
                .header("userid", Credentails.userid)
                .when()
                .get("/dimension/{id}")
                .then()
                .extract()
                .response();

        

        return response;
    }

    public static List<Map<String, Object>> readJsonFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
    }

    public void compareResponseWithExternalData(Response response, Map<String, Object> externalData) throws JsonMappingException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.getBody().asString());
        
        
        List<String> keysToSkip = Arrays.asList("scanned_time_zone", "workflow_data", "additional_info","retried_count","failed_status_codes","mode","webhook_log_ids","user_name","system","site");


        for (Map.Entry<String, Object> entry : externalData.entrySet()) {
            String key = entry.getKey();
            if (key == null) {
                continue;
            }

            Object value = entry.getValue();
            

            String expectedValue = (value != null) ? value.toString().trim() : "null"; 

            String mappedKey = getMappedKey(key);
            String actualValue = jsonResponse.has(mappedKey) ? jsonResponse.path(mappedKey).asText().trim() : "null";


            if (keysToSkip.contains(key)) {
                continue; 
            }
            if (expectedValue.equals("NA") && (actualValue.equals("null") || actualValue.isEmpty())) {
                continue; 
            }
            
            if (key.equals("scanned_time")) {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM d yyyy, hh:mm:ss a", Locale.ENGLISH);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("EEE MMM d, yyyy h:mm:ss a", Locale.ENGLISH);

                LocalDateTime expectedDateTime = LocalDateTime.parse(expectedValue, inputFormatter);
                String expectedFormattedDate = expectedDateTime.format(outputFormatter);

                String actualFormattedDate = jsonResponse.has(mappedKey) ? jsonResponse.path(mappedKey).asText().trim() : "null";
                
                if (actualFormattedDate.equals(expectedFormattedDate)) {
                    continue; // Dates match, skip the assertion
                } else {
                    System.out.println("Date Comparison - Key: " + key + ", Expected: " + expectedFormattedDate + ", Actual: " + actualFormattedDate);
                    Assert.fail("Date value for key '" + key + "' does not match the expected value.");
                }
            }
            int statusCode = response.getStatusCode();


            Assert.assertEquals(actualValue, expectedValue, "Response value for key '" + key + "' does not match the expected value.");
            Assert.assertEquals(statusCode, 200 , "Correct status code not returned");

        }
        

        
        }
    

    private String getMappedKey(String key) {
        Map<String, String> keyMapping = new HashMap<>();
        keyMapping.put("id", "id");
        keyMapping.put("scanned_time", "scannedOn");
        keyMapping.put("weight_unit", "weightUnit");
        keyMapping.put("dimension_unit", "dimensionUnit");
        keyMapping.put("length", "length");
        keyMapping.put("width", "width");
        keyMapping.put("height", "height");
        keyMapping.put("weight_unit", "weightUnit");
        keyMapping.put("barcode", "sku");
        keyMapping.put("actual_weight", "actualWeight");
        keyMapping.put("auxiliary_actual_weight", "auxiliaryActualWeight");
        keyMapping.put("additional_info", "additionalInfo");
        keyMapping.put("volumetric_weight", "volumetricWeight");
        keyMapping.put("auxiliary_volumetric_weight", "auxiliaryVolumetricWeight");
        keyMapping.put("cubic_volume", "cubicVolume");
        keyMapping.put("real_volume", "realVolume");
        keyMapping.put("workflow_data", "workflowmetadata");
        keyMapping.put("status_code", "statusCode");
        keyMapping.put("annotated_image", "annotatedImage");



        return keyMapping.getOrDefault(key, key);
    }
    @Test(priority=2)
    public void unauthorizedWithMultipeScenarious() throws IOException {

        RestAssured.baseURI = Credentails.v2;

        int numIterations = 3;       
        List<Map<String, Object>> jsonDataList = readJsonFile(Credentails.filepath);
        
        Object idValue = jsonDataList.get(0).get("id");

        String id = (idValue != null) ? idValue.toString() : null;


        for (int i = 1; i < numIterations; i++) {
            String systemId = Credentails.systemid;
            String userId = Credentails.userid;

            if (i == 1) {
                systemId = "";
            } else if (i == 2) {
                userId = "";
            } else if (i == 3) {
                systemId = "";
                userId = "";
            }

            Response response = RestAssured.given()
                    .pathParam("id", id)
                    .header("systemid", systemId)
                    .header("userid", userId)
                .when()
                    .get("/dimension/{id}")
              .then()
              .extract()
              .response();
            JSONObject jsonResponse = new JSONObject(response.getBody().asString());
            
            String message = jsonResponse.getString("message");
            
            String excepted_message="Unauthorized!";
            
            Assert.assertEquals(message, excepted_message,"Unauthorized message is not matched");
            int statusCode = response.getStatusCode();

            Assert.assertEquals(statusCode, 401 , "Correct status code not returned");


            
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
                id = "1234";
                expectedMessage = "No Measurement available for the given ID";
            } else if (i == 2) {
                id = "qwer";
                expectedMessage = "Invalid Measurement ID";
            }

            Response response = RestAssured.given()
                    .pathParam("id", id)
                    .header("systemid", Credentails.systemid)
                    .header("userid", Credentails.userid)
                .when()
                    .get("/dimension/{id}")
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
