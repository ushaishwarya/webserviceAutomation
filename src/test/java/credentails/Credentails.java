package credentails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Credentails {

	public static String baseurl="https://stage.vmeasure.cloud:6600/api/";
	
	public static String heightPref="1.5";

	public static String referenceboxsku="nisimiya";
	
	public static String artifactid="12762";

	public static Integer ImageId =12762;



	public static String filepath="/home/usha/Downloads/measurements-2023-10-06 13-19-03.json";
	
	public static String pathtostoreresponse="/home/usha/Music/ApiAutomationResponse.json";

	public static String systemid="1665583342135BAZGyd9TTiGExWAAUEszYZUckZH";//qa-1694515189076AUn3watP_rY7a6TEFX2i25X7-4Y

//1692709795765Whp_NJjh3KVaCbuKJypnt4sa9w1

	public static String userid="1673253571738_9w3cqD7Fb7JxJHk8116pkThP1c";//taki-1673253571738_9w3cqD7Fb7JxJHk8116pkThP1c

	public static String secretid="9f03de24-b049-4ecf-b193-3018e658bce3";//taki-3fe4ed66-3cfb-4524-abdb-b0cf777ffe91



	public static String Sku="suzumitaki";

	public static String userFromDate="2023-10-05";
	public static String userToDate="2023-10-06";
	public static Integer userFromId=12475;

	public static Integer userToId=12481;

	public static Integer InvalidImageId=25;


	public static String invalidSku="tika";

	public static String startDate="2023-10-05";
	public static String endDate="2023-10-06";
	public static String lessninty="2023-01-19";
	public static String envelopeMod="false";

	public static Boolean status=true;
	public static Integer statusCode=600;
	public static String additionalInfo ="Reference box measured successfully";


	public static String datas() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		Date current_Time = new Date();

		String formattedTime = dateFormat.format(current_Time);

		return formattedTime;
	}

	public static void main(String[] args) {
		String currentTime = datas();
		System.out.println("Current Time: " + currentTime);
	}
}






