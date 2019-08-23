package hashstacs.sdk.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import com.hashstacs.sdk.crypto.GspECKey;

import hashstacs.sdk.TransferTokenSample;
import hashstacs.sdk.request.TransferTokenReqBO;
import hashstacs.sdk.request.WalletBalanceReqBO;
import hashstacs.sdk.response.AsyncRespBO;
import hashstacs.sdk.response.TransferTokenStatusRespBO;
import hashstacs.sdk.response.WalletBalanceRespBO;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.TokenUnit;
import hashstacs.sdk.wallet.WalletConnector;

public class TransferTokenTest {
	private static String _chainPubKey;
	private static String _merchantPriKey;
	private static String _aesKey;
	private static String _merchantId;
	private static String _gatewayUrl;
	
	private static WalletConnector _walletConn;
		
	private static GspECKey _issuerSignKey;
	private static String _tokenCustodyAddress;
	
	private static GspECKey _investorSignKey;
	private static String _investorWalletAddress;
	
	private TransferTokenReqBO _transferRequest;
	
	private TokenUnit _recipientWalletInitialBalance;
	private TokenUnit _senderWalletInitialBalance;
	
	private static String CONFIG_PROPERTIES = "config.properties";
	private static final String DIGITAL_CURRENCY = "USD";
	private static final int AMOUNT_TO_TRANSFER = 1;
	
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
		
		_walletConn = WalletConnector.initConn(_chainPubKey, _merchantPriKey, _aesKey, _merchantId, _gatewayUrl);
				
		_issuerSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.ISSUER_KEY)));
		_tokenCustodyAddress = _issuerSignKey.getHexAddress();	
		
		_investorSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.INVESTOR_KEY)));
		_investorWalletAddress = _investorSignKey.getHexAddress();
	}
	
	/**
	 * setup the request object for each test
	 */
	@Before
	public void setupTransferTokenReqBO() {
		TokenUnit transferAmount = new TokenUnit(DIGITAL_CURRENCY, new BigDecimal(AMOUNT_TO_TRANSFER));
		TransferTokenSample transferInfo = new TransferTokenSample(_tokenCustodyAddress,_investorWalletAddress,transferAmount);
		_transferRequest = transferInfo.getTransferTokenRequest();
		
	}
	
	/**
	 * retrieve initial wallet balance of the sender before the transfer 
	 */
	@Before
	public void checkSenderInitialWalletBalance() {
		WalletBalanceReqBO investorWalletBalanceReq = new WalletBalanceReqBO(_tokenCustodyAddress,DIGITAL_CURRENCY);
		WalletBalanceRespBO investorWalletBalanceResponse = _walletConn.getWalletBalance(investorWalletBalanceReq);
		_senderWalletInitialBalance = investorWalletBalanceResponse.getTotalSupply();
	}
	
	/**
	 * retrieve initial wallet balance of the recipient before the transfer
	 */
	@Before
	public void checkRecipientInitialWalletBalance() {
		WalletBalanceReqBO investorWalletBalanceReq = new WalletBalanceReqBO(_investorWalletAddress,DIGITAL_CURRENCY);
		WalletBalanceRespBO investorWalletBalanceResponse = _walletConn.getWalletBalance(investorWalletBalanceReq);
		_recipientWalletInitialBalance = investorWalletBalanceResponse.getTotalSupply();
	}
	
	/**
	 * ensure that signature validation is done before request is sent
	 */
	@Test
	public void checkValidationSignature() {
		AsyncRespBO transferResponse = _walletConn.transferToken(_transferRequest, _investorSignKey);
		assertNull(transferResponse);
		
	}
	
	/**
	 * transfer funds and check wallet balances of the sender and recipient
	 * @throws InterruptedException
	 */
	@Test
	public void transferFunds() throws InterruptedException {
		AsyncRespBO transferResponse = _walletConn.transferToken(_transferRequest, _issuerSignKey);
		assertNotNull(transferResponse);
		assertTrue(transferResponse.get_responseCode().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespCode())==0);
		
		Thread.sleep(StacsUtil.POLL_WAIT_TIME_IN_MS);
		TransferTokenStatusRespBO transferStatus = _walletConn.getTransferTokenStatus(transferResponse.get_txId());
		assertTrue(transferStatus.getRawRespCode().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespCode())==0);
		
		//validate recipient wallet balance
		WalletBalanceReqBO recipientWalletBalanceReq = new WalletBalanceReqBO(_investorWalletAddress,DIGITAL_CURRENCY);
		WalletBalanceRespBO recipientWalletBalanceResponse = _walletConn.getWalletBalance(recipientWalletBalanceReq);
		assertTrue(recipientWalletBalanceResponse.getTotalSupply().get_amount().compareTo(_recipientWalletInitialBalance.get_amount().add(new BigDecimal(AMOUNT_TO_TRANSFER)))==0);
		
		//validate sender wallet balance
		WalletBalanceReqBO senderWalletBalanceReq = new WalletBalanceReqBO(_tokenCustodyAddress,DIGITAL_CURRENCY);
		WalletBalanceRespBO senderWalletBalanceResponse = _walletConn.getWalletBalance(senderWalletBalanceReq);
		assertTrue(senderWalletBalanceResponse.getTotalSupply().get_amount().compareTo(_senderWalletInitialBalance.get_amount().subtract(new BigDecimal(AMOUNT_TO_TRANSFER)))==0);
	}
	

}
