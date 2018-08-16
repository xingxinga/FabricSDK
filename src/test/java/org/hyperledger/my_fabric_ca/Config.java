package org.hyperledger.my_fabric_ca;

public class Config {
	
	private static String osName = System.getProperties().getProperty("os.name");
	
	public static String wrap = Config.getWrap();
	
	public static String separator = Config.getSeparator();
	
	public static String getWrap(){
		if(osName.equals("Linux")){
            return "\r\n";
        }
        else{
        	return "\n";
        }
	}
	
	public static String getSeparator(){
		if(osName.equals("Linux")){
            return "/";
        }
        else{
        	return "\\";
        }
	}
	
	
}
