package org.hyperledger.my_fabric_ca;
import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
public class CaUser implements User{
	
	//用户名
	public String name;
	
	//用户密码
	public String password;
	
	//用户类型
	public String type;
	
	//用户所属组织
	public String affiliation;
	
	public CaUser() {
		super();
	}

	public CaUser(String name, String password, String affiliation) {
		super();
		this.name = name;
		this.password = password;
		this.affiliation = affiliation;
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	public Enrollment enrollment;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	@Override
	public Set<String> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enrollment getEnrollment() {
		// TODO Auto-generated method stub
		return enrollment;
	}

	@Override
	public String getMspId() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
