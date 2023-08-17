package v3.Get.api;

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
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetMeasurementDataById {
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];
	
	public static String id;

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
            
            
            // Update minimum and maximum response times
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
    public static Response callApiV2(String id) {
        RestAssured.baseURI = Credentails.v3;

        Response response = RestAssured.given()
                .pathParam("id", id)
    			.header("Content-Type","application/json")
    			.header("System-Token",Credentails.systemid)
    			.header("Authorization","Bearer "+accessToken)
                .queryParam("isStandardDateFormat", "false")
                .queryParam("isStandardDimensionUnitFormat", "true")
                .queryParam("isStandardWeightUnitFormat", "true")

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


            Assert.assertEquals(actualValue,expectedValue,"' - Response value for key '" + key + "' does not match the expected value.");
            
            
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

        RestAssured.baseURI = Credentails.v3;

        int numIterations = 2; 
        List<Map<String, Object>> jsonDataList = readJsonFile(Credentails.filepath);
        
        Object idValue = jsonDataList.get(0).get("id");

     // Convert the idValue to a string
     String id = (idValue != null) ? idValue.toString() : null;


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
                    .pathParam("id", id)
        			.header("Content-Type","application/json")
        			.header("System-Token",systemId)
        			.header("Authorization","Bearer "+token)
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

        RestAssured.baseURI = Credentails.v3;
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
        			.header("Content-Type","application/json")
        			.header("System-Token",Credentails.systemid)
        			.header("Authorization","Bearer "+accessToken)
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
    @Test
        public void verifyStandardFormate() throws IOException {

            List<Map<String, Object>> jsonDataList = readJsonFile(Credentails.filepath);

            Object idValue = jsonDataList.get(0).get("id");
            String id = (idValue != null) ? idValue.toString() : null;

            RestAssured.baseURI = Credentails.v3;
            String[] tokens = PostAuth.getauth();
            String accessToken = tokens[0];

            Response response = RestAssured.given()
                    .pathParam("id", id) // Use the id variable here
                    .header("Content-Type", "application/json")
                    .header("System-Token", Credentails.systemid)
                    .header("Authorization", "Bearer " + accessToken)
                    .queryParam("isStandardDateFormat", "true")
                    .queryParam("isStandardDimensionUnitFormat", "true")
                    .queryParam("isStandardWeightUnitFormat", "true")
                    .when()
                    .get("/dimension/{id}")
                    .then()
                    .extract()
                    .response();

            String actualId = response.jsonPath().getString("id");
            String actualDimensionUnitFormat = response.jsonPath().getString("dimensionUnit");
            String actualWeightUnitFormat = response.jsonPath().getString("weightUnit");
            String actualDateUnitFormat = response.jsonPath().getString("scannedOn");


            Assert.assertEquals(actualId, id, "Response id does not match the expected id.");

            Assert.assertTrue(actualDimensionUnitFormat.equalsIgnoreCase("in")
                    || actualDimensionUnitFormat.equalsIgnoreCase("cm"),
                    "Response dimension unit format is not 'in' or 'cm'.");

            Assert.assertTrue(actualWeightUnitFormat.equalsIgnoreCase("kg")
                    || actualWeightUnitFormat.equalsIgnoreCase("g")
                    || actualWeightUnitFormat.equalsIgnoreCase("lb"),
                    "Response weight unit format is not one of 'kg', 'g', or 'lb'.");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime inputDateTime = LocalDateTime.parse(actualDateUnitFormat, formatter);
            LocalDateTime desiredDateTime = LocalDateTime.parse(actualDateUnitFormat, formatter);

            Assert.assertEquals(inputDateTime, desiredDateTime, "Dates do not match.");

        }
    @Test
    public void verifylegacyFormate() throws IOException {

        List<Map<String, Object>> jsonDataList = readJsonFile(Credentails.filepath);

        Object idValue = jsonDataList.get(0).get("id");
        String id = (idValue != null) ? idValue.toString() : null;

        RestAssured.baseURI = Credentails.v3;
        String[] tokens = PostAuth.getauth();
        String accessToken = tokens[0];

        Response response = RestAssured.given()
                .pathParam("id", id) 
                .header("Content-Type", "application/json")
                .header("System-Token", Credentails.systemid)
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("isStandardDateFormat", "false")
                .queryParam("isStandardDimensionUnitFormat", "false")
                .queryParam("isStandardWeightUnitFormat", "false")
                .when()
                .get("/dimension/{id}")
                .then()
                .extract()
                .response();

        String actualId = response.jsonPath().getString("id");
        String actualDimensionUnitFormat = response.jsonPath().getString("dimensionUnit");
        String actualWeightUnitFormat = response.jsonPath().getString("weightUnit");
        String actualDateUnitFormat = response.jsonPath().getString("scannedOn");


        Assert.assertEquals(actualId, id, "Response id does not match the expected id.");

        Assert.assertTrue(actualDimensionUnitFormat.equalsIgnoreCase("Inch")
                || actualDimensionUnitFormat.equalsIgnoreCase("Centimeter"),
                "Response dimension unit format is not 'Inch' or 'Centimeter'.");

        Assert.assertTrue(actualWeightUnitFormat.equalsIgnoreCase("Kilogram")
                || actualWeightUnitFormat.equalsIgnoreCase("gram")
                || actualWeightUnitFormat.equalsIgnoreCase("pound"),
                "Response weight unit format is not one of 'Kilogram', 'gram', or 'pound'.");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM d, yyyy h:mm:ss a", Locale.ENGLISH);
        LocalDateTime inputDateTime = LocalDateTime.parse(actualDateUnitFormat, formatter);
        LocalDateTime desiredDateTime = LocalDateTime.parse(actualDateUnitFormat, formatter);

        Assert.assertEquals(inputDateTime, desiredDateTime, "Dates do not match.");

    }




}
