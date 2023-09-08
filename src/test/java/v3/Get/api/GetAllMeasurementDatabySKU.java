package v3.Get.api;


import java.io.IOException;



import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.CommonMethods;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetAllMeasurementDatabySKU extends CommonMethods{
	String[] tokens = PostAuth.getauth();
	String accessToken = tokens[0];

	@Test
	public void verifySkuId() {

		RestAssured.baseURI=Credentails.v3;

		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", Credentails.Sku)
				.queryParam("start_date", Credentails.startDate)
				.queryParam("end_date", Credentails.endDate)

				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();

		List<String> skuList = response.jsonPath().getList("sku", String.class);


		for (String exceptedsku : skuList) {
			Assert.assertEquals(exceptedsku,Credentails.Sku, "Barcode does not match the expected value.");

		}


	}

	@Test
	public void SkipFailureIsTrue() {

		RestAssured.baseURI=Credentails.v3;

		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", Credentails.Sku)
				.queryParam("start_date", Credentails.startDate)
				.queryParam("end_date", Credentails.endDate)
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
	public void SkipFailureIsFalse() {

		RestAssured.baseURI=Credentails.v3;

		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", Credentails.Sku)
				.queryParam("start_date", Credentails.startDate)
				.queryParam("end_date", Credentails.endDate)
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
	public void verifylegacyFormat() {
		RestAssured.baseURI=Credentails.v3;

		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", Credentails.Sku)
				.queryParam("start_date", Credentails.startDate)
				.queryParam("end_date", Credentails.endDate)
				.queryParam("isStandardDateFormat", "false")
				.queryParam("isStandardDimensionUnitFormat", "false")
				.queryParam("isStandardWeightUnitFormat", "false")

				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();

			legacy(response);

		
	}
	@Test
	public void verifystandardFormat() {
		RestAssured.baseURI=Credentails.v3;

		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", Credentails.Sku)
				.queryParam("start_date", Credentails.startDate)
				.queryParam("end_date", Credentails.endDate)
				.queryParam("isStandardDateFormat", "true")
				.queryParam("isStandardDimensionUnitFormat", "true")
				.queryParam("isStandardWeightUnitFormat", "true")

				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();
		
		Standard(response);

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

		
	    assertMessageAndStatuscode(response, "\"sku\" is not allowed to be empty", 400);

	}

	@Test
	public void  lessThanStartDate() {
		RestAssured.baseURI=Credentails.v3;


		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", Credentails.Sku)
				.queryParam("start_date",Credentails.endDate )
				.queryParam("end_date", Credentails.startDate)

				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();

		
	    assertMessageAndStatuscode(response, "end date cannot be less than the start date", 400);



	}
	@Test
	public void badRequestmultiplescenario1() {
		RestAssured.baseURI=Credentails.v3;


		Response response = RestAssured.given()

				.header("Content-Type","application/json")
				.header("System-Token",Credentails.systemid)
				.header("Authorization","Bearer "+accessToken)
				.queryParam("sku", Credentails.Sku)
				.queryParam("start_date", Credentails.lessninty)
				.queryParam("end_date", Credentails.endDate)

				.when()
				.get("/find-all-dimensions-by-sku")

				.then()
				.extract()
				.response();

		
	    assertMessageAndStatuscode(response, "start date cannot be less than 92 days from today", 400);



	}
	@Test
	public void unauthorizedWithMultipeScenarious() throws IOException {

		RestAssured.baseURI = Credentails.v3;

			Response response = RestAssured.given()
					.queryParam("sku", Credentails.Sku)
					.header("Content-Type","application/json")
					.header("System-Token","")
					.header("Authorization","Bearer "+"")
					.when()
					.get("/find-all-dimensions-by-sku")
					.then()
					.extract()
					.response();
		    assertMessageAndStatuscode(response, "Unauthorized!", 401);
		}
	
	@Test
	public void noMeasurement() throws IOException {

		RestAssured.baseURI = Credentails.v3;


			Response response = RestAssured.given()
					.queryParam("sku", Credentails.invalidSku)
					.header("Content-Type","application/json")
					.header("System-Token",Credentails.systemid)
					.header("Authorization","Bearer "+accessToken)


					.when()
					.get("/find-all-dimensions-by-sku")
					.then()
					.extract()
					.response();
			
		    assertMessageAndStatuscode(response, "No Measurement available for the given barcode", 404);

		}
	}



