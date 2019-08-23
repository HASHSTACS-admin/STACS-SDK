package hashstacs.sdk.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import com.hashstacs.sdk.crypto.GspECKey;

import hashstacs.sdk.GrantSubscriptionPermissionSample;
import hashstacs.sdk.IssueTokenSample;
import hashstacs.sdk.SubscriptionSample;
import hashstacs.sdk.chain.ChainConnector;
import hashstacs.sdk.request.GrantSubscribePermReqBO;
import hashstacs.sdk.request.IssueTokenReqBO;
import hashstacs.sdk.request.SubscribeReqBO;
import hashstacs.sdk.request.TxHistoryReqBO;
import hashstacs.sdk.response.AsyncRespBO;
import hashstacs.sdk.response.GrantSubscribePermStatusRespBO;
import hashstacs.sdk.response.IssueTokenStatusRespBO;
import hashstacs.sdk.response.SubscribeStatusRespBO;
import hashstacs.sdk.response.TxHistoryRespBO;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.TokenUnit;
import hashstacs.sdk.wallet.TxHistoryRecord;
import hashstacs.sdk.wallet.WalletConnector;

public class SubscribeTest {

	private static String _chainPubKey;
	private static String _merchantPriKey;
	private static String _aesKey;
	private static String _merchantId;
	private static String _gatewayUrl;
	
	private static ChainConnector _chainConn;
	private static WalletConnector _walletConn;
	
	private static GspECKey _sponsorSignKey;
	private static String _sponsorWalletAddress;
	
	private static GspECKey _issuerSignKey;
	private static String _tokenCustodyAddress;
	
	private static GspECKey _investorSignKey;
	private static String _investorWalletAddress;
	
	private static AsyncRespBO _issueTokenResponse;
	private static IssueTokenStatusRespBO _issueTokenStatus;
	private static AsyncRespBO _grantSubscriptionPermissionResponse;
	private static GrantSubscribePermStatusRespBO _grantSubscriptionPermissionStatus;
	private static SubscribeReqBO _subscribeRequest;
	
	private static String CONFIG_PROPERTIES = "config.properties";
	
	private static Boolean _isTokenAlreadyIssued = Boolean.FALSE;
	private static Boolean _isPermissionGranted = Boolean.FALSE;

	//generate a token for the day to test
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final String TEST_TOKEN = "TOKENSUB" + DATE_FORMAT.format(System.currentTimeMillis());
	
