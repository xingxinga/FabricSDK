package com.chsoft.fabric;

import java.io.Serializable;
import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
/**
 * 
* @ClassName: FabricUser 
* @Description: TODO 操作fabric网络时的用户
* @author lixing  
* @date 2018年9月13日 上午11:36:10 
* @version V1.0
 */
public class FabricUser implements User, Serializable {
	
    private static final long serialVersionUID = 5695080465408336815L;
	
    /** 名称 */
    private String name;
    /** 规则 */
    private Set<String> roles;
    /** 账户 */
    private String account;
    /** 从属联盟 */
    private String affiliation;
    /** 会员id */
    private String mspId;
    /** 注册登记操作 */
    Enrollment enrollment = null; // �?要在测试env中访�?
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public Set<String> getRoles() {
		// TODO Auto-generated method stub
		return roles;
	}

	@Override
	public String getAccount() {
		// TODO Auto-generated method stub
		return account;
	}

	@Override
	public String getAffiliation() {
		// TODO Auto-generated method stub
		return affiliation;
	}

	@Override
	public Enrollment getEnrollment() {
		// TODO Auto-generated method stub
		return enrollment;
	}

	@Override
	public String getMspId() {
		// TODO Auto-generated method stub
		return mspId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public void setMspId(String mspId) {
		this.mspId = mspId;
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	
}
