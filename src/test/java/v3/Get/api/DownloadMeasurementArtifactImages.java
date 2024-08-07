package v3.Get.api;

import java.awt.Desktop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DownloadMeasurementArtifactImages {
	
	@Test
    public static void Download_Measurement_Artifact_Images_v3() {

        String[] tokens = PostAuth.getauth();
        String accessToken = tokens[0];

        RestAssured.baseURI = Credentails.baseurl;

        Response response = RestAssured.given()
                .pathParam("id", Credentails.artifactid)
                .header("Content-Type", "application/json")
                .header("System-Token", Credentails.systemid)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("v3/download-dimension-artifacts/{id}")
                .then()
                .extract()
                .response();

        if (response.getStatusCode() == 200) {
            // Extract the file name from the response headers
            String fileName = response.getHeader("Content-Disposition")
                    .replaceAll(".*filename=\"([^\"]+)\".*", "$1");

            // Download the zip file
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                response.getBody().asInputStream().transferTo(fos);
            } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            System.out.println("Zip file downloaded successfully: " + fileName);
            try {
				openFile(fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


        } else {
            System.out.println("Failed to download the zip file. Status code: " + response.getStatusCode());
        }
    }
    private static void openFile(String fileName) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(new File(fileName));
            }
        }
    }


}
