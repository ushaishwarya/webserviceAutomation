package measurement.api;

import java.io.File;

import java.util.Scanner;

import org.json.JSONObject;
import org.testng.annotations.Test;

import credentails.Credentails;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class addMeasurementDetails {
    @Test
    public void postmethod() {
        
        String[] tokenAndMacid = secretKeyWithMacid.shared_Key();
        
        String token = tokenAndMacid[0]; 
        String macid = tokenAndMacid[1]; 


        
        String current_Time = Credentails.datas();

        Scanner sc = new Scanner(System.in);

        System.out.println("Measurement Count:");
        int measurementcount = sc.nextInt();
        sc.nextLine(); 

        System.out.println("Measurement id:");
        int measurementid = sc.nextInt();
        sc.nextLine(); 
        
        System.out.println("PassOrFail:");
        Boolean status = sc.nextBoolean();
        sc.nextLine(); 

        System.out.println("sku:");
        String sku = sc.nextLine();

            for (int i = measurementid; i< measurementid + measurementcount; i++) {

                RestAssured.baseURI ="https://qa.vmeasure.cloud:8000/device/v6";
                Response response = RestAssured.given()
                        .header("Authorization", "Bearer " + token)
                        .contentType("multipart/form-data")
                        .multiPart("annotatedImage", new File("/home/usha/Pictures/tika.png"))
                        .multiPart("id", i)
                        .multiPart("status", status)
                        .multiPart("dimensionUnit", "cm")
                        .multiPart("length", "13.0")
                        .multiPart("width", "14.0")
                        .multiPart("height", "15.0")
                        .multiPart("weightUnit", "kg")
                        .multiPart("sku", sku)
                        .multiPart("cubicVolume","10")
                        .multiPart("realVolume","10")
                        .multiPart("statusCode","400")
                        .multiPart("actualWeight", "27")
                        .multiPart("additionalInfo", "Object Measured Successfully")
                        .multiPart("MacId", macid)
                        .multiPart("volumetricWeight", "36")
                        .multiPart("scannedOn", current_Time)
                        .multiPart("scannedTimeZone", "Asia/Kolkata")
                        .when()
                        .post("/add-measurement-details");

                JSONObject jsonResponse = new JSONObject(response.getBody().asPrettyString());
                String message = jsonResponse.getString("message");

                System.out.println( "measurementid"+i+ ":" + message);
            }
        }
    }

