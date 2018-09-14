package com.chsoft.newFabric; 
/**
 * 
* @ClassName: FabricOrderer 
* @Description: TODO(Fabric网络中的orderer节点对象) 
* @author lixing  
* @date 2018年9月13日 下午2:16:56 
* @version V1.0
 */
public class FabricOrderer {
	
    /** orderer 排序服务器所在根域名 */
    private String ordererDomainName; // anti-moth.com
	
    /** orderer 排序服务器的域名 */
    private String ordererName;
    
    /** orderer 排序服务器的访问地址 */
    private String ordererLocation;

    
	public FabricOrderer() {
		super();
	}

	public FabricOrderer(String ordererDomainName, String ordererName, String ordererLocation) {
		super();
		this.ordererDomainName = ordererDomainName;
		this.ordererName = ordererName;
		this.ordererLocation = ordererLocation;
	}

	public String getOrdererDomainName() {
		return ordererDomainName;
	}

	public void setOrdererDomainName(String ordererDomainName) {
		this.ordererDomainName = ordererDomainName;
	}

	public String getOrdererName() {
		return ordererName;
	}

	public void setOrdererName(String ordererName) {
		this.ordererName = ordererName;
	}

	public String getOrdererLocation() {
		return ordererLocation;
	}

	public void setOrdererLocation(String ordererLocation) {
		this.ordererLocation = ordererLocation;
	}
    
    
}
