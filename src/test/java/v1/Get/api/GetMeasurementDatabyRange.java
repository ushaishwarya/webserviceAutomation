package v1.Get.api;

import java.text.ParseException;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;


import credentails.Credentails;
import credentails.CommonMethods;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetMeasurementDatabyRange extends CommonMethods{
    @Test(priority = 1)
	  public  void assertthedaterange() {

	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	        try {
	            Date fromDate = dateFormat.parse(Credentails.userFromDate);
	            Date toDate = dateFormat.parse(Credentails.userToDate);

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

	            Date fromDate1 = dateFormat.parse(Credentails.userFromDate);
	            Date toDate1 = dateFormat.parse(Credentails.userToDate);

	            if (isValidDateRange(fromDate1, toDate1, validFromDate, validToDate)) {

	                RestAssured.baseURI = Credentails.v1;

	                Response response = RestAssured.given()
	                        .header("systemid", Credentails.systemid)
	                        .header("userid", Credentails.userid)
	                        .queryParam("range", "Date")
	                        .queryParam("from", Credentails.userFromDate)
	                        .queryParam("to", Credentails.userToDate)
	                        .when()
	                        .get("/dimension")
	                        .then()
	                        .extract()
	                        .response();
	                

	                List<String> scannedOnTimes = response.jsonPath().getList("scannedOn");
	                
	                if(scannedOnTimes==null) {
	                	System.out.println(response.asPrettyString());
	                }
	                

	                List<Date> scannedDates = new ArrayList<>();
	                for (String scannedTime : scannedOnTimes) {
	                    Date scannedDate = new SimpleDateFormat("E MMM dd, yyyy hh:mm:ss a").parse(scannedTime);
	                    String formattedScannedDate = dateFormat.format(scannedDate); 
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

		  public  void assertTheIdRange() {

	    	try {
		                RestAssured.baseURI = Credentails.v1;

		                Response response = RestAssured.given()
		                        .header("systemid", Credentails.systemid)
		                        .header("userid", Credentails.userid)
		                        .queryParam("range", "id")
		                        .queryParam("from", Credentails.userFromId)
		                        .queryParam("to", Credentails.userToId)
		                        .when()
		                        .get("/dimension")
		                        .then()
		                        .extract()
		                        .response();

		                List<Integer> measurementIds = response.jsonPath().getList("id", Integer.class);
		                
		                if(measurementIds==null) {
		                	System.out.println(response.asPrettyString());
		                }

		                
		                
		                boolean allmeasurementidValid = true; 

		                // Compare the IDs with the entered ID range
		                for (Integer measurementId : measurementIds) {
		                    if (!isValidIdRange(measurementId, Credentails.userFromId, Credentails.userToId)) {
		                    	allmeasurementidValid = false;
		                        break;
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

  	  public  void invalidRangeValue() {
          RestAssured.baseURI = Credentails.v1;

          Response response = RestAssured.given()
                  .header("systemid", Credentails.systemid)
                  .header("userid", Credentails.userid)
                  .queryParam("range", "2eefe")
                  .queryParam("from",Credentails.userFromDate)
                  .queryParam("to", Credentails.userToDate)
                  .when()
                  .get("/dimension")
                  .then()
                  .extract()
                  .response();
          
          assertMessageAndStatuscode(response, "Invalid Range value", 400);
  		  
  	  }
      
      @Test(priority=4)
  	  public  void invalidFromOrToValue() {
          int numIterations = 4;
          for (int i = 1; i <= numIterations; i++) {
        	  String range="Date";
              Integer from = Credentails.userToId;
              Integer to = Credentails.userToId;
              
              String userfrom = Integer.toString(from);
              String userto = Integer.toString(to);

              

              

              if (i == 1) {
                  range = "Date";
              } else if (i == 2) {
            	  userfrom = "eww";
              } else if (i == 3) {
                  userto = "3433";
              }else if (i == 4) {
                  userfrom = "103300"; 
                  userto = "103400";   

          }


          RestAssured.baseURI = Credentails.v1;

          Response response = RestAssured.given()
                  .header("systemid", Credentails.systemid)
                  .header("userid", Credentails.userid)
                  .queryParam("range", range)
                  .queryParam("from",userfrom)
                  .queryParam("to", userto)
                  .when()
                  .get("/dimension")
                  .then()
                  .extract()
                  .response();
          
	        
	        assertMessageAndStatuscode(response, "Either FROM or TO Date is invalid.", 400);


          }
      
      }
      @Test(priority = 5)
      public void Unauthorized() {
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
              

              RestAssured.baseURI = Credentails.v1;

              Response response = RestAssured.given()
                      .header("systemid", systemId)
                      .header("userid", userId)
                      .queryParam("range", "id")
                      .queryParam("from", Credentails.userFromId)
                      .queryParam("to", Credentails.userToId)
                      .when()
                      .get("/dimension")
                      .then()
                      .extract()
                      .response();

              assertMessageAndStatuscode(response, "Unauthorized!", 401);
          }
      }
}


