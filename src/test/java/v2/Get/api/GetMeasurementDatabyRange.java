package v2.Get.api;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import credentails.Credentails;
import io.restassured.RestAssured;
import io.restassured.response.Response;


public class GetMeasurementDatabyRange {
    @Test(priority = 1)
    @Parameters({ "userFromDate", "userToDate" })
	  public  void assertthedaterange(String userFromDate, String userToDate) {

	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	        try {
	            Date fromDate = dateFormat.parse(userFromDate);
	            Date toDate = dateFormat.parse(userToDate);

	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(fromDate);
	            calendar.set(Calendar.MONTH, Calendar.JANUARY);
	            calendar.set(Calendar.DAY_OF_MONTH, 1);
	            calendar.set(Calendar.HOUR_OF_DAY, 0);
	            calendar.set(Calendar.MINUTE, 0);
	            calendar.set(Calendar.SECOND, 0);
	            Date validFromDate = calendar.getTime();

	            calendar.setTime(toDate);
	            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
	            calendar.set(Calendar.DAY_OF_MONTH, 31);
	            calendar.set(Calendar.HOUR_OF_DAY, 23);
	            calendar.set(Calendar.MINUTE, 59);
	            calendar.set(Calendar.SECOND, 59);
	            Date validToDate = calendar.getTime();

	            Date fromDate1 = dateFormat.parse(userFromDate);
	            Date toDate1 = dateFormat.parse(userToDate);

	            if (isValidDateRange(fromDate1, toDate1, validFromDate, validToDate)) {
	                System.out.println("Date range is valid.");

	                // API Request using RestAssured
	                RestAssured.baseURI = Credentails.v2;

	                Response response = RestAssured.given()
	                        .header("systemid", Credentails.systemid)
	                        .header("userid", Credentails.userid)
	                        .queryParam("range", "Date")
	                        .queryParam("from", userFromDate)
	                        .queryParam("to", userToDate)
	                        .when()
	                        .get("/dimension")
	                        .then()
	                        .extract()
	                        .response();
	                

	                List<String> scannedOnTimes = response.jsonPath().getList("scannedOn");
	                
	                if(scannedOnTimes==null) {
	                	System.out.println(response.asPrettyString());
	                }
	                
//	                System.out.println(scannedOnTimes);

	             // Inside the for loop where you parse scannedOnTimes
	                List<Date> scannedDates = new ArrayList<>();
	                for (String scannedTime : scannedOnTimes) {
	                    Date scannedDate = new SimpleDateFormat("E MMM dd, yyyy hh:mm:ss a").parse(scannedTime);
	                    String formattedScannedDate = dateFormat.format(scannedDate); // Format the scanned date using dateFormat
//	                    System.out.println(formattedScannedDate); // Debug print to check the formatted date
	                    scannedDates.add(dateFormat.parse(formattedScannedDate)); // Parse the formatted date back to Date
	                }
	                

	                boolean allScannedDatesValid = true; // Flag to track validity of all scanned dates

	                for (Date scannedDate : scannedDates) {
	                    if (!isValidDateRange(scannedDate, scannedDate, fromDate, toDate)) {
	                        allScannedDatesValid = false;
	                        break; // No need to continue checking if any scanned date is invalid
	                    }
	                }

	                if (allScannedDatesValid) {
	                    System.out.println("All scanned dates are within the entered date range.");
	                } else {
	                    System.out.println("Some scanned dates are not within the entered date range.");
	                }
	    	        int statuscode=response.getStatusCode();
	    	        
	    	        Assert.assertEquals(statuscode, 200);



	            } else {
	                System.out.println("Date range is not valid.");
	            }
	        } catch (ParseException e) {
	            System.out.println("Invalid date format.");
	    }
    }
	    public static boolean isValidDateRange(Date fromDate, Date toDate, Date validFromDate, Date validToDate) {
	        return fromDate.compareTo(validFromDate) >= 0 && toDate.compareTo(validFromDate) >= 0 &&
	                fromDate.compareTo(validToDate) <= 0 && toDate.compareTo(validToDate) <= 0;
	    }
	    @Test(priority=2)
	    @Parameters({ "userFromId", "userToId" })

