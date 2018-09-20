package com.chsoft.fabric;

import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
public class ClientManager {
	
	public static HFClient getPeerUserClient(FabricUser fabricUser) throws Exception{
		HFClient client = HFClient.createNewInstance();
		
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        
        client.setUserContext(fabricUser); 
        
		return client;
	}
	
	
}
