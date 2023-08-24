package v2.Get.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import credentails.Credentails;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ListMeasurementIDandDates {
    @Test
    public void testScanIdListEndpoint() {
        RestAssured.baseURI = Credentails.v2;

        Response response = RestAssured.given()
                .header("systemid", Credentails.systemid)
                .header("userid", Credentails.userid)
                .when()
                .get("/scanidlist")
                .then()
                .extract()
                .response();

        // Assert the response status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Unexpected status code");

        // Parse the JSON response body
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> scanIdList = jsonPath.getList("$");

        // Validate each object in the array
        for (Map<String, Object> scanId : scanIdList) {
            Assert.assertTrue(scanId.containsKey("id"), "Missing 'id' property");
            Assert.assertTrue(scanId.get("id") instanceof Integer, "'id' should be an integer");

            Assert.assertTrue(scanId.containsKey("scannedOn"), "Missing 'scannedOn' property");
            String scannedOn = (String) scanId.get("scannedOn");
            
            //System.out.println(scannedOn);
            Assert.assertTrue(validateDatePattern(scannedOn), "'scannedOn' has invalid date pattern");
        }
    }

    private boolean validateDatePattern(String date) {
        // Define the expected date pattern
        String expectedPattern = "EEE MMM dd, yyyy h:mm:ss a";

        // Create a SimpleDateFormat instance with the expected pattern
        SimpleDateFormat dateFormat = new SimpleDateFormat(expectedPattern);

        try {
            // Attempt to parse the provided date using the expected pattern
            dateFormat.parse(date);
            return true; // If parsing succeeds, the date has the correct pattern
        } catch (ParseException e) {
            return false; // If parsing fails, the date has an incorrect pattern
        }
    }
    @Test
	public void Unauthorized() {

	    RestAssured.baseURI = Credentails.v2;
	    Response response = RestAssured
	            .given()
	            .header("systemid", "")
	            .header("userid", "")
	            .when()
                .get("/scanidlist")
	            .then()
	            .extract().response();
	    
	    int statuscode=response.getStatusCode();
	    Assert.assertEquals(statuscode, 401);
	    
	    JSONObject jsonResponse = new JSONObject(response.getBody().asString());
	    String message = jsonResponse.getString("message");
	    String excepted_message="Unauthorized!";
	    Assert.assertEquals(excepted_message, message);

		}
    @Test
	public void Notfound() {

	    RestAssured.baseURI = Credentails.v2;
	    Response response = RestAssured
	            .given()
	            .header("systemid", Credentails.systemid)
	            .header("userid", Credentails.userid)
	            .when()
                .get("/")
	            .then()
	            .extract().response();
	    
	    int statuscode=response.getStatusCode();
	    Assert.assertEquals(statuscode, 404);
	    
	    JSONObject jsonResponse = new JSONObject(response.getBody().asString());
	    String message = jsonResponse.getString("message");
	    String excepted_message="Not found";
	    Assert.assertEquals(excepted_message, message);

		}



}