	/**
	 * Initialize all parameters for the test
	 */
	@BeforeClass
	public static void initialize() {
		_chainPubKey = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_PUBKEY);
		_merchantPriKey = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_PRIKEY);
		_aesKey = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_AESKEY);
		_merchantId = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_MERCHANTID);
		_gatewayUrl = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_GATEWAY);
		
		_chainConn = ChainConnector.initConn(_chainPubKey, _merchantPriKey, _aesKey, _merchantId, _gatewayUrl);
		_walletConn = WalletConnector.initConn(_chainPubKey, _merchantPriKey, _aesKey, _merchantId, _gatewayUrl);
		
		_sponsorSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.SPONSOR_KEY)));
		_sponsorWalletAddress = _sponsorSignKey.getHexAddress();
		
		_issuerSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.ISSUER_KEY)));
		_tokenCustodyAddress = _issuerSignKey.getHexAddress();
		
		_investorSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.INVESTOR_KEY)));
		_investorWalletAddress = _investorSignKey.getHexAddress();
		
	}
	
	/**
	 * setup the token for testing subscription and creates one if it does not exist 
	 * @throws InterruptedException
	 */
	@Before
	public void setupIssuedToken() throws InterruptedException {	
		//check if token is already issued, otherwise issue it
		TxHistoryReqBO txHistoryA = new TxHistoryReqBO(TEST_TOKEN,false);
		TxHistoryRecord rec = _walletConn.getTxHistoryForWalletAndOrCurrency(txHistoryA);
		TxHistoryRespBO txHist2 = new TxHistoryRespBO(rec);
		
		if(txHist2!=null) {
			_isTokenAlreadyIssued = (txHist2.getTxHistList().getIssueTxHistory().size() != 0);
		}  
		
		//not issued, proceed to issue the token
		if(!_isTokenAlreadyIssued) {
			IssueTokenSample issueTokenInfo = new IssueTokenSample(_sponsorWalletAddress,_tokenCustodyAddress,TEST_TOKEN);
			IssueTokenReqBO issueTokenRequest = issueTokenInfo.getIssueRequest();
			_issueTokenResponse = _chainConn.issueToken(issueTokenRequest, _sponsorSignKey);
			
			Thread.sleep(StacsUtil.POLL_WAIT_TIME_IN_MS);
			_issueTokenStatus = _chainConn.getIssueTokenStatus(_issueTokenResponse.get_txId());
		}
		//if no permission, grant it first
		if(!_isPermissionGranted) {
			//grant subscribe permission to wallet
			GrantSubscriptionPermissionSample grantPermissionInfo = new GrantSubscriptionPermissionSample(_investorWalletAddress,TEST_TOKEN,_sponsorWalletAddress);
			GrantSubscribePermReqBO grantSubscriptionPermissionRequest = grantPermissionInfo.getGrantSubscriptionPermissionRequest();
			_grantSubscriptionPermissionResponse = _chainConn.grantSubscriptionPermission(grantSubscriptionPermissionRequest, _sponsorSignKey);
			
			Thread.sleep(StacsUtil.POLL_WAIT_TIME_IN_MS);
			_grantSubscriptionPermissionStatus = _chainConn.getGrantSubscriptionPermissionStatus(_grantSubscriptionPermissionResponse.get_txId());	
		}	
	}
	
	/**
	 * setup the subscribeReqBO object for each test
	 */
	@BeforeClass
	public static void setupSubscribeReqBO() {
		TokenUnit tokenToSubscribe = new TokenUnit(TEST_TOKEN,new BigDecimal(20));
		SubscriptionSample subscribeInfo = new SubscriptionSample(_investorWalletAddress,tokenToSubscribe);
		_subscribeRequest = subscribeInfo.getSubscriptionRequest();
	}
	
	/**
	 * check that the issuance of a new token was successful
	 */
	@Test
	public void checkIssueOfNewToken() {
		if(!_isTokenAlreadyIssued) {
			assertTrue(verifySuccessStatus(_issueTokenStatus.getResultCode().getRespCode()));
		}
	}
	
	/**
	 * check that the subscription permission granted was successful
	 */
	@Test
	public void checkSubscriptionPermission() {
		assertTrue(verifySuccessStatus(_grantSubscriptionPermissionResponse.get_responseCode()));
		assertTrue(verifySuccessStatus(_grantSubscriptionPermissionStatus.getResultCode().getRespCode()));
	}
	
	/**
	 * check that signatures validations are being run before the request is sent
	 */
	@Test
	public void checkValidationSignature() {
		AsyncRespBO subscribeResponse = _chainConn.subscribeToToken(_subscribeRequest, _sponsorSignKey);
		assertNull(subscribeResponse);
	}
	
	/**
	 * Check that subscription can be executed
	 * @throws InterruptedException
	 */
	@Test
	public void subscribeTest() throws InterruptedException {
		
		AsyncRespBO subscribeResponse = _chainConn.subscribeToToken(_subscribeRequest, _investorSignKey);
		assertTrue(verifySuccessStatus(subscribeResponse.get_responseCode()));
		
		Thread.sleep(StacsUtil.POLL_WAIT_TIME_IN_MS);
		SubscribeStatusRespBO subscribeStatus = _chainConn.getSubscriptionStatus(subscribeResponse.get_txId());
		
		assertTrue(verifySuccessStatus(subscribeStatus.getRawRespCode()));
	}
	
	/**
	 * check if the permission grants have 1 of the success response codes
	 * @param respCode
	 * @return
	 */
	public Boolean verifySuccessStatus(String respCode) {
		if(respCode.compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespCode())==0 || 
				respCode.compareTo(AsyncRespBO.asyncResponseCodesEnum.GRANT_PERMISSION_SUCCESS.getRespCode())==0 ||
						respCode.compareTo(AsyncRespBO.asyncResponseCodesEnum.GRANT_PERMISSION_VERIFIED_SUCCESS.getRespCode())==0) {
			return true;
		}
		return false;
		
	}
}