		  public  void assertTheIdRange(String userFromIdInput, String userToIdInput) {
	    	int userFromId;
	    	int userToId;

	    	try {
	    	    userFromId = Integer.parseInt(userFromIdInput);
	    	    userToId = Integer.parseInt(userToIdInput);
	    	    
	    	    System.out.println("ID range is valid.");
		                // API Request using RestAssured
		                RestAssured.baseURI = Credentails.v2;

		                Response response = RestAssured.given()
		                        .header("systemid", Credentails.systemid)
		                        .header("userid", Credentails.userid)
		                        .queryParam("range", "id")
		                        .queryParam("from", userFromId)
		                        .queryParam("to", userToId)
		                        .when()
		                        .get("/dimension")
		                        .then()
		                        .extract()
		                        .response();
//		                System.out.println(response.asPrettyString());

		             // Parse the JSON response and get the IDs
		                List<Integer> measurementIds = response.jsonPath().getList("id", Integer.class);
		                
//		                System.out.println(measurementIds);
		                
		                boolean allmeasurementidValid = true; // Flag to track validity of all scanned dates

		                // Compare the IDs with the entered ID range
		                for (Integer measurementId : measurementIds) {
		                    if (!isValidIdRange(measurementId, userFromId, userToId)) {
		                    	allmeasurementidValid = false;
		                        break; // No need to continue checking if any scanned date is invalid
		                    }
		                }

		                if (allmeasurementidValid) {
		                    System.out.println("All Measurement id are within the entered id range.");
		                } else {
		                    System.out.println("Some Measurement id are not within the entered id range.");
		                }
		    	        int statuscode=response.getStatusCode();
		    	        
		    	        Assert.assertEquals(statuscode, 200);


		                  
		             
	    	} catch (NumberFormatException e) {
	    	    System.out.println("Invalid ID format. Please enter valid integer IDs.");
	    }
	    }
        public static boolean isValidIdRange(int idToCheck, int fromId, int toId) {
            return idToCheck >= fromId && idToCheck <= toId;
        }
      @Test(priority=3)
	  @Parameters({ "userFromId", "userToId" })

  	  public  void invalidRangeValue(String userFromIdInput, String userToIdInput) {
          RestAssured.baseURI = Credentails.v2;

          Response response = RestAssured.given()
                  .header("systemid", Credentails.systemid)
                  .header("userid", Credentails.userid)
                  .queryParam("range", "2eefe")
                  .queryParam("from",userFromIdInput)
                  .queryParam("to", userToIdInput)
                  .when()
                  .get("/dimension")
                  .then()
                  .extract()
                  .response();
          

       // Parse the JSON response and get the IDs
          String message = response.jsonPath().getString("message");
          
          Assert.assertEquals(message, "Invalid Range value");
          
	        int statuscode=response.getStatusCode();
	        
	        Assert.assertEquals(statuscode, 400);
  		  
  	  }
      
      @Test(priority=4)
	  @Parameters({ "userFromId", "userToId" })

  	  public  void invalidFromOrToValue(String userFromIdInput, String userToIdInput) {
          int numIterations = 4;
          for (int i = 1; i <= numIterations; i++) {
        	  String range="Date";
              String from = userFromIdInput;
              String to = userToIdInput;
              

              if (i == 1) {
                  range = "Date";
              } else if (i == 2) {
                  from = "eww";
              } else if (i == 3) {
                  to = "3433";
              }else if (i == 4) {
                  from = "103300"; // Using a valid date format
                  to = "103400";   // Using a valid date format

          }


          RestAssured.baseURI = Credentails.v2;

          Response response = RestAssured.given()
                  .header("systemid", Credentails.systemid)
                  .header("userid", Credentails.userid)
                  .queryParam("range", range)
                  .queryParam("from",from)
                  .queryParam("to", to)
                  .when()
                  .get("/dimension")
                  .then()
                  .extract()
                  .response();
          
//          System.out.println(response.asPrettyString());

          String message = response.jsonPath().getString("message");
          
          Assert.assertEquals(message, "Either FROM or TO Date is invalid.");
          
	        int statuscode=response.getStatusCode();
	        
	        Assert.assertEquals(statuscode, 400);

          }
      
      }
      @Test(priority = 5)
      @Parameters({ "userFromId", "userToId" })
      public void Unauthorized(String userFromIdInput, String userToIdInput) {
          int userFromId;
          int userToId;

          int numIterations = 3;

          for (int i = 1; i < numIterations; i++) {
              String systemId = Credentails.systemid;
              String userId = Credentails.userid;

              if (i == 1) {
                  systemId = "";
              } else if (i == 2) {
                  userId = "";
              } else if (i == 3) {
                  systemId = "";
                  userId = "";
              }
              
              userFromId = Integer.parseInt(userFromIdInput);
              userToId = Integer.parseInt(userToIdInput);

              RestAssured.baseURI = Credentails.v2;

              Response response = RestAssured.given()
                      .header("systemid", systemId)
                      .header("userid", userId)
                      .queryParam("range", "id")
                      .queryParam("from", userFromId)
                      .queryParam("to", userToId)
                      .when()
                      .get("/dimension")
                      .then()
                      .extract()
                      .response();

              JSONObject jsonResponse = new JSONObject(response.getBody().asString());

              String message = jsonResponse.getString("message");

              String expectedMessage = "Unauthorized!";

              Assert.assertEquals(message, expectedMessage, "Unauthorized message is not matched");
              int statusCode = response.getStatusCode();

              Assert.assertEquals(statusCode, 401, "Correct status code not returned");
          }
      }


}
