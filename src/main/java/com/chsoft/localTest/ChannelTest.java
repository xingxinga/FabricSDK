package com.chsoft.localTest;

import java.io.File;

import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.ChannelConfiguration;
import org.springframework.stereotype.Component;

import com.chsoft.fabric.FabricChaincode;
import com.chsoft.fabric.FabricClient;
import com.chsoft.fabric.FabricOrderer;
import com.chsoft.fabric.FabricPeer;
import com.chsoft.fabric.FabricUser;;
@Component
public class ChannelTest {
	
	public E2e_Config config;
	public FabricUser org1Admin;
	public FabricOrderer orderer;
	
	public FabricClient fabricClient;
	public FabricPeer fabricPeer;
	public FabricChaincode fabricChaincode;
	public static String channelName = "mychannel";
	public ChannelTest() throws Exception{
		config = new E2e_Config();
		org1Admin = config.getAdminList().get(0);
		orderer = config.getOrdererList().get(0);
		fabricClient = new FabricClient(org1Admin);
		fabricPeer = config.getPeerList().get(0).get(0);
		fabricChaincode = config.getFabricChaincode("mycc");
	}
	
	public void init() throws Exception{
		config = new E2e_Config();
		org1Admin = config.getAdminList().get(0);
		orderer = config.getOrdererList().get(0);
		fabricClient = new FabricClient(org1Admin);
		fabricPeer = config.getPeerList().get(0).get(0);
		fabricChaincode = config.getFabricChaincode("mycc");
	}
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		ChannelTest channelTest= new ChannelTest();
		//channelTest.testCreateChannel();
		//channelTest.testChannelJoin();
		channelTest.testQueryChannels();
		//channelTest.testQueryInstalledChaincodes();
		
		//channelTest.testPeerInstallChaincodePath();
		
		//channelTest.testInstantiateChainCode();
		
		//channelTest.testQueryChainCode();
		
		//channelTest.testQueryInstantiateChainCode();
	}
	
	public void testCreateChannel() throws Exception{
		String configFilePath = "E:\\chsoft\\Git\\fabric-samples\\first-network\\channel-artifacts\\channel.tx";
		
		ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(configFilePath));
		
		fabricClient.createChannel("mychannel", orderer, channelConfiguration);
		
	}
	
	public void testChannelJoin() throws Exception{
		fabricClient.channelJoinPeer(fabricClient.getRunningChannel(channelName, orderer), fabricPeer);
	}
	
	public void testQueryChannels() throws Exception{
		System.out.println(fabricClient.queryChannels(org1Admin, fabricPeer));
	}
	
	public void testPeerInstallChaincodePath() throws Exception{
		fabricClient.peerInstallChaincode(fabricChaincode, fabricPeer);
	}
	
	public void testQueryInstalledChaincodes() throws Exception{
		fabricClient.queryInstalledChaincodes(fabricPeer);
	}
	
	public void testInstantiateChainCode() throws Exception{
	    ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
	    chaincodeEndorsementPolicy.fromYamlFile(new File(config.channlePath+"chaincodeendorsementpolicy.yaml"));
	    fabricClient.peerInstantiateChainCode(fabricClient.getRunningChannel(channelName, orderer),fabricPeer,fabricChaincode, chaincodeEndorsementPolicy);
	}
	
	public void testQueryChainCode() throws Exception{
		String[] queryArg = new String[] {  "a" };  
		fabricClient.queryChaincode(fabricClient.getRunningChannel(channelName, null),fabricPeer, fabricChaincode, "query",queryArg);
	}
	
	public void testQueryInstantiateChainCode() throws Exception{
		fabricClient.queryInstantiateChaincodes(fabricClient.getRunningChannel(channelName, null), fabricPeer);
	}
}
