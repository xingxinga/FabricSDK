package org.hyperledger.my_fabric_ca;

public class MSP {
	//
	public String mspDirectory;

	public String cacerts;
	
	public String intermediatecerts;
	
	public String keystore;
	
	public String signcerts;
	
	public String tlscacerts;

	public MSP(String mspDirectory) {
		super();
		this.mspDirectory = mspDirectory;
	}

	public MSP(String mspDirectory, String cacerts, String intermediatecerts, String keystore, String signcerts,
			String tlscacerts) {
		super();
		this.mspDirectory = mspDirectory;
		this.cacerts = cacerts;
		this.intermediatecerts = intermediatecerts;
		this.keystore = keystore;
		this.signcerts = signcerts;
		this.tlscacerts = tlscacerts;
	}

	public String getMspDirectory() {
		return mspDirectory;
	}

	public void setMspDirectory(String mspDirectory) {
		this.mspDirectory = mspDirectory;
	}

	public String getCacerts() {
		return cacerts;
	}

	public void setCacerts(String cacerts) {
		this.cacerts = cacerts;
	}

	public String getIntermediatecerts() {
		return intermediatecerts;
	}

	public void setIntermediatecerts(String intermediatecerts) {
		this.intermediatecerts = intermediatecerts;
	}

	public String getKeystore() {
		return keystore;
	}

	public void setKeystore(String keystore) {
		this.keystore = keystore;
	}

	public String getSigncerts() {
		return signcerts;
	}

	public void setSigncerts(String signcerts) {
		this.signcerts = signcerts;
	}

	public String getTlscacerts() {
		return tlscacerts;
	}

	public void setTlscacerts(String tlscacerts) {
		this.tlscacerts = tlscacerts;
	}
	
}
