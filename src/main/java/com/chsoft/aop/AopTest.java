package com.chsoft.aop;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hyperledger.fabric.protos.peer.Query.ChaincodeInfo;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chsoft.fabric.FabricChaincode;
import com.chsoft.fabric.FabricOrderer;
import com.chsoft.fabric.FabricPeer;
import com.chsoft.fabric.FabricUser;
import com.chsoft.localTest.ChannelTest;
import com.chsoft.localTest.E2e_Config;


//import com.chsoft.fabric.FabricClient;
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"classpath*:beans.xml"})
public class AopTest {  
	
	private static final String channelName = "mychannel";
	
	@Resource 
	private AopFabricClient aopFabricClient;
	 
	@Resource 
	private ChannelTest channelTest;
	
	@Resource 
	private E2e_Config config;
	
	public FabricUser org1Admin;
	public FabricOrderer orderer;
	
	public FabricPeer peer;
	public FabricChaincode fabricChaincode;
	
	@Before
	public void config() throws Exception{
		config.init();
		aopFabricClient.init(config.getAdminList().get(0));
		org1Admin = config.getAdminList().get(0);
		orderer = config.getOrdererList().get(0);
		peer = config.getPeerList().get(0).get(1);
		fabricChaincode = config.getFabricChaincode("mycc");
	}
	
	@Test  
    public void aopTest() throws Exception{
		Set<String> queryChannels = aopFabricClient.queryChannels(peer);
	}
	
	@Test
	public void createChannelTest() throws Exception{
		String channelTxPath = "E:\\chsoft\\Git\\fabric-samples\\first-network\\channel-artifacts\\channel.tx";	
		aopFabricClient.createChannel(channelName, orderer, channelTxPath);
	}
	
	@Test
	public void channelJoin() throws Exception{
		aopFabricClient.channelJoinPeer(channelName,orderer,peer);
	}
	
	@Test
	public void queryChannelsTest() throws Exception{
		Set<String> list = aopFabricClient.queryChannels(peer);
		System.out.println("aa");
	}
	
	@Test
	public void testPeerInstallChaincodePath() throws Exception{
		aopFabricClient.peerInstallChaincode(fabricChaincode, peer);
	}
	
	@Test
	public void testQueryInstalledChaincodes() throws Exception{
		List<ChaincodeInfo> list = aopFabricClient.queryInstalledChaincodes(peer);
		System.out.println("aa");
	}
	
	@Test
	public void instantiateChainCodeTest() throws Exception{
	    ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
	    chaincodeEndorsementPolicy.fromYamlFile(new File(config.channlePath+"chaincodeendorsementpolicy.yaml"));
	    aopFabricClient.peerInstantiateChainCode(channelName,orderer,peer,fabricChaincode, chaincodeEndorsementPolicy);
	}
	
	@Test
	public void queryInstantiateChainCodeTest() throws Exception{
		List<ChaincodeInfo> list = aopFabricClient.queryInstantiateChaincodes(channelName, peer);
		System.out.println("aa");
	}
	
	@Test
	public void queryChainCodeTest() throws Exception{
		String[] queryArg = new String[] {  "a" };  
		aopFabricClient.queryChaincode(channelName, peer,fabricChaincode, "query",queryArg);
	}
	
	
	
}  