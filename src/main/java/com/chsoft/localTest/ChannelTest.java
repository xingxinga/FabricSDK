package com.chsoft.localTest;

import java.io.File;

import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.ChannelConfiguration;

import com.chsoft.newFabric.FabricChaincode;
import com.chsoft.newFabric.FabricClient;
import com.chsoft.newFabric.FabricOrderer;
import com.chsoft.newFabric.FabricPeer;
import com.chsoft.newFabric.FabricUser;;

public class ChannelTest {
	
	public E2e_Config config;
	public FabricUser org1Admin;
	public FabricOrderer orderer;
	public FabricClient fabricClient;
	public FabricPeer fabricPeer;
	public ChannelTest() throws Exception{
		config = new E2e_Config();
		org1Admin = config.getAdminList().get(0);
		orderer = config.getOrdererList().get(0);
		fabricClient = new FabricClient(org1Admin);
		fabricPeer = config.getPeerList().get(0).get(0);
	}
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		ChannelTest channelTest= new ChannelTest();
		//channelTest.testCreateChannel();
		//channelTest.testChannelJoin();
		//channelTest.testQueryChannels();
		channelTest.testPeerInstallChaincodePath();
		//channelTest.testQueryInstalledChaincodes();
		//channelTest.testInstantiateChainCode();
	}
	
	public void testCreateChannel() throws Exception{
		File configFile = new File("E:\\chsoft\\Git\\fabric-samples\\first-network\\channel-artifacts\\channel.tx");
	    ChannelConfiguration channelConfiguration = new ChannelConfiguration(configFile);
		fabricClient.createChannel("mychannel", orderer, channelConfiguration);
		
	}
	
	public void testChannelJoin() throws Exception{
		FabricClient fabricClient = new FabricClient(org1Admin);
		fabricClient.channelJoinPeer(fabricClient.getRunningChannel("mychannel", orderer), fabricPeer);
	}
	
	public void testQueryChannels() throws Exception{
		System.out.println(fabricClient.queryChannels(org1Admin, fabricPeer));
	}
	
	public void testPeerInstallChaincodePath() throws Exception{
		FabricChaincode fabricChaincode = new FabricChaincode();
		fabricChaincode.setChaincodeName("myccb");
		fabricChaincode.setChaincodePath("github.com/chaincode/chaincode_example02/go/");
		fabricChaincode.setChaincodeVersion("1.0");
		fabricChaincode.setChaincodeFilePath("chaincode\\chaincode_example02\\go\\");
		fabricClient.peerInstallChaincode(fabricChaincode, fabricPeer);
	}
	
	public void testQueryInstalledChaincodes() throws Exception{
		fabricClient.queryInstalledChaincodes(fabricPeer);
	}
	
	public void testInstantiateChainCode() throws Exception{
		FabricChaincode fabricChaincode = new FabricChaincode();
		fabricChaincode.setChaincodeName("mycc");
		fabricChaincode.setChaincodePath("E:\\chsoft\\Git\\fabric-samples\\");
		fabricChaincode.setChaincodeVersion("1.0");
		fabricChaincode.setChaincodeFilePath("chaincode\\chaincode_example02\\go\\");
	    ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
	    chaincodeEndorsementPolicy.fromYamlFile(new File(config.channlePath+"chaincodeendorsementpolicy.yaml"));
	    fabricClient.peerInstantiateChainCode(fabricClient.getRunningChannel("mychannel", orderer),
	    								      fabricPeer,fabricChaincode, chaincodeEndorsementPolicy);
	}
	
	

}
