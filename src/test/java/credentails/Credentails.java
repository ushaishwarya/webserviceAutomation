package credentails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Credentails {
	
	public static String filepath="/home/usha/Downloads/measurements-2023-08-07 12-12-33.json";

	public static String systemid="1668776078569L2IFEtHqjr-H-KP5TILeo267Zwu";
	//forge-1684524307039d3s98zg70HxaPzI-K0sj11VqiyS
	
	public static String userid="1673253571738_9w3cqD7Fb7JxJHk8116pkThP1c";
	
	public static String secretid="3fe4ed66-3cfb-4524-abdb-b0cf777ffe91";
	
	//forge-520c1e79-3663-4db1-97a4-a8393cadba83
	
	public static String MacId="e4:5f:01:31:77:33";
	
	public static String id="103405";
	public static String ArtifactimageId="408";
	
	public static String from="2023-01-01";
	public static String to="2023-01-11";
	
	public static String sku="111";
	
	public static String v3="https://qa.vmeasure.cloud:6600/api/v3";
	public static String v2="https://qa.vmeasure.cloud:6600/api/v2";
	public static String v1="https://qa.vmeasure.cloud:6600/api/v1";
	
	public static String current_Time;
	
	
	
	
	public static String datas() {
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	current_Time=dateFormat.format(date);
	
	return current_Time;
}


}
