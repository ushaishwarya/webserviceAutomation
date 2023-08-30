package v3.Get.api;

import org.testng.annotations.Test;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetMeasurementArtifactImage extends ListMeasurementArtifactsIDandTitle{
	@Test
	public static void Get_Measurement_Artifact_Image_v3() {
		
        List<Integer> responses = List_Measurement_Artifacts_ID_and_Title_200status();
        

		
		String[] tokens = PostAuth.getauth();
		String accessToken = tokens[0];
		
		RestAssured.baseURI=Credentails.v3;
	    for (Integer additionalImageId : responses) {

        Response response = RestAssured
		
		.given()
			.queryParam("measurement_id","6295")
            .queryParam("additional_image_id",additionalImageId) // Convert List to Array
			
			.header("Content-Type","application/json")
			.header("System-Token",Credentails.systemid)
			.header("Authorization","Bearer "+accessToken)
			
		.when()
			.get("/dimension-additional-image")
			
		.then()
			.extract().response();

        
        // Read the response as bytes
        byte[] imageBytes = response.asByteArray();

        try {
        	
        	// Generate a unique file name based on the current timestamp
            String fileName = "image_" + System.currentTimeMillis() + ".jpg";

            // Convert the image bytes to BufferedImage
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

            // Save the image as JPG file
            File outputFile = new File(fileName);
            ImageIO.write(image, "jpg", outputFile);
            
            System.out.println("Image saved successfully as"+fileName);

            
            // Open the saved image file using the default image viewer
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(outputFile);
            } else {//        System.out.println(responses);

                System.out.println("Opening the image automatically is not supported on this platform.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	

	}
	
	}
