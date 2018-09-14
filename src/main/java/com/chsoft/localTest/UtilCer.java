package com.chsoft.localTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import com.chsoft.newFabric.FabricUser;
import com.chsoft.newFabric.UserEnrollement;

/**
 * 
* @ClassName: UtilCer 
* @Description: TODO(本地测试证书解析类) 
* @author lixing  
* @date 2018年9月13日 下午2:35:35 
* @version V1.0
 */
public class UtilCer {
	
	/**
	 * 
	* @Title: getMember 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param 用户名 name
	* @param @param org
	* @param @param mspId
	* @param @param privateKeyFile
	* @param @param certificateFile
	* @param @return
	* @param @throws IOException
	* @param @throws NoSuchAlgorithmException
	* @param @throws NoSuchProviderException
	* @param @throws InvalidKeySpecException    入参
	* @return FabricUser    返回类型
	* @author lixing 
	* @throws
	* @date 2018年9月13日 下午2:36:58 
	* @version V1.0
	 */
	public FabricUser getMember(String name, String org, String mspId, File privateKeyFile, File certificateFile){
			
            // 创建User，并尝试从键值存储中恢复它的状�??(如果找到的话)�?
			FabricUser fabricUser = new FabricUser();
			fabricUser.setName(name);
			fabricUser.setAffiliation(org);
            fabricUser.setMspId(mspId);
            String certificate = null;
            PrivateKey privateKey = null;
			try {
				certificate = new String(IOUtils.toByteArray(new FileInputStream(certificateFile)), "UTF-8");
				privateKey = getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream(privateKeyFile)));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            fabricUser.setEnrollment(new UserEnrollement(privateKey, certificate));
            return fabricUser;
    }
	
	/**
	 * 
	* @Title: getPrivateKeyFromBytes 
	* @Description: TODO(根据传入的二进制数据生成私钥对象) 
	* @param @param data
	* @return PrivateKey    返回类型
	* @author lixing 
	* @throws
	* @date 2018年9月13日 下午3:23:42 
	* @version V1.0
	 */
	public PrivateKey getPrivateKeyFromBytes(byte[] data) throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        final Reader pemReader = new StringReader(new String(data));
        final PrivateKeyInfo pemPair;
        try (PEMParser pemParser = new PEMParser(pemReader)) {
            pemPair = (PrivateKeyInfo) pemParser.readObject();
        }
        PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);
        return privateKey;
    }
	
	static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
     * 从指定路径中获取后缀为 _sk 的文件，且该路径下有且仅有该文件
     * 
     * @param directorys
     *            指定路径
     * @return File
     */
	public File findFileSk(File directory) {
        File[] matches = directory.listFiles((dir, name) -> name.endsWith("_sk"));
        if (null == matches) {
            throw new RuntimeException(String.format("Matches returned null does %s directory exist?", directory.getAbsoluteFile().getName()));
        }
        if (matches.length != 1) {
            throw new RuntimeException(String.format("Expected in %s only 1 sk file but found %d", directory.getAbsoluteFile().getName(), matches.length));
        }
        return matches[0];
    }
}
