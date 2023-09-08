package credentails;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.testng.Assert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class CommonMethods {

	public static void assertMessageAndStatuscode(Response response, String expectedMessage, int expectedStatusCode) {
		JSONObject jsonResponse = new JSONObject(response.getBody().asString());
		String message = jsonResponse.getString("message");
		int statusCode = response.getStatusCode();

		Assert.assertEquals(message, expectedMessage, "Message does not match the expected value.");
		Assert.assertEquals(statusCode, expectedStatusCode, "Correct status code not returned");

	}

	public void legacy(Response response) {
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

	public void Standard(Response response) {


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
	public void extractValuesFromImage(Response response) {

		Assert.assertEquals(response.getStatusCode(), 200);

		byte[] imageBytes = response.body().asByteArray();

		File tempImageFile = null;
		try {
			tempImageFile = File.createTempFile("tempImage", ".jpg");
			try (FileOutputStream fos = new FileOutputStream(tempImageFile)) {
				fos.write(imageBytes);
			}

			ITesseract tesseract = new Tesseract();
			tesseract.setDatapath("//usr/share/tesseract-ocr/4.00/tessdata");

			String extractedText = tesseract.doOCR(tempImageFile);
			String desiredKey = "MID";
			Integer expectedValue = Credentails.ImageId;
			Integer extractedValue = extractValueFromText(extractedText, desiredKey);

			Assert.assertEquals(extractedValue, expectedValue, "MID value does not match the expected value.");

		} catch (AssertionError assertionError) {
			String responseContent = response.getBody().asString();
			System.out.println("Response Content:\n" + responseContent);

			throw assertionError;
		} catch (TesseractException tesseractException) {
			String responseContent = response.getBody().asString();
			tesseractException.printStackTrace();

			String desiredKey = "MID";
			Integer expectedValue = Credentails.ImageId;
			Integer extractedValue = extractValueFromText(responseContent, desiredKey);

			Assert.assertEquals(extractedValue, expectedValue, "MID value does not match the expected value.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tempImageFile != null) {
				tempImageFile.delete(); 
			}
		}
	}


	private Integer extractValueFromText(String text, String desiredKey) {
		String pattern = desiredKey + "\\s*:\\s*(\\d+)";
		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(text);

		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		}
		return null;

	}
	public void imageIdAssert(Response response) {

		Assert.assertEquals(response.getStatusCode(), 404);

		String responseMessage = response.getBody().jsonPath().getString("message").trim();
		String expectedMessage = "No Image Found for measurement ID:" + Credentails.InvalidImageId;
		//To avoid whitespace
		String whitespacePattern = "\\s*";
		responseMessage = responseMessage.replaceAll(whitespacePattern, "");
		expectedMessage = expectedMessage.replaceAll(whitespacePattern, "");



		Assert.assertTrue(responseMessage.contains(expectedMessage));

	}
	public void assertIdAndDate(Response response) {

		Assert.assertEquals(response.getStatusCode(), 200, "Unexpected status code");

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

	public static void calibrationAssert(Response response) {

		JsonPath jsonPath = response.jsonPath();
		
		String id=jsonPath.getString("id");

		System.out.println("calibration id "+id);

		String additionalinfo=jsonPath.getString("additionalInfo");
		
		
		System.out.println("calibration "+additionalinfo);

		Integer status=jsonPath.getInt("statusCode");

		Assert.assertEquals(additionalinfo, "System Calibrated Successfully.");

		Assert.assertEquals(status, 300);

		int statuscode=response.getStatusCode();

		Assert.assertEquals(statuscode, 200);
	}
	public static void measurementAssert(Response response) {

		JsonPath jsonPath = response.jsonPath();
		
		String id=jsonPath.getString("id");

		System.out.println("calibration id "+id);


		String additionalinfo=jsonPath.getString("additionalInfo");

		Assert.assertEquals(additionalinfo, "Object Measured Successfully");

		System.out.println("Measurement "+additionalinfo);

		Integer status=jsonPath.getInt("statusCode");
		Assert.assertEquals(status, 400);

		boolean statustorf=jsonPath.getBoolean("status");
		Assert.assertEquals(statustorf, true);


		int statuscode=response.getStatusCode();
		Assert.assertEquals(statuscode, 200);

	}
	
	public static void main(String[] args) {
		
	}
	
	
}
