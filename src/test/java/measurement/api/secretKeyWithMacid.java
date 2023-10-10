package measurement.api;

import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;

import java.util.Scanner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class secretKeyWithMacid {
	
	public static String token;
	public static String macid;
	
	@Test
	public static String[] shared_Key() {
		
		System.out.println("MacId:");
		
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(System.in);

		String macid=sc.nextLine();
		

		RestAssured.baseURI="https://qa.vmeasure.cloud:8000/device/v6";
		
		Response response = given()
				.queryParam("MacId",macid)
				
				.when()
					.get("/get-shared-secret");
		
		String Token = response.jsonPath().getString("Token");
        return new String [] {Token,macid};
//
	}


}
