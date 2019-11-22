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

import hashstacs.sdk.DistributePaymentsSample;
import hashstacs.sdk.chain.ChainConnector;
import hashstacs.sdk.request.DistributePaymentReqBO;
import hashstacs.sdk.request.GeneratePaymentRecordReqBO;
import hashstacs.sdk.request.GetTokenHoldersReqBO;
import hashstacs.sdk.response.AsyncRespBO;
import hashstacs.sdk.response.DistributePaymentStatusRespBO;
import hashstacs.sdk.response.GetTokenHoldersRespBO;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.TokenUnit;

public class DistributePaymentsTest {
	private static String _chainPubKey;
	private static String _merchantPriKey;
	private static String _aesKey;
	private static String _merchantId;
	private static String _gatewayUrl;
	
	private static ChainConnector _chainConn;
	
	private static GspECKey _sponsorSignKey;
	private static String _sponsorWalletAddress;
	
	private String _paymentRecordId;
	private DistributePaymentReqBO _distributePaymentRequest;
	
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
	}
	
	/**
	 * create the basic payment request object for each test
	 */
	@Before
	public void constructBaseDistributePaymentRequestBO() {
		GeneratePaymentRecordReqBO paymentRecordRequest = new GeneratePaymentRecordReqBO();
		AsyncRespBO paymentRecordResponse = _chainConn.generatePaymentRecord(paymentRecordRequest, _sponsorSignKey);
		_paymentRecordId = paymentRecordResponse.get_txId();	
		
		GetTokenHoldersReqBO redeemTokenHolders = new GetTokenHoldersReqBO(_paymentRecordId,TEST_TOKEN);
		GetTokenHoldersRespBO redeemTokenHoldersResp = _chainConn.getTokenHolders(redeemTokenHolders);
		
		DistributePaymentsSample distributePayment = new DistributePaymentsSample("USD",_sponsorWalletAddress,TEST_TOKEN,_paymentRecordId,redeemTokenHoldersResp);
		_distributePaymentRequest = distributePayment.getDistributePaymentRequest();
	}
	/**
	 * check that payments can be successfully distributed
	 * @throws InterruptedException
	 */
	@Test
	public void checkPayments() throws InterruptedException {
		
		AsyncRespBO distributePaymentResponse = _chainConn.distributePayment(_distributePaymentRequest, _sponsorSignKey);
		Thread.sleep(StacsUtil.POLL_WAIT_TIME_IN_MS);
		DistributePaymentStatusRespBO distributePaymentStatus = _chainConn.getDistributePaymentStatus(distributePaymentResponse.get_txId());		
		assertNotNull(distributePaymentStatus.getBlockHeight());
	}
	
	/**
	 * Check that if there are no payment recipients, the request will not go to the chain
	 */
	@Test
	public void checkValidationNoPaymentRequests() {
		TokenUnit paymentPerToken = new TokenUnit("USD", new BigDecimal("2"));
		DistributePaymentReqBO emptyReqBO = new DistributePaymentReqBO(_paymentRecordId,paymentPerToken);
		AsyncRespBO distributePaymentResponse = _chainConn.distributePayment(emptyReqBO, _sponsorSignKey);
		assertNull(distributePaymentResponse);
	}
}
