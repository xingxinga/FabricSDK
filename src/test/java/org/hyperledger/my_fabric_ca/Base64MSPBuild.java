
package org.hyperledger.my_fabric_ca;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivateKey;

public class Base64MSPBuild implements MSPBuild {

	private FabricCaMSP msp;
	private ExportCert exportCert = new ExportCert();
	public Base64MSPBuild(String mspDirectory) {
		super();
		this.msp = new FabricCaMSP(mspDirectory);
	}
	
	@Override
	public void BuildCacerts() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void BuildIntermediatecerts() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void BuildKeystore(PrivateKey privateKey) {
		// TODO Auto-generated method stub
		try {
			exportCert.exportPrivateKey(privateKey, msp.keystore+msp.keystoreName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void BuildSigncerts() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void BuildTlscacerts() {
		// TODO Auto-generated method stub
		
	}

	public void BuildAdmincerts(String certs,String userName) {
		// TODO Auto-generated method stub
		try {
			String path = msp.mspDirectory+Config.getSeparator()+"admincerts"+Config.getSeparator();
			File file1=new File(path);
			file1.mkdirs();
			exportCert.exportCertByString(certs,path+userName+"-cer.pem");
			copyFile(path+userName+"-cer.pem",msp.signcerts+userName+"-cer.pem");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void copyFile(String from,String to) throws IOException{
		File fromFile = new File(from);
		File toFile = new File(to);
        FileInputStream ins = new FileInputStream(fromFile);
        FileOutputStream out = new FileOutputStream(toFile);
        byte[] b = new byte[1024];
        int n=0;
        while((n=ins.read(b))!=-1){
            out.write(b, 0, n);
        }
        
        ins.close();
        out.close();
    }
	
}
