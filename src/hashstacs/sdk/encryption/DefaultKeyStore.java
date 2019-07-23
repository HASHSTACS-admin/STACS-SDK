package hashstacs.sdk.encryption;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import org.spongycastle.jce.spec.ECPrivateKeySpec;

import com.hashstacs.sdk.crypto.ECKey;
import com.hashstacs.sdk.crypto.GspECKey;
import com.hashstacs.sdk.crypto.jce.ECKeyFactory;
import com.hashstacs.sdk.crypto.jce.SpongyCastleProvider;

import hashstacs.sdk.util.StacsUtil;

//TODO - DO NOT COMMIT
public class DefaultKeyStore {

	//to be extracted from config file instead
	private static final String KEYSTORE_FILE_LOCATION = "newKeyStoreFilename.jks";
	
	public DefaultKeyStore() {
		
	}
	
	private void Test(String password) {
		
		//convert password into a char array		
		char[] pwdArray = password.toCharArray();
		
		if(pwdArray == null | pwdArray.length == 0) {
			//log error here as empty password
			return ; 
		}
		
		//initialize keystore
		KeyStore ks = null;
		try {
			ks = KeyStore.getInstance("PKCS12");
			ks.load(null,pwdArray);
		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//save new keystore to file system by creating a new .jks file
		try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE_LOCATION)) {
			ks.store(fos,pwdArray);	
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//store Private Key
		GspECKey newKey = new GspECKey();
		System.out.println("Public Key: " + newKey.getPublicKeyAsHex());
		System.out.println("Private Key: " + StacsUtil.toHex(newKey.getPrivKeyBytes()));
		System.out.println("Address: " + newKey.getHexAddress());
		
		//create Certificate Chain and add relevant certs
		X509Certificate[] certChain = new X509Certificate[2];
		//to add relevant certs, e.g. certChain[0] = new X509Certificate();
		
		//convert BigInteger format of the GspECKey to PrivateKey format 
		try {
			PrivateKey pk = ECKeyFactory.getInstance(SpongyCastleProvider.getInstance()).generatePrivate(new ECPrivateKeySpec(newKey.getPrivKey(), ECKey.CURVE_SPEC));
		} catch (InvalidKeySpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//To retrieve the private key stored in the keystore
		Key retrievedKey = null;
		try {
			retrievedKey = ks.getKey("stacs-key", pwdArray);
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String retrievedPriKey = StacsUtil.toHex(retrievedKey.getEncoded());
		
		
		//verifying if password entered matches what was used to setup keystore
		try {
			char[] wrongPwd = "he".toCharArray();
			if(wrongPwd == null | wrongPwd.length == 0) {
				System.out.println("fail with no pwd");
			}
			else {
				ks.load(new FileInputStream("newKeyStoreFilename.jks"),wrongPwd);
				System.out.println("succeess");
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
}
