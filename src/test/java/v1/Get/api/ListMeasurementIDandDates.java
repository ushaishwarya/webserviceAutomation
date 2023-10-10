package v1.Get.api;

import org.testng.annotations.Test;

import credentails.Credentails;
import credentails.CommonMethods;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ListMeasurementIDandDates extends CommonMethods {
    @Test
    public void ScanIdListEndpoint() {
    	
        RestAssured.baseURI = Credentails.baseurl;

        Response response = RestAssured
        		.given()
                		.header("systemid", Credentails.systemid)
                				.header("userid", Credentails.userid)
                .when()
                		.get("v1/scanidlist")
                .then()
                		.extract()
                			.response();
       assertIdAndDate(response);
    }
    @Test
	public void Unauthorized() {

	    RestAssured.baseURI = Credentails.baseurl;
	    
	    Response response = RestAssured
	    		
	            .given()
	            		.header("systemid", "")
	            				.header("userid", "")
	            .when()
                		.get("v1/scanidlist")
	            .then()
	            	.extract()
	            		.response();
	    
	    
	    assertMessageAndStatuscode(response, "Unauthorized!", 401);

		}
    @Test
	public void Notfound() {

	    RestAssured.baseURI = Credentails.baseurl;
	    
	    Response response = RestAssured
	    		
	            .given()
	            		.header("systemid", Credentails.systemid)
	            				.header("userid", Credentails.userid)
	            .when()
                		.get("/")
	            .then()
	            		.extract()
	            				.response();
	    
	    assertMessageAndStatuscode(response, "Not found", 404);

		}

    }



