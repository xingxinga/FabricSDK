package com.chsoft.newFabric;

import java.io.Serializable;
import java.security.PrivateKey;

import org.hyperledger.fabric.sdk.Enrollment;

/**
 * 
* @ClassName: UserEnrollement 
* @Description: TODO(用户凭证) 
* @author lixing  
* @date 2018年9月13日 下午2:46:40 
* @version V1.0
 */
public class UserEnrollement implements Enrollment, Serializable {
	private static final long serialVersionUID = 6965341351799577442L;

    /** 私钥 */
    private final PrivateKey privateKey;
    /** 授权证书 */
    private final String certificate;

    public UserEnrollement(PrivateKey privateKey, String certificate) {
        this.certificate = certificate;
        this.privateKey = privateKey;
    }

    @Override
    public PrivateKey getKey() {
        return privateKey;
    }

    @Override
    public String getCert() {
        return certificate;
    }

}
