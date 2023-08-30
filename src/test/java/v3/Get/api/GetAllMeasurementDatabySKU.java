package v3.Get.api;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetAllMeasurementDatabySKU {
	String[] tokens = PostAuth.getauth();
	String accessToken = tokens[0];

	@Test
	@Parameters({"Sku","startDate","endDate"})
	public void verifySkuId(String sku,String startDate,String endDate) {

		RestAssured.baseURI=Credentails.v3;

		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", sku)
				.queryParam("start_date", startDate)
				.queryParam("end_date", endDate)

				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();

		List<String> skuList = response.jsonPath().getList("sku", String.class);


		for (String exceptedsku : skuList) {
			Assert.assertEquals(exceptedsku,sku, "Barcode does not match the expected value.");

		}


	}

	@Test
	@Parameters({"Sku","startDate","endDate"})
	public void SkipFailureIsTrue(String sku,String startDate,String endDate) {

		RestAssured.baseURI=Credentails.v3;

		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", sku)
				.queryParam("start_date", startDate)
				.queryParam("end_date", endDate)
				.queryParam("skip_failure", true)

				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();

		List<String> statusList = response.jsonPath().getList("status", String.class);


		for (String status : statusList) {

			if (status.equals("false")) {
				Assert.fail("Status is false. Assertion failed.");
			} else {
				Assert.assertEquals(status, "true", "Barcode does not match the expected value.");
			}
		}
	}
	@Test
	@Parameters({"Sku","startDate","endDate"})
	public void SkipFailureIsFalse(String sku,String startDate,String endDate) {

		RestAssured.baseURI=Credentails.v3;

		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", sku)
				.queryParam("start_date", startDate)
				.queryParam("end_date", endDate)
				.queryParam("skip_failure", false)

				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();

		List<String> statusList = response.jsonPath().getList("status", String.class);



		boolean foundFalse = false;

		for (String status : statusList) {
			if (status.equals("false")) {
				foundFalse = true;
				break;  // Exit the loop since you found a "false" status
			}
		}

		if (!foundFalse) {
			Assert.fail("No false status found. Assertion failed.");
		} else {
			// At least one "false" status was found; the assertion passes
		}


	}

	@Test
	@Parameters({"Sku","startDate","endDate"})
	public void verifylegacyFormat(String sku,String startDate,String endDate) {
		RestAssured.baseURI=Credentails.v3;

		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", sku)
				.queryParam("start_date", startDate)
				.queryParam("end_date", endDate)
				.queryParam("isStandardDateFormat", "false")
				.queryParam("isStandardDimensionUnitFormat", "false")
				.queryParam("isStandardWeightUnitFormat", "false")

				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();

		List<Map<String, Object>> responseData = response.jsonPath().getList("$");
		for (Map<String, Object> data : responseData) {
			String actualDimensionUnitFormat = data.get("dimensionUnit").toString();
			String actualWeightUnitFormat = data.get("weightUnit").toString();
			String actualDateUnitFormat = data.get("scannedOn").toString();

			// Your assertions here
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
	@Test
	@Parameters({"Sku","startDate","endDate"})
	public void verifystandardFormat(String sku,String startDate,String endDate) {
		RestAssured.baseURI=Credentails.v3;

		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", sku)
				.queryParam("start_date", startDate)
				.queryParam("end_date", endDate)
				.queryParam("isStandardDateFormat", "true")
				.queryParam("isStandardDimensionUnitFormat", "true")
				.queryParam("isStandardWeightUnitFormat", "true")

				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();
		List<Map<String, Object>> responseData = response.jsonPath().getList("$");
		for (Map<String, Object> data : responseData) {
			String actualDimensionUnitFormat = data.get("dimensionUnit").toString();
			String actualWeightUnitFormat = data.get("weightUnit").toString();
			String actualDateUnitFormat = data.get("scannedOn").toString();

			// Your assertions here
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
	}

	@Test
	public void badRequestmultiplescenario() {
		RestAssured.baseURI=Credentails.v3;


		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", "")
				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();

		JSONObject jsonResponse = new JSONObject(response.getBody().asString());
		String message = jsonResponse.getString("message");
		int statusCode = response.getStatusCode();

		Assert.assertEquals(message, "\"sku\" is not allowed to be empty");

		Assert.assertEquals(statusCode ,400, "Correct status code not returned 400");
	}

	@Test
	@Parameters({"Sku","startDate","endDate"})
	public void  lessThanStartDate(String sku,String startDate,String endDate) {
		RestAssured.baseURI=Credentails.v3;


		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", sku)
				.queryParam("start_date",endDate )
				.queryParam("end_date", startDate)

				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();

		JSONObject jsonResponse = new JSONObject(response.getBody().asString());
		String message = jsonResponse.getString("message");
		int statusCode = response.getStatusCode();

		Assert.assertEquals(message, "end date cannot be less than the start date");

		Assert.assertEquals(statusCode ,400, "Correct status code not returned 400");


	}
	@Test
	@Parameters({"Sku","endDate","lessninty"})
	public void badRequestmultiplescenario(String sku ,String endDate,String lessninty) {
		RestAssured.baseURI=Credentails.v3;


		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", sku)
				.queryParam("start_date", lessninty)
				.queryParam("end_date", endDate)

				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();

		JSONObject jsonResponse = new JSONObject(response.getBody().asString());
		String message = jsonResponse.getString("message");
		int statusCode = response.getStatusCode();

		Assert.assertEquals(message, "start date cannot be less than 92 days from today");

		Assert.assertEquals(statusCode ,400, "Correct status code not returned 400");


	}
	@Test
	@Parameters({"Sku","startDate","endDate"})
	public void unauthorizedWithMultipeScenarious(String sku,String startDate,String endDate) throws IOException {

		RestAssured.baseURI = Credentails.v3;

			Response response = RestAssured.given()
					.queryParam("sku", sku)
					.header("Content-Type","application/json")
					.header("System-Token","")
					.header("Authorization","Bearer "+"")
					.when()
					.get("/find-all-dimensions-by-sku")
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
	
	@Test
	@Parameters("invalidSku")
	public void noMeasurement(String invalidSku) throws IOException {

		RestAssured.baseURI = Credentails.v3;


			Response response = RestAssured.given()
					.queryParam("sku", invalidSku)
					.header("Content-Type","application/json")
					.header("System-Token",Credentails.systemid)
					.header("Authorization","Bearer "+accessToken)


					.when()
					.get("/find-all-dimensions-by-sku")
					.then()
					.extract()
					.response();
			JSONObject jsonResponse = new JSONObject(response.getBody().asString());

			String message = jsonResponse.getString("message");

			String excepted_message="No Measurement available for the given barcode";

			Assert.assertEquals(message, excepted_message,"Unauthorized message is not matched");
			int statusCode = response.getStatusCode();

			Assert.assertEquals(statusCode, 404 , "Correct status code not returned");
		}
	}



