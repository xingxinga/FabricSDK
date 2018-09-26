package com.chsoft.fabric;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hyperledger.fabric.protos.peer.Query.ChaincodeInfo;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.ChannelConfiguration;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.InstallProposalRequest;
import org.hyperledger.fabric.sdk.InstantiateProposalRequest;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import com.chsoft.localTest.FabricClientFactory;
import com.chsoft.localTest.Util;

/**
 * 
* @ClassName: FabricClient 
* @Description: TODO(自己封装的fabric客户端，操作时使用自己封装的对象) 
* @author lixing  
* @date 2018年9月13日 下午5:35:57 
* @version V1.0
 */
public class FabricClient {
	
	private HFClient client;
	
	public FabricClient(FabricUser fabricUser){
		try {
			client = FabricClientFactory.getPeerUserClient(fabricUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Channel createChannel(String channelName,FabricOrderer fabricOrderer,ChannelConfiguration channelConfiguration) throws Exception{
	    
	    Orderer orderer = client.newOrderer(fabricOrderer.getOrdererName(), fabricOrderer.getOrdererLocation());
	    
	    Channel channel = client.newChannel(channelName, orderer, channelConfiguration, client.getChannelConfigurationSignature(channelConfiguration,client.getUserContext()));
		
	    return channel;
	}
	
	public Set<String> queryChannels(FabricUser fabricUser,FabricPeer fabricPeer) throws InvalidArgumentException{
		Peer peer;
		Set<String> result = null;
		try {
			peer = client.newPeer(fabricPeer.getPeerName(), fabricPeer.getPeerLocation());
			result = client.queryChannels(peer);
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProposalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public Channel channelJoinPeer (Channel channel,FabricPeer fabricPeer) throws Exception {
		
        Peer peer = client.newPeer(fabricPeer.getPeerName(), fabricPeer.getPeerLocation());
        
        Orderer orderer = null;
        
        Collection ordererList = channel.getOrderers();
        
		Iterator iterator = ordererList.iterator();
		while(iterator.hasNext()) {
			orderer = (Orderer) iterator.next();
		}
		setField(channel, "initialized", false);
		channel.addOrderer(orderer);
		channel.joinPeer(peer);
		return channel;
	}
	
	public Channel getRunningChannel(String channelName,FabricOrderer fabricOrderer) throws Exception {
		
        Channel channel = client.newChannel(channelName);
        
        if(fabricOrderer!=null){
        	Orderer orderer = client.newOrderer(fabricOrderer.getOrdererName(), fabricOrderer.getOrdererLocation());
            
            channel.addOrderer(orderer);
        }
        
        setField(channel, "initialized", true);
        
        return channel;
    }
	
	public boolean peerListInstallChaincode(FabricChaincode fabricChaincode,List<FabricPeer> fabricPeerList) throws Exception {

	
        ChaincodeID.Builder chaincodeIDBuilder = ChaincodeID.newBuilder().setName(fabricChaincode.getChaincodeName())
                .setVersion(fabricChaincode.getChaincodeVersion());
        chaincodeIDBuilder.setPath(fabricChaincode.getChaincodePath());
        ChaincodeID chaincodeID = chaincodeIDBuilder.build();
	    InstallProposalRequest installProposalRequest = client.newInstallProposalRequest();
	    installProposalRequest.setChaincodeID(chaincodeID);
	    
	    //client.sendInstallProposal(installProposalRequest, peers)
	    return true;
	}
	
	public void peerInstallChaincode(FabricChaincode fabricChaincode,FabricPeer fabricPeer) throws Exception   {
		ChaincodeID.Builder chaincodeIDBuilder = ChaincodeID.newBuilder().setName(fabricChaincode.getChaincodeName())
                .setVersion(fabricChaincode.getChaincodeVersion());
        chaincodeIDBuilder.setPath(fabricChaincode.getChaincodePath());
        ChaincodeID chaincodeID = chaincodeIDBuilder.build();
	    InstallProposalRequest installProposalRequest = client.newInstallProposalRequest();
	    installProposalRequest.setChaincodeID(chaincodeID);
	    
	    if (fabricChaincode.getChaincodeLanguage().equals(Type.GO_LANG)) {
            installProposalRequest.setChaincodeInputStream(Util.generateTarGzInputStream(
                    (Paths.get(fabricChaincode.getChaincodePath(), fabricChaincode.getChaincodeFilePath()).toFile()),
                    Paths.get("src", fabricChaincode.getChaincodeFilePath()).toString()));
        } 
	    //installProposalRequest.setChaincodeSourceLocation(Paths.get(fabricChaincode.getChaincodePath()).toFile());
	    installProposalRequest.setChaincodeVersion(fabricChaincode.getChaincodeVersion());
        installProposalRequest.setChaincodeLanguage(fabricChaincode.getChaincodeLanguage());
	    Peer peer = client.newPeer(fabricPeer.getPeerName(), fabricPeer.getPeerLocation());
	    Collection<Peer> peerList = new ArrayList<Peer>();
	    peerList.add(peer);
	    Collection<ProposalResponse> list = client.sendInstallProposal(installProposalRequest,peerList);
	}
	
	public List<ChaincodeInfo> queryInstalledChaincodes(FabricPeer fabricPeer) throws Exception{
		Peer peer = client.newPeer(fabricPeer.getPeerName(), fabricPeer.getPeerLocation());
		List<ChaincodeInfo> list = client.queryInstalledChaincodes(peer);
		return list;
	}
	
	public void peerInstantiateChainCode(Channel channel,FabricPeer fabricPeer,FabricChaincode fabricChaincode,ChaincodeEndorsementPolicy chaincodeEndorsementPolicy) throws Exception{
		ChaincodeID.Builder chaincodeIDBuilder = ChaincodeID.newBuilder().setName(fabricChaincode.getChaincodeName())
                .setVersion(fabricChaincode.getChaincodeVersion());
        chaincodeIDBuilder.setPath(fabricChaincode.getChaincodePath());
        ChaincodeID chaincodeID = chaincodeIDBuilder.build();
        
		InstantiateProposalRequest instantiateProposalRequest = client.newInstantiationProposalRequest();
        instantiateProposalRequest.setProposalWaitTime(fabricChaincode.getDeployWatiTime());
        instantiateProposalRequest.setChaincodeID(chaincodeID);
        instantiateProposalRequest.setChaincodeLanguage(fabricChaincode.getChaincodeLanguage());
        instantiateProposalRequest.setFcn("init");
        instantiateProposalRequest.setArgs(new String[] {"a", "100", "b", "200"});
        Map<String, byte[]> tm = new HashMap<>();
        tm.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
        tm.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
        instantiateProposalRequest.setTransientMap(tm);
        instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
        Peer peer = client.newPeer(fabricPeer.getPeerName(), fabricPeer.getPeerLocation());
        Collection<Peer> peerList = new ArrayList<Peer>();
	    peerList.add(peer);
	    setField(channel, "initialized", false);
	    channel.addPeer(peer);
	    setField(channel, "initialized", true);
        channel.sendInstantiationProposal(instantiateProposalRequest, peerList);
        
	}
	
	public void queryChaincode(Channel channel,FabricPeer fabricPeer,FabricChaincode fabricChaincode,String fcn, String[] args) throws Exception{
		Map<String, String> resultMap = new HashMap<>();
        QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(args);
        queryByChaincodeRequest.setFcn(fcn);
        queryByChaincodeRequest.setChaincodeID(fabricChaincode.getChaincodeID());
        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
        queryByChaincodeRequest.setTransientMap(tm2);
        Collection<Peer> peers = new ArrayList<Peer>();
        peers.add(client.newPeer(fabricPeer.getPeerName(), fabricPeer.getPeerLocation()));
        Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest,peers);
        System.out.println("aa");
	}
	
	public List<ChaincodeInfo> queryInstantiateChaincodes(Channel channel,FabricPeer fabricPeer) throws Exception{
		setField(channel, "initialized", false);
		Peer peer = client.newPeer(fabricPeer.getPeerName(), fabricPeer.getPeerLocation());
		channel.addPeer(peer);
		setField(channel, "initialized",true);
		return channel.queryInstantiatedChaincodes(peer);
	}
	
	
	/**
     * Sets the value of a field on an object
     *
     * @param o         The object that contains the field
     * @param fieldName The name of the field
     * @param value     The new value
     * @return The previous value of the field
     */
    public static Object setField(Object o, String fieldName, Object value) {
        Object oldVal = null;
        try {
            final Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            oldVal = field.get(o);
            field.set(o, value);
        } catch (Exception e) {
            throw new RuntimeException("Cannot get value of field " + fieldName, e);
        }
        return oldVal;
    }
}
