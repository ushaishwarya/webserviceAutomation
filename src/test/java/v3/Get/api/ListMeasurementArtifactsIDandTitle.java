package v3.Get.api;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.PostAuth;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ListMeasurementArtifactsIDandTitle {
    public static List<Integer> responseList = new ArrayList<>();
    
    
    @Test
    public static List<Integer> List_Measurement_Artifacts_ID_and_Title_200status() {

        String[] tokens = PostAuth.getauth();
        String accessToken = tokens[0];

        RestAssured.baseURI = Credentails.v3;

        Response response = RestAssured.given()
            .pathParam("id", "6295")
            .header("Content-Type", "application/json")
            .header("System-Token", Credentails.systemid)
            .header("Authorization", "Bearer " + accessToken)
        .when()
            .get("/dimension-artifacts-list/{id}")
        .then()
            .extract()
            .response();

        System.out.println(response.asPrettyString());

        List<Integer> additional_image_id = response.jsonPath().getList("additional_image_id");

        int loopLimit = Math.min(10, additional_image_id.size()); // Avoid going beyond list length
        for (int i = 0; i < loopLimit; i++) {
            Integer responseItem = additional_image_id.get(i);
            responseList.add(responseItem);
        }

        return responseList;
    }
}
