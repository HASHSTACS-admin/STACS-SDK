package hashstacs.sdk.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import com.hashstacs.sdk.crypto.GspECKey;

import hashstacs.sdk.chain.ChainConnector;
import hashstacs.sdk.request.FreezeWalletReqBO;
import hashstacs.sdk.response.AsyncRespBO;
import hashstacs.sdk.response.FreezeOrUnfreezeStatusRespBO;
import hashstacs.sdk.util.StacsUtil;

public class FreezeWalletTest {
	private static String _chainPubKey;
	private static String _merchantPriKey;
	private static String _aesKey;
	private static String _merchantId;
	private static String _gatewayUrl;
	
	private static ChainConnector _chainConn;
	
	private static GspECKey _investorSignKey;
	private static String _investorWalletAddress;
	
	private static GspECKey _freezeKey;
	private static String _freezeKeyAddress;
	
	private AsyncRespBO _freezeResponse;
	
	private FreezeOrUnfreezeStatusRespBO _freezeStatus;
	
	private static String CONFIG_PROPERTIES = "config.properties";
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
		
		_investorSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.INVESTOR_KEY)));
		_investorWalletAddress = _investorSignKey.getHexAddress();
		
		_freezeKey = 
				GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.FREEZE_PERMISSION_KEY)));
		_freezeKeyAddress=_freezeKey.getHexAddress();
	}
	
	/**
	 * freeze the wallet before each test case
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Before
	public void freezeWalletTokens() throws InterruptedException, ParseException {
		FreezeWalletReqBO freezeRequest = new FreezeWalletReqBO(FreezeWalletReqBO.FreezeFunctionEnum.FREEZE);
		freezeRequest.setCondition(FreezeWalletReqBO.FreezeTokenTypeEnum.TOKEN);
		freezeRequest.setTargetAddress(_investorWalletAddress);
		freezeRequest.setFreezeAuthorityAddress(_freezeKeyAddress);
		freezeRequest.setEndTime(DATE_FORMAT.parse("2030-12-31 00:00:00"));
		
		_freezeResponse = _chainConn.freezeOrUnfreezeWallet(freezeRequest, _freezeKey);
		Thread.sleep(StacsUtil.POLL_WAIT_TIME_IN_MS);
	}
	
	/**
	 * check that the freeze request was successful
	 */
	@Test
	public void checkFreezeRequestBOStatus() {
		assertNotNull(_freezeResponse);
	}
	
	/**
	 * Check that the freeze query request is successful
	 * @throws InterruptedException
	 */
	@Test
	public void checkFreezeQueryResponseStatus() throws InterruptedException {
		_freezeStatus = _chainConn.getFreezeOrUnfreezeWalletStatus(_freezeResponse.get_txId());
		assertTrue(_freezeStatus.getRawRespCode().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespCode())==0);
	}
	
	/**
	 * check that the signature is being validated before being sent
	 * @throws ParseException
	 */
	@Test
	public void checkValidationSignature() throws ParseException {
		FreezeWalletReqBO freezeRequest = new FreezeWalletReqBO(FreezeWalletReqBO.FreezeFunctionEnum.FREEZE);
		freezeRequest.setCondition(FreezeWalletReqBO.FreezeTokenTypeEnum.TOKEN);
		freezeRequest.setTargetAddress(_investorWalletAddress);
		freezeRequest.setFreezeAuthorityAddress(_freezeKeyAddress);
		freezeRequest.setEndTime(DATE_FORMAT.parse("2030-12-31 00:00:00"));
		
		AsyncRespBO failFreezeResponse = _chainConn.freezeOrUnfreezeWallet(freezeRequest, _investorSignKey);
		assertNull(failFreezeResponse);
	}
	
	/**
	 * Unfreeze the wallet once all tests are completed
	 */
	@AfterClass
	public static void unfreezeWallet() {
		FreezeWalletReqBO freezeRequest = new FreezeWalletReqBO(FreezeWalletReqBO.FreezeFunctionEnum.UNFREEZE);
		freezeRequest.setCondition(FreezeWalletReqBO.FreezeTokenTypeEnum.TOKEN);
		freezeRequest.setTargetAddress(_investorWalletAddress);
		freezeRequest.setFreezeAuthorityAddress(_freezeKeyAddress);
		_chainConn.freezeOrUnfreezeWallet(freezeRequest, _freezeKey);
	}

}
