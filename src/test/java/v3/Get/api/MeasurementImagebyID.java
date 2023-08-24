package v3.Get.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class MeasurementImagebyID {
	static String[] tokens = PostAuth.getauth();
	static String accessToken = tokens[0];

    @Test
    @Parameters("ImageId")
    public void VeridyTheIdByImage(Integer ImageId) {
        RestAssured.baseURI = Credentails.v3;
        Response response = RestAssured
                .given()
                .pathParam("id", ImageId)
        		.header("System-Token",Credentails.systemid)
        		.header("Authorization","Bearer "+accessToken)
                .when()
                .get("/image/{id}")
                .then()
                .extract().response();
        int statuscode=response.getStatusCode();
        
        Assert.assertEquals(statuscode, 200);


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
            //To print extracted image
//            System.out.println("Extracted Text:\n" + extractedText);

            String desiredKey = "MID";
            Integer expectedValue = ImageId;
            Integer extractedValue = extractValueFromText(extractedText, desiredKey);

            Assert.assertEquals(extractedValue, expectedValue, "MID value does not match the expected value.");
            
        } catch (AssertionError assertionError) {
            // Print the response content
            String responseContent = response.getBody().asString();
            System.out.println("Response Content:\n" + responseContent);
            
            // Re-throw the assertion error
            throw assertionError;
        } catch (TesseractException tesseractException) {
            // Print the response content
            String responseContent = response.getBody().asString();
            //To print the extracted text
//            System.out.println("Response Content:\n" + responseContent);
            
            // Print the Tesseract exception details
            tesseractException.printStackTrace();
            
            // Continue with the assertion (you can add your desired logic here)
            String desiredKey = "MID";
            Integer expectedValue = ImageId;
            Integer extractedValue = extractValueFromText(responseContent, desiredKey);

            Assert.assertEquals(extractedValue, expectedValue, "MID value does not match the expected value.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tempImageFile != null) {
                tempImageFile.delete(); // Make sure to delete the temporary file
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

    @Test
    @Parameters("InvalidImageId")

    public void testNoImageFound(Integer InvalidImageId) {
        
        RestAssured.baseURI = Credentails.v1;
        Response response = RestAssured
                .given()
                .pathParam("id", InvalidImageId)
                .header("systemid", Credentails.systemid)
                .header("userid", Credentails.userid)
                .when()
                .get("/image/{id}")
                .then()
                .extract().response();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(404, statusCode);

        String responseMessage = response.getBody().jsonPath().getString("message").trim();
        String expectedMessage = "No Image Found for measurement ID:" + InvalidImageId;
        //To avoid whitespace
        String whitespacePattern = "\\s*";
        responseMessage = responseMessage.replaceAll(whitespacePattern, "");
        expectedMessage = expectedMessage.replaceAll(whitespacePattern, "");



        Assert.assertTrue(responseMessage.contains(expectedMessage));
    }
    @Test
    @Parameters("InvalidImageId")

	public void Unauthorized(Integer InvalidImageId) {

    RestAssured.baseURI = Credentails.v3;
    Response response = RestAssured
            .given()
            .pathParam("id", InvalidImageId)
    		.header("System-Token","")
    		.header("Authorization","Bearer "+"")
            .when()
            .get("/image/{id}")
            .then()
            .extract().response();
    
    int statuscode=response.getStatusCode();
    Assert.assertEquals(statuscode, 401);
    
    JSONObject jsonResponse = new JSONObject(response.getBody().asString());
    String message = jsonResponse.getString("message");
    String excepted_message="Unauthorized!";
    Assert.assertEquals(excepted_message, message);

	}


}
