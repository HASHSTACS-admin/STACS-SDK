package hashstacs.sdk.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import com.hashstacs.sdk.crypto.GspECKey;

import hashstacs.sdk.IssueTokenSample;
import hashstacs.sdk.chain.ChainConnector;
import hashstacs.sdk.request.IssueTokenReqBO;	
import hashstacs.sdk.response.AsyncRespBO;
import hashstacs.sdk.response.IssueTokenStatusRespBO;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.TokenUnit;

public class IssueTokenTest {
	private static String _chainPubKey;
	private static String _merchantPriKey;
	private static String _aesKey;
	private static String _merchantId;
	private static String _gatewayUrl;
	
	private static ChainConnector _chainConn;
	
	private static GspECKey _sponsorSignKey;
	private static String _sponsorWalletAddress;
	
	private static GspECKey _issuerSignKey;
	private static String _tokenCustodyAddress;
	
	private IssueTokenReqBO _issueTokenReqBO;
	
	private static String CONFIG_PROPERTIES = "config.properties";

	//generate a token for the day to test
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final String TEST_TOKEN = "TOK" + DATE_FORMAT.format(System.currentTimeMillis());
	
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
		
		_sponsorSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.SPONSOR_KEY)));
		_sponsorWalletAddress = _sponsorSignKey.getHexAddress();
		
		_issuerSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.ISSUER_KEY)));
		_tokenCustodyAddress = _issuerSignKey.getHexAddress();			
	}
	
	/**
	 * Setup the IssueTokenReqBO object before each test
	 */
	@Before
	public void constructBaseTokenReqBO() {
		IssueTokenSample issueTokenInfo = new IssueTokenSample(_sponsorWalletAddress,_tokenCustodyAddress,TEST_TOKEN);
		_issueTokenReqBO = issueTokenInfo.getIssueRequest();
	}
	
	/**
	 * Check that a new token can be issued 
	 * @throws InterruptedException
	 */
	@Test
	public void checkIssueNewToken() throws InterruptedException {
	
		AsyncRespBO issueTokenResponse = _chainConn.issueToken(_issueTokenReqBO, _sponsorSignKey);
		System.out.println(issueTokenResponse.get_txId());
		assertTrue(issueTokenResponse.get_responseCode().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespCode()) ==0);
		Thread.sleep(StacsUtil.POLL_WAIT_TIME_IN_MS);
		
		IssueTokenStatusRespBO issueTokenStatus = _chainConn.getIssueTokenStatus(issueTokenResponse.get_txId());
		assertTrue(issueTokenStatus.getResultCode() == AsyncRespBO.asyncResponseCodesEnum.SUCCESS);
	}
	
	/**
	 * check that the available amount is less than total supply 
	 * @throws ParseException
	 */
	@Test
	public void checkTokenValidationAvailableAmount() throws ParseException {
	
		BigDecimal reserveAmount = _issueTokenReqBO.get_tokenAndTotalSupply().get_amount().add(new BigDecimal(50L));
		TokenUnit reservedAmount = new TokenUnit(TEST_TOKEN,reserveAmount);
		_issueTokenReqBO.setReservedAmount(reservedAmount);
		
		AsyncRespBO issueTokenResponse = _chainConn.issueToken(_issueTokenReqBO, _sponsorSignKey);
		assertNull(issueTokenResponse);
	}
	
	/**
	 * check that the pricing is not a negative number
	 */
	@Test
	public void checkTokenValidationPriceInfo() {
		
		TokenUnit invalidPriceInfo = new TokenUnit(_issueTokenReqBO.get_tokenUnitPricing().get_currency(),new BigDecimal(-1));
		_issueTokenReqBO.setTokenUnitPricing(invalidPriceInfo);
		
		AsyncRespBO issueTokenResponse = _chainConn.issueToken(_issueTokenReqBO, _sponsorSignKey);
		assertNull(issueTokenResponse);
	}
	
	/**
	 * check that the subscription period is valid
	 * @throws ParseException
	 */
	@Test
	public void checkTokenValidationSubscriptionDate() throws ParseException {
		_issueTokenReqBO.setSubscriptionEndDate(StacsUtil.getDateFormat().parse("2000-08-30 18:00:00"));
		AsyncRespBO issueTokenResponse = _chainConn.issueToken(_issueTokenReqBO, _sponsorSignKey);
		assertNull(issueTokenResponse);
	}
	
	/**
	 * check that the max number of lots for subscription is valid
	 */
	@Test
	public void checkTokenValidationSubscribeLots() {
		_issueTokenReqBO.setMaxSubscriptionLots(0);
		AsyncRespBO issueTokenResponse = _chainConn.issueToken(_issueTokenReqBO, _sponsorSignKey);
		assertNull(issueTokenResponse);
	}
	
	/**
	 * check that the token does not accept payment with itself
	 */
	@Test
	public void checkTokenValidationPricingCurrency() {
		_issueTokenReqBO.setTokenUnitPricing(_issueTokenReqBO.get_tokenAndTotalSupply());
		AsyncRespBO issueTokenResponse = _chainConn.issueToken(_issueTokenReqBO, _sponsorSignKey);
		assertNull(issueTokenResponse);
	}
	
	/**
	 * check that the signature validation is being done before being sent to the node
	 */
	//@Test
	public void checkTokenValidationSignature() {
		AsyncRespBO issueTokenResponse = _chainConn.issueToken(_issueTokenReqBO, _issuerSignKey);
		assertNull(issueTokenResponse);
	}
	
	
}
