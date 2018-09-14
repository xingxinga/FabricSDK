package com.chsoft.localTest;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.chsoft.newFabric.FabricOrderer;
import com.chsoft.newFabric.FabricPeer;
import com.chsoft.newFabric.FabricUser;
import com.chsoft.newFabric.UserEnrollement;

public class E2e_Config {
	
	public UtilCer utilCer;
	
	public static final String ip = "192.168.15.151";
	
	public static final String channlePath = "E:\\chsoft\\Git\\fabric-samples\\first-network\\";
	
	public static final String cryptoConfigPath = "E:\\chsoft\\Git\\fabric-samples\\first-network" + "\\crypto-config\\";
	
	public static final String channelArtifactsPath = "E:\\chsoft\\Git\\fabric-samples\\first-network" + "\\channel-artifacts\\";

/*	public static final String userName = "Admin@org1.example.com";
	
	public static final String orgName = "Org1";
	
	public static final String orgMSPID = "Org1MSP";
	
	public static final String orgDomainName = "org1.example.com";*/
	
	private List<FabricUser> adminList;
	
	private List<List<FabricUser>> userList;
	
	private List<List<FabricPeer>> peerList;
	
	private List<FabricOrderer> ordererList;
	
	public static final int userCount = 2;
	
	public static final int orgCount = 2;
	
	public static final int peerCount = 2;
	
	public static final int ordererCount = 1;
	
	public E2e_Config() throws Exception{
		utilCer = new UtilCer();
		adminList = new ArrayList<>();
		userList = new ArrayList<>();
		peerList = new ArrayList<>();
		ordererList = new ArrayList<>();
		configAdminList();
		configOrdererList();
		configPeerList();
//		configUserList();
	}
	
	
	public void configAdminList() throws Exception{
		FabricUser fabricUser;
		String orgDomainName,orgName,mspId,userName;
		for(int i = 1; i < orgCount+1; i++){
			fabricUser = new FabricUser();
			//初始化用户基本信息
			orgDomainName = "org" + i + ".example.com";
			orgName = "Org" + i ;
			mspId = "Org" + i + "MSP";
			userName = "Admin@" + orgDomainName;
			fabricUser.setName(userName);
			fabricUser.setAffiliation(orgName);
			fabricUser.setMspId(mspId);
			//初始化用户凭证
			File skFile = Paths.get(cryptoConfigPath +"/peerOrganizations/",orgDomainName, 
					String.format("/users/%s/msp/keystore", userName)).
				toFile();
			File certificateFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", orgDomainName,
					String.format("/users/%s/msp/signcerts/%s-cert.pem", userName, userName)).toFile();
			String certificate = new String(IOUtils.toByteArray(new FileInputStream(certificateFile)), "UTF-8");
	        PrivateKey privateKey = utilCer.getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream(utilCer.findFileSk(skFile))));
			fabricUser.setEnrollment(new UserEnrollement(privateKey, certificate));
			adminList.add(fabricUser);
		}
	}
	
	public void configUserList() throws Exception{
		FabricUser fabricUser;
		String orgDomainName,orgName,mspId,userName;
		for(int i = 1; i < orgCount+1; i++){
			List<FabricUser> orgList = new ArrayList<>();
			fabricUser = new FabricUser();
			//初始化用户基本信息
			orgDomainName = "org" + i + ".example.com";
			orgName = "Org" + i ;
			mspId = "Org" + i + "MSP";
			for(int k = 1; k < userCount+1; k++){
				userName = userCount == 1? "User@" + orgDomainName:"User" + k + "@" + orgDomainName;
				fabricUser.setName(userName);
				fabricUser.setAffiliation(orgName);
				fabricUser.setMspId(mspId);
				//初始化用户凭证
				File skFile = Paths.get(cryptoConfigPath +"/peerOrganizations/",orgDomainName, 
						String.format("/users/%s/msp/keystore", userName)).
					toFile();
				File certificateFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", orgDomainName,
						String.format("/users/%s/msp/signcerts/%s-cert.pem", userName, userName)).toFile();
				String certificate = new String(IOUtils.toByteArray(new FileInputStream(certificateFile)), "UTF-8");
		        PrivateKey privateKey = utilCer.getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream(skFile)));
				fabricUser.setEnrollment(new UserEnrollement(privateKey, certificate));
				orgList.add(fabricUser);
			}
			userList.add(orgList);
		}
	}
	
	public void configPeerList(){
		FabricPeer fabricPeer;
		for(int i = 1; i < orgCount+1; i++){
			List<FabricPeer> orgList = new ArrayList<>();
			for(int k = 0; k < peerCount; k++){
				fabricPeer = new FabricPeer();
				String peerName = "peer" + k + ".org" + i + ".example.com" ;
				fabricPeer.setPeerName(peerName);
				fabricPeer.setPeerLocation("grpc://"+ip+":7051");
				orgList.add(fabricPeer);
			}
			peerList.add(orgList);
		}
	}
	
	public void configOrdererList(){
		for(int i = 1; i < ordererCount+1; i++){
			FabricOrderer fabricOrderer= new FabricOrderer();
			fabricOrderer.setOrdererDomainName("example.com");
			fabricOrderer.setOrdererLocation("grpc://"+ip+":7050");
			String ordererName = ordererCount==1?"orderer.example.com":"orderer"+ i +".example.com";
			fabricOrderer.setOrdererName(ordererName);
			ordererList.add(fabricOrderer);
		}
	}
	
	
	public List<FabricUser> getAdminList(){
		return adminList;
	}
	
	public List<List<FabricUser>> getUserList(){
		return userList;
	}
	
	public List<List<FabricPeer>> getPeerList(){
		return peerList;
	}
	
	public List<FabricOrderer> getOrdererList(){
		return ordererList;
	}
	
}
