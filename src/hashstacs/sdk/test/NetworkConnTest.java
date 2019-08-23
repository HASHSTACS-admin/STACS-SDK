package hashstacs.sdk.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import hashstacs.sdk.chain.ChainConnector;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.wallet.WalletConnector;


public class NetworkConnTest {
	private static String _chainPubKey;
	private static String _merchantPriKey;
	private static String _aesKey;
	private static String _merchantId;
	private static String _gatewayUrl;
	
	private static String CONFIG_PROPERTIES = "config.properties";
	
	@BeforeClass
	public static void getConnDetails() {
		_chainPubKey = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_PUBKEY);
		_merchantPriKey = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_PRIKEY);
		_aesKey = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_AESKEY);
		_merchantId = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_MERCHANTID);
		_gatewayUrl = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_GATEWAY);
	}
	
	@Test
	public void checkConfigPropertiesFile() {
		assertNotNull(_chainPubKey);
		assertNotNull(_merchantPriKey);
		assertNotNull(_aesKey);
		assertNotNull(_merchantId);
		assertNotNull(_gatewayUrl);
	}
	
	@Test
	public void checkWalletConnector() {
		WalletConnector walletConn = WalletConnector.initConn(_chainPubKey, _merchantPriKey, _aesKey, _merchantId, _gatewayUrl);
		assertNotNull(walletConn);
	}
	
	@Test
	public void checkChainConnector() {
		ChainConnector chainConn = ChainConnector.initConn(_chainPubKey, _merchantPriKey, _aesKey, _merchantId, _gatewayUrl);
		assertNotNull(chainConn);
	}

}
