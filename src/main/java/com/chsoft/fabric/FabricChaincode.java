package com.chsoft.fabric;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;

/**
 * 
* @ClassName: FabricChaincode 
* @Description: TODO(智能合约类) 
* @author lixing  
* @date 2018年9月13日 下午3:03:56 
* @version V1.0
 */
public class FabricChaincode {

    /** 当前将要访问的智能合约所属频道名称 */
    private String channelName; // mychannel
    /** 智能合约名称 */
    private String chaincodeName; // mycc
    /** 智能合约代码的路径 */
    private String chaincodeFilePath; // E:\\chsoft\\Git\\fabric-samples\\chaincode\\chaincode_example02\\go\\
    /** 智能合约在节点上的安装路径,可以随意赋值，标识作用 */
    private String chaincodePath; // github.com/hyperledger/fabric/chaincode/go/
    /** 智能合约版本号 */
    private String chaincodeVersion; // 1.0
    /** 执行智能合约操作等待时间 */
    private int invokeWatiTime = 100000;
    /** 执行智能合约实例等待时间 */
    private int deployWatiTime = 120000;
    
    private ChaincodeID chaincodeID;
    
    // Chaincode language
    private Type chaincodeLanguage = Type.GO_LANG;
    
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChaincodeName() {
        return chaincodeName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getChaincodePath() {
        return chaincodePath;
    }

    public void setChaincodePath(String chaincodePath) {
        this.chaincodePath = chaincodePath;
    }

    public String getChaincodeVersion() {
        return chaincodeVersion;
    }

    public void setChaincodeVersion(String chaincodeVersion) {
        this.chaincodeVersion = chaincodeVersion;
    }

    public int getInvokeWatiTime() {
        return invokeWatiTime;
    }

    public void setInvokeWatiTime(int invokeWatiTime) {
        this.invokeWatiTime = invokeWatiTime;
    }

    public int getDeployWatiTime() {
        return deployWatiTime;
    }

    public void setDeployWatiTime(int deployWatiTime) {
        this.deployWatiTime = deployWatiTime;
    }

	public String getChaincodeFilePath() {
		return chaincodeFilePath;
	}

	public void setChaincodeFilePath(String chaincodeFilePath) {
		this.chaincodeFilePath = chaincodeFilePath;
	}

	public Type getChaincodeLanguage() {
		return chaincodeLanguage;
	}

	public void setChaincodeLanguage(Type chaincodeLanguage) {
		this.chaincodeLanguage = chaincodeLanguage;
	}

	public ChaincodeID getChaincodeID() {
		return chaincodeID;
	}

	public void setChaincodeID(ChaincodeID chaincodeID) {
		this.chaincodeID = chaincodeID;
	}

	
    
}