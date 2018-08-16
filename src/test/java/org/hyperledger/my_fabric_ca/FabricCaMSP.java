package org.hyperledger.my_fabric_ca;

import java.io.File;

public class FabricCaMSP extends MSP{
	
	public static final String cacertsName = "localhost-7054.pem";
	
	public static final String intermediatecertsName = "localhost-7054.pem";
	
	public static final String keystoreName = "abc_sk";
	
	public static final String signcertsName = "";
	
	public static final String tlscacertsName = "cert.pem";
	
	public FabricCaMSP(String mspDirectory) {
		super(mspDirectory);

		cacerts = mspDirectory+"cacerts"+Config.separator;
		
		intermediatecerts = mspDirectory+"intermediatecerts"+Config.separator;
		
		keystore = mspDirectory+"keystore"+Config.separator;
		
		signcerts = mspDirectory+"signcerts"+Config.separator;
		
		tlscacerts = mspDirectory+"tlscacerts"+Config.separator;
		
		File file1=new File(cacerts);
		file1.mkdirs();
		
		File file2=new File(intermediatecerts);
		file2.mkdirs();
		
		File file3=new File(keystore);
		file3.mkdirs();
		
		File file4=new File(signcerts);
		file4.mkdirs();
		
		File file5=new File(tlscacerts);
		file5.mkdirs();
	}
	
}
