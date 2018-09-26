package com.chsoft.aop;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hyperledger.fabric.protos.peer.Query.ChaincodeInfo;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Channel.NOfEvents;
import org.hyperledger.fabric.sdk.Channel.TransactionOptions;
import org.hyperledger.fabric.sdk.ChannelConfiguration;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.InstallProposalRequest;
import org.hyperledger.fabric.sdk.InstantiateProposalRequest;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.Peer.PeerRole;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.springframework.stereotype.Component;

import com.chsoft.fabric.FabricChaincode;
import com.chsoft.fabric.FabricOrderer;
import com.chsoft.fabric.FabricPeer;
import com.chsoft.fabric.FabricUser;
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
@Component
public class AopFabricClient {
	
	private HFClient client;
	
	private Channel channel;
	
	private Orderer orderer;
	
	private Peer peer;
	
	private HFClient getHFClient(){
		return client;
	}
	/**
	* @Title:用户的客户端，用于操作fabric网络
	* @Description: TODO  
	* @param @param fabricUser 入参：指定用户
	 */
	public AopFabricClient(FabricUser fabricUser){
		try {
			client = FabricClientFactory.getPeerUserClient(fabricUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public AopFabricClient(){
		
	}
	
	public void init(FabricUser fabricUser){
		try {
			client = FabricClientFactory.getPeerUserClient(fabricUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	* @Title: createChannel 
	* @Description: TODO(创建channel) 
	* @param @param channelName 通道的名称
	* @param @param fabricOrderer 创建通道的orderer信息
	* @param @param channelTxPath 储存通道的交易配置信息文件的地址
	* @param @return
	* @param @throws Exception    入参
	* @return Channel    返回类型 返回创建好的channel
	* @author lixing 
	* @throws
	* @date 2018年9月20日 上午10:33:00 
	* @version V1.0
	 */
	@FabricCreate(orderer = 2)
	public Channel createChannel(String channelName,FabricOrderer fabricOrderer,String channelTxPath) throws Exception{
	    
		ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(channelTxPath));
	    
	    Channel channel = client.newChannel(channelName, orderer, channelConfiguration, client.getChannelConfigurationSignature(channelConfiguration,client.getUserContext()));
		
	    return channel;
	}
	
	/**
	* @Title: queryChannels 
	* @Description: TODO(查询peer节点上加入的channel信息) 
	* @param @param fabricUser
	* @param @param fabricPeer
	* @param @return
	* @param @throws InvalidArgumentException    入参
	* @return Set<String>    返回类型
	* @author lixing 
	* @throws
	* @date 2018年9月20日 上午10:39:13 
	* @version V1.0
	 */
	@FabricCreate(peer = 1)
	public Set<String> queryChannels(FabricPeer fabricPeer) throws Exception{
		return client.queryChannels(peer);
	}
	
	/**
	* @Title: channelJoinPeer 
	* @Description: TODO(peer加入一个指定的channel) 
	* @param @param channel
	* @param @param fabricPeer
	* @param @return
	* @param @throws Exception    入参
	* @return Channel    返回类型
	* @author lixing 
	* @throws
	* @date 2018年9月20日 上午10:40:05 
	* @version V1.0
	 */
	@FabricCreate(channel = 1,orderer = 2)
	public Channel channelJoinPeer (String channelName,FabricOrderer fabricOrderer,FabricPeer fabricPeer) throws Exception {
		setField(channel, "initialized", false);
		channel.joinPeer(client.newPeer(fabricPeer.getPeerName(), fabricPeer.getPeerLocation()));
		return channel;
	}
	
	
	private Channel getRunningChannel(String channelName,FabricOrderer fabricOrderer,FabricPeer fabricPeer) throws Exception {
		
        Channel channel = client.newChannel(channelName);
        
        if(fabricOrderer!=null){
        	channel.addOrderer(client.newOrderer(fabricOrderer.getOrdererName(), fabricOrderer.getOrdererLocation()));
        }
        
        if(fabricPeer!=null){
        	channel.addPeer(client.newPeer(fabricPeer.getPeerName(), fabricPeer.getPeerLocation()));
        }

        setField(channel, "initialized", true);
        
        return channel;
    }
	
	private Channel buildChannel(Channel channel, Orderer Orderer, Peer peer ) throws Exception{

		setField(channel, "initialized", false);
		
		channel.addPeer(peer);
		
		channel.addOrderer(orderer);
		
		setField(channel, "initialized", true);
		
		return channel;
		
	}
	
	/*private Channel getRunningChannel(String channelName) throws Exception {
		
        Channel channel = client.newChannel(channelName);
        
        setField(channel, "initialized", true);
        
        return channel;
    }*/
	
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
	
	@FabricCreate(peer = 2)
	public String peerInstallChaincode(FabricChaincode fabricChaincode,FabricPeer fabricPeer) throws Exception   {
	    InstallProposalRequest installProposalRequest = client.newInstallProposalRequest();
	    installProposalRequest.setChaincodeID(fabricChaincode.getChaincodeID());
	    if (fabricChaincode.getChaincodeLanguage().equals(Type.GO_LANG)) {
            installProposalRequest.setChaincodeInputStream(Util.generateTarGzInputStream(
                    (Paths.get(fabricChaincode.getChaincodeFilePath()).toFile()),
                    Paths.get("src", fabricChaincode.getChaincodePath()).toString()));
        }
	    installProposalRequest.setChaincodeVersion(fabricChaincode.getChaincodeVersion());
        installProposalRequest.setChaincodeLanguage(fabricChaincode.getChaincodeLanguage());
	    Collection<Peer> peerList = new ArrayList<Peer>();
	    peerList.add(peer);
	    Collection<ProposalResponse> list = client.sendInstallProposal(installProposalRequest,peerList);
	    ProposalResponse proposalResponse = null;
	    Iterator iterator = list.iterator();
		while(iterator.hasNext()) {
			proposalResponse = (ProposalResponse) iterator.next();
		}
		return proposalResponse.getMessage();
	}
	
	@FabricCreate(peer = 1)
	public List<ChaincodeInfo> queryInstalledChaincodes(FabricPeer fabricPeer) throws Exception{
		return client.queryInstalledChaincodes(peer);
	}
	
	@FabricCreate(channel = 1,peer = 2)
	public List<ChaincodeInfo> queryInstantiateChaincodes(String channelName,FabricPeer fabricPeer) throws Exception{
		return channel.queryInstantiatedChaincodes(peer);
	}
	
	
	@FabricCreate(channel = 1,orderer = 2,peer = 3)
	public String peerInstantiateChainCode(String channelName,FabricOrderer fabricOrderer,FabricPeer fabricPeer,FabricChaincode fabricChaincode,ChaincodeEndorsementPolicy chaincodeEndorsementPolicy) throws Exception{
		InstantiateProposalRequest instantiateProposalRequest = client.newInstantiationProposalRequest();
        instantiateProposalRequest.setChaincodeID(fabricChaincode.getChaincodeID());
		instantiateProposalRequest.setChaincodePath(fabricChaincode.getChaincodePath());
		instantiateProposalRequest.setChaincodeVersion(fabricChaincode.getChaincodeVersion());
        instantiateProposalRequest.setProposalWaitTime(fabricChaincode.getDeployWatiTime());
        instantiateProposalRequest.setChaincodeName(fabricChaincode.getChaincodeName());
        instantiateProposalRequest.setChaincodeLanguage(fabricChaincode.getChaincodeLanguage());
        instantiateProposalRequest.setFcn("init");
        instantiateProposalRequest.setArgs(new String[] {"a", "100", "b", "200"});
        Map<String, byte[]> tm = new HashMap<>();
        tm.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
        tm.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
        instantiateProposalRequest.setTransientMap(tm);
        instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
        Collection<ProposalResponse> list = channel.sendInstantiationProposal(instantiateProposalRequest);
	    ProposalResponse proposalResponse = null;
	    Iterator iterator = list.iterator();
		while(iterator.hasNext()) {
			proposalResponse = (ProposalResponse) iterator.next();
		}
		 NOfEvents nOfEvents = NOfEvents.createNofEvents();
         if (!channel.getPeers(EnumSet.of(PeerRole.EVENT_SOURCE)).isEmpty()) {
             nOfEvents.addPeers(channel.getPeers(EnumSet.of(PeerRole.EVENT_SOURCE)));
         }
         if (!channel.getEventHubs().isEmpty()) {
             nOfEvents.addEventHubs(channel.getEventHubs());
         }

         channel.sendTransaction(list, TransactionOptions.createTransactionOptions() //Basically the default options but shows it's usage.
                 .userContext(client.getUserContext()) //could be a different user context. this is the default.
                 .shuffleOrders(false) // don't shuffle any orderers the default is true.
                 .orderers(channel.getOrderers()) // specify the orderers we want to try this transaction. Fails once all Orderers are tried.
                 .nOfEvents(nOfEvents) // The events to signal the completion of the interest in the transaction
         );
		return proposalResponse.getMessage();
	}
	
	@FabricCreate(channel = 1,peer = 2)
	public void queryChaincode(String channelName,FabricPeer fabricPeer,FabricChaincode fabricChaincode,String fcn, String[] args) throws Exception{
		Map<String, String> resultMap = new HashMap<>();
        QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(args);
        queryByChaincodeRequest.setFcn(fcn);
        queryByChaincodeRequest.setChaincodeID(fabricChaincode.getChaincodeID());
        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
        queryByChaincodeRequest.setTransientMap(tm2);
        Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest);
        System.out.println("aa");
	}
	
	
	public void channelAddpeer(Channel channelNow,Peer peerNow) throws Exception{
		setField(channelNow, "initialized", false);
		channelNow.addPeer(peerNow);
		setField(channelNow, "initialized", true);
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
