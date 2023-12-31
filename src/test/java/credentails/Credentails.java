package credentails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Credentails {

	public static String v1="https://stage.vmeasure.cloud:6600/api/v1";

	public static String v2="https://stage.vmeasure.cloud:6600/api/v2";

	public static String v3="https://stage.vmeasure.cloud:6600/api/v3";


	public static String filepath="/home/usha/Downloads/measurements-2023-09-08 19-25-34.json";

	public static String systemid="1665583342135BAZGyd9TTiGExWAAUEszYZUckZH";


	public static String userid="1673253571738_9w3cqD7Fb7JxJHk8116pkThP1c";

	public static String secretid="9f03de24-b049-4ecf-b193-3018e658bce3";	



	public static String referenceboxsku="123456";
	public static Integer ImageId =10891;

	public static String userFromDate="2023-09-06";
	public static String userToDate="2023-09-09";
	public static Integer userFromId=10884;

	public static Integer userToId=10891;

	public static Integer InvalidImageId=123;

	public static String Sku="suzume";

	public static String invalidSku="tika";

	public static String startDate="2023-09-06";
	public static String endDate="2023-09-09";
	public static String lessninty="2023-01-19";
	public static String artifactid="10891";
	public static String heightPref="1.5";
	public static String envelopeMod="false";

	public static Boolean status=true;
	public static Integer statusCode=600;
	public static String additionalInfo ="Reference box measured successfully";
	public static double length=23.5;
	public static double width=15.5;
	public static double height=8;
	public static String weightUni="kg";
	public static double actualWeight=0.35;


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






