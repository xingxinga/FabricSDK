package org.hyperledger.my_fabric_ca;

import java.security.PrivateKey;

/**
 * 生成MSP文件夹
 * @author lixing
 *
 */
public interface MSPBuild {
	
	//生成根证书
	public void BuildCacerts();
	
	//生成中间证书
	public void BuildIntermediatecerts();
	
	//生成私钥
	public void BuildKeystore(PrivateKey privateKey);
	
	//生成被组织根证书签名，验证本节点签名的证书
	public void BuildSigncerts();
	
	//生成TLS证书
	public void BuildTlscacerts();
	
}
