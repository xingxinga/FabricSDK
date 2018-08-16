package org.hyperledger.my_fabric_ca;

import java.util.ArrayList;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoPrimitives;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAAffiliation;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.HFCAInfo;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

public class UserEnroll {
	
    private static final String TEST_ADMIN_NAME = "admin";
    private static final String TEST_ADMIN_PW = "adminpw";
    private static final String ip = "192.168.15.142:7054"; 
    private static final String CA_NAME = "client"; 
    private static final String MSP_PATH = "D:\\msp\\";
    private static CryptoPrimitives crypto;
    
    private HFCAClient client;
    
    public UserEnroll() throws Exception{
		super();
		//通过CA服务器地址和端口、CA服务器名称，获得CA客户端实体类——client
		client = HFCAClient.createNewInstance(CA_NAME, "http://"+ip, null);
        //调用client的登录方法，参数为登录用户的用户名和用户密码
		CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        client.setCryptoSuite(cryptoSuite);
	}
	public static void main(String[] args) throws Exception{
		UserEnroll u = new UserEnroll();
		HFCAInfo HFCAInfo = u.client.info();
		System.out.println(HFCAInfo.toString());
    	/*//初始化执行注册用户
    	
		CaUser serverUser = new CaUser();
		serverUser.setName("User8@org1.example.com");
		Enrollment enrollment = u.Enroll(serverUser.getName(),"password");
		serverUser.setEnrollment(enrollment);
		//初始化注册用户
		CaUser registerUser = new CaUser("User8@org1.example.com","password","com.example.org1");
		u.register(serverUser,registerUser);
		u.generateUserMSP(serverUser);*/
	}
    /**
     * 用户登录方法
     * @return：返回用户登录CA服务器的凭证（应该具有时效性）
     * @throws Exception
     */
	public Enrollment Enroll(String userName,String password) throws Exception {
		//CA客户端实体类设置默认的加密工具——cryptoSuite
        return client.enroll(userName, password);
	}
	
	/**
	 * 注册新用户
	 * 
	 * @param user
	 * @throws Exception
	 */
	
	public void register(CaUser serverUser,CaUser registerUser) throws Exception {
		//注册新用户的信息：用户名称与用户所在组织名
		//用户名不能有重复，组织名必须是执行注册的人拥有操作权限的组织列表中存在的
		RegistrationRequest regreq = new RegistrationRequest(registerUser.getName(), registerUser.getAffiliation());
		regreq.setSecret(registerUser.getPassword());
        client.register(regreq, serverUser);
	}
	
	
	public void testConnectionCA() throws Exception{
		HFCAClient hfCAClient = HFCAClient.createNewInstance("client", "http://192.168.15.142:7054", null);
		HFCAInfo hfCAInfo = hfCAClient.info();
		System.out.println(hfCAInfo);
	}
	
	public void generateUserMSP(User user) throws Exception{
		String path = MSP_PATH;
		try{
			HFCAAffiliation affiliation= client.getHFCAAffiliations(user);
			while(affiliation.getChildren()!=null&&affiliation.getChildren().size()==1){
				ArrayList<HFCAAffiliation> list = (ArrayList) affiliation.getChildren();
				affiliation = list.get(0);
			}
			path = path + affiliation.getName() + Config.getSeparator();
		} catch(Exception e){
			
		}
		finally{
			path = path + user.getName() + Config.getSeparator() + "msp" + Config.getSeparator();
			Base64MSPBuild build = new Base64MSPBuild(path);
			build.BuildKeystore(user.getEnrollment().getKey());
			build.BuildAdmincerts(user.getEnrollment().getCert(), user.getName());
		}
		
	}
	
}
