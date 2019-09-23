package hashstacs.sdk.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.spongycastle.util.encoders.Hex;

import com.hashstacs.sdk.crypto.GspECKey;

import hashstacs.sdk.chain.ChainConnector;
import hashstacs.sdk.request.IssueTokenReqBO;
import hashstacs.sdk.util.StacsUtil;
import lombok.Getter;

@Getter
public class BaseSetupTest {

	public static String _chainPubKey;
	public static String _merchantPriKey;
	public static String _aesKey;
	public static String _merchantId;
	public static String _gatewayUrl;
	
	public static ChainConnector _chainConn;
	
	public static GspECKey _sponsorSignKey;
	public static String _sponsorWalletAddress;
	
	public static GspECKey _issuerSignKey;
	public static String _tokenCustodyAddress;
	
	private static String CONFIG_PROPERTIES = "config_dev.properties";
	
	protected static boolean isInitialized = false;
	//generate a token for the day to test
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	public static final String TEST_TOKEN = "TOK" + DATE_FORMAT.format(System.currentTimeMillis());
	public static Boolean isTokenCreated = false;
		
	public static void setup() {
		if(!isInitialized) {
			initialize();
			isInitialized = true;
		}
	}
	
	private static void initialize() {
		_chainPubKey = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_PUBKEY);
		_merchantPriKey = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_PRIKEY);
		_aesKey = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_AESKEY);
		_merchantId = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_MERCHANTID);
		_gatewayUrl = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_GATEWAY);
		
		_chainConn = ChainConnector.initConn(_chainPubKey, _merchantPriKey, _aesKey, _merchantId, _gatewayUrl);
		
		_sponsorSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.SPONSOR_KEY)));
		_sponsorWalletAddress = _sponsorSignKey.getHexAddress();
		
		_issuerSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.ISSUER_KEY)));
		_tokenCustodyAddress = _issuerSignKey.getHexAddress();	
	}
}
