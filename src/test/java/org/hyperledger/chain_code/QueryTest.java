package org.hyperledger.chain_code;

import java.util.Map;

public class QueryTest {
	
	public static String ip = "192.168.15.129";
	
	
	public static void main(String[] args) {
		ChaincodeManager c = null;
		FabricConfig fabricConfig = new FabricConfig();
		Peers peers = new Peers();
	    peers.setOrgName("Org1");
	    peers.setOrgMSPID("Org1MSP");
	    peers.setOrgDomainName("org1.example.com");
	    peers.addPeer("peer0.org1.example.com", 
	    		"peer0.org1.example.com", 
	    		"grpc://"+ip+":7051",
	    		"grpc://"+ip+":7053", 
	    		"grpc://"+ip+":7054");
	    fabricConfig.setPeers(peers);
	    Orderers orderers = new Orderers();
	    orderers.setOrdererDomainName("example.com");
	    orderers.addOrderer("orderer.example.com", "grpc://"+"192.168.15.143"+":7050");
	    fabricConfig.setOrderers(orderers);
	    Chaincode chaincode = new Chaincode();
	    //若节点上已经安装此chaincode，此处可天空字符串
	    chaincode.setChaincodePath("E:\\chsoft\\Git\\fabric-samples\\chaincode\\chaincode_example02\\go");
	    chaincode.setChannelName("mychannel");
	    chaincode.setChaincodeName("mycc");
	    chaincode.setChaincodeVersion("1.0");
	    fabricConfig.setChaincode(chaincode);
		try {
			 c = new ChaincodeManager(fabricConfig);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Map result ;
			String[] queryArg = new String[] {  "a" };  
			result = c.query("query",queryArg);
			String aa = result.get("data")!=null?result.get("data").toString():"空";
			System.out.println("查询结果");
			System.out.println("查询结果：a = "+aa);
			
			/*String[] invokeArg = new String[] { "b","a","10" };  
			Map m = c.invoke("invoke",invokeArg);
			System.out.println("修改完成");
			aa = result.get("data")!=null?result.get("data").toString():"空";
			System.out.println("修改后查询结果：a = "+aa);*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
