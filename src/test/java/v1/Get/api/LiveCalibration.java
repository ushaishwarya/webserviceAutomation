package v1.Get.api;


import org.testng.annotations.Test;


import credentails.Credentails;
import credentails.CommonMethods;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LiveCalibration extends CommonMethods{
	
	@Test
	public static void CalibratedSuccessfully () throws InterruptedException {
        Thread.sleep(3000); // Time is in milliseconds

		RestAssured.baseURI=Credentails.v1;
		Response response= RestAssured.given()
			.header("systemid",Credentails.systemid)
			.header("userid",Credentails.userid)
			
			.queryParam("heightPref",Credentails.heightPref)
			.queryParam("unitPref","m")
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("/calibrate");
		
		
		calibrationAssert(response);      

		
	}
	@Test
	public static void badrequest() {
		
	
        for (int i = 1; i < 2; i++) {
        	
    		String heightPref="";
    		String unitpref="";

            if (i == 1) {
            	unitpref = "";
            } else if (i == 2) {
            	unitpref="wdsse";
            	
           }
		
		RestAssured.baseURI=Credentails.v1;
		Response response= RestAssured.given()
			.header("systemid",Credentails.systemid)
			.header("userid",Credentails.userid)
			
			.queryParam("heightPref",heightPref)
			.queryParam("unitPref",unitpref)
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("/calibrate");
		
        assertMessageAndStatuscode(response, "Please send the preferable height and unit", 400);        
		
	}
}
	@Test
	public static void invalidheight() {
		
	
        for (int i = 1; i < 2; i++) {
        	
    		String heightPref="";

            if (i == 1) {
            	heightPref = "1222";
            	
           }
		
		RestAssured.baseURI=Credentails.v1;
		Response response= RestAssured.given()
			.header("systemid",Credentails.systemid)
			.header("userid",Credentails.userid)
			
			.queryParam("heightPref",heightPref)
			.queryParam("unitPref","meter")
			.queryParam("envelopeMod",Credentails.envelopeMod)
			
		.when()
			.get("/calibrate");
		
        
        assertMessageAndStatuscode(response, "Enter valid heightPref either 1.1, 1.5 or 2.2", 400);              
		
	}

	}
}
