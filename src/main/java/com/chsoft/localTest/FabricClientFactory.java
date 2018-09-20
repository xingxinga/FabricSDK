package com.chsoft.localTest;

import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import com.chsoft.fabric.FabricUser;

/**
 * 
* @ClassName: FabricClientFactory 
* @Description: TODO(Fabric客户端的生产工厂) 
* @author lixing  
* @date 2018年9月13日 下午5:25:16 
* @version V1.0
 */
public class FabricClientFactory {
	
	public static HFClient getPeerUserClient(FabricUser fabricUser) throws Exception{
		HFClient client = HFClient.createNewInstance();
		
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        
        client.setUserContext(fabricUser); 
        
		return client;
	}
}
