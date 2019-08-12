package hashstacs.sdk;

import java.math.BigDecimal;

import org.spongycastle.util.encoders.Hex;

import com.hashstacs.sdk.crypto.GspECKey;

import hashstacs.sdk.chain.ChainConnector;
import hashstacs.sdk.request.DistributePaymentReqBO;
import hashstacs.sdk.request.FreezeWalletReqBO;
import hashstacs.sdk.request.GeneratePaymentRecordReqBO;
import hashstacs.sdk.request.GrantSubscribePermReqBO;
import hashstacs.sdk.request.IssueTokenReqBO;
import hashstacs.sdk.request.SubscribeReqBO;
import hashstacs.sdk.request.TransferTokenReqBO;
import hashstacs.sdk.request.TxHistoryBetweenBlocksReqBO;
import hashstacs.sdk.request.TxHistoryReqBO;
import hashstacs.sdk.request.WalletBalanceReqBO;
import hashstacs.sdk.response.AsyncRespBO;
import hashstacs.sdk.response.DistributePaymentStatusRespBO;
import hashstacs.sdk.response.FreezeOrUnfreezeStatusRespBO;
import hashstacs.sdk.response.GeneratePaymentRecordRespBO;
import hashstacs.sdk.response.GrantSubscribePermStatusRespBO;
import hashstacs.sdk.response.IssueTokenStatusRespBO;
import hashstacs.sdk.response.LatestBlockRespBO;
import hashstacs.sdk.response.SubscribeStatusRespBO;
import hashstacs.sdk.response.TransferTokenStatusRespBO;
import hashstacs.sdk.response.TxHistoryRespBO;
import hashstacs.sdk.response.WalletBalanceRespBO;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.TokenUnit;
import hashstacs.sdk.wallet.WalletConnector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleUsage {
	
	private static String _chainPubKey;
	private static String _merchantPriKey;
	private static String _aesKey;
	private static String _merchantId;
	private static String _gatewayUrl;
	
	private static WalletConnector _walletConn;
	private static ChainConnector _chainConn;
	
	private static GspECKey _sponsorSignKey;
	private static String _sponsorWalletAddress;
	private static GspECKey _issuerSignKey;
	private static String _tokenCustodyAddress;
	private static GspECKey _investorSignKey;
	private static String _investorWalletAddress;
	private static GspECKey _freezeKey;
	private static String _freezeKeyAddress;
	
	private static String SAMPLE_TOKEN = "BOND2019";
	private static String CONFIG_PROPERTIES = "config.properties";
	
	public static void main(String[] args)  {
	
		//initializes variables for sample data
		initialize();
		
		//contains all chain functions and sample data of using them
		//ChainSampleUsage();
		
		//contains all wallet functions and sample data of using them
		//WalletSampleUsage();
		
		//Creates a public/private key pair and an address, prints to line
		StacsUtil.createWallet();
		
	}
	
	private static void initialize() {
		//load configuration file
		
		_chainPubKey = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_PUBKEY);
		_merchantPriKey = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_PRIKEY);
		_aesKey = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_AESKEY);
		_merchantId = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_MERCHANTID);
		_gatewayUrl = StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.NODE_GATEWAY);
		
		//initialize Wallet and Blockchain connections
		_walletConn = WalletConnector.initConn(_chainPubKey, _merchantPriKey, _aesKey, _merchantId, _gatewayUrl);
		_chainConn = ChainConnector.initConn(_chainPubKey, _merchantPriKey, _aesKey, _merchantId, _gatewayUrl);
		
		/*SAMPLE KEY PAIRS VALUES - FOR TESTING PURPOSES ONLY*/
		_sponsorSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.SPONSOR_KEY)));
		_sponsorWalletAddress = _sponsorSignKey.getHexAddress();
		
		_issuerSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.ISSUER_KEY)));
		_tokenCustodyAddress = _issuerSignKey.getHexAddress();
		
		_investorSignKey = GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.INVESTOR_KEY)));
		_investorWalletAddress = _investorSignKey.getHexAddress();
		
		_freezeKey = 
				GspECKey.fromPrivate(Hex.decode(StacsUtil.getConfigProperty(CONFIG_PROPERTIES,StacsUtil.ConfigEnums.FREEZE_PERMISSION_KEY)));
		_freezeKeyAddress=_freezeKey.getHexAddress();
	}
	
	private static void ChainSampleUsage() {

		/**
		 *Issuing a token on a STACS Native network with the request object @IssueTokenReqBO and 
		 *receive the asynchronous response object @AsyncRespBO 
		 *
		 *Verify the request status with the @transaction_id as part of the request object and
		 *receive the response object @IssueTokenStatusRespBO
		 */
		IssueTokenSample issueTokenInfo = new IssueTokenSample(_sponsorWalletAddress,_tokenCustodyAddress,SAMPLE_TOKEN);
		IssueTokenReqBO issueTokenRequest = issueTokenInfo.getIssueRequest();
		AsyncRespBO issueTokenResponse = _chainConn.issueToken(issueTokenRequest, _sponsorSignKey);
		log.debug("Issued Token Request Transaction Id: " + issueTokenResponse.get_txId());
		
		IssueTokenStatusRespBO issueTokenStatus = _chainConn.getIssueTokenStatus(issueTokenResponse.get_txId());
		
		/**
		 *Granting subscription permission to investor wallets with the request object @GrantSubscribePermReqBO and 
		 *receive the asynchronous response object @AsyncRespBO
		 *
		 *Verify the request status with the @transaction_id as part of the request object and
		 *receive the response object @GrantSubscribePermStatusRespBO
		 */
		GrantSubscriptionPermissionSample grantPermissionInfo = new GrantSubscriptionPermissionSample(_investorWalletAddress,SAMPLE_TOKEN,_sponsorWalletAddress);
		GrantSubscribePermReqBO grantSubscriptionPermissionRequest = grantPermissionInfo.getGrantSubscriptionPermissionRequest();
		AsyncRespBO grantSubscriptionPermissionResponse = _chainConn.grantSubscriptionPermission(grantSubscriptionPermissionRequest, _sponsorSignKey);
		log.debug("Grant Subscription Permission Request Transaction Id: " + grantSubscriptionPermissionResponse.get_txId());
		
		GrantSubscribePermStatusRespBO grantSubscriptionPermissionStatus = _chainConn.getGrantSubscriptionPermissionStatus(grantSubscriptionPermissionResponse.get_txId());
				
		/**
		 *Investors subscribing to tokens with the request object @SubscribeReqBO and 
		 *receive the asynchronous response object @AsyncRespBO
		 *
		 *Verify the request status with the @transaction_id as part of the request object and
		 *receive the response object @SubscribeStatusRespBO
		 */
		TokenUnit tokenToSubscribe = new TokenUnit(SAMPLE_TOKEN,new BigDecimal(20));
		SubscriptionSample subscribeInfo = new SubscriptionSample(_investorWalletAddress,tokenToSubscribe);
		SubscribeReqBO subscribeRequest = subscribeInfo.getSubscriptionRequest();
		AsyncRespBO subscribeResponse = _chainConn.subscribeToToken(subscribeRequest, _investorSignKey);
		log.debug("Subscription Request Transaction Id: " + subscribeResponse.get_txId());
		
		SubscribeStatusRespBO subscribeStatus = _chainConn.getSubscriptionStatus(subscribeResponse.get_txId());
		
		/**
		 *Sponsor generates a payment record to establish the blockheight and datetime where
		 * wallet balances are calculated in order to perform a payment 
		 * with the request object @GeneratePaymentRecordReqBO and 
		 *receive the asynchronous response object @AsyncRespBO
		 *
		 *Verify the request status with the @transaction_id as part of the request object and
		 *receive the response object @GeneratePaymentRecordRespBO
		 */
		GeneratePaymentRecordReqBO paymentRecordRequest = new GeneratePaymentRecordReqBO();
		AsyncRespBO paymentRecordResponse = _chainConn.generatePaymentRecord(paymentRecordRequest, _sponsorSignKey);
		log.debug("payment record request Transaction Id:" + paymentRecordResponse.get_txId());
	
		GeneratePaymentRecordRespBO payRecordStatus = _chainConn.getPaymentRecordStatus(paymentRecordResponse.get_txId());
		
		/**
		 *Sponsor distributes payments to investor wallet addresses 
		 *with the request object @DistributePaymentReqBO and 
		 *receive the asynchronous response object @AsyncRespBO
		 *
		 *Verify the request status with the @transaction_id as part of the request object and
		 *receive the response object @DistributePaymentStatusRespBO
		 */
		DistributePaymentsSample distributePayment = new DistributePaymentsSample("USD",_sponsorWalletAddress,SAMPLE_TOKEN,paymentRecordResponse.get_txId());
		DistributePaymentReqBO distributePaymentRequest = distributePayment.getDistributePaymentRequest();
		AsyncRespBO distributePaymentResponse = _chainConn.distributePayment(distributePaymentRequest, _sponsorSignKey);
		log.debug("distribute payment request Transaction id: " + distributePaymentResponse.get_txId());
		
		DistributePaymentStatusRespBO distributePaymentStatus = _chainConn.getDistributePaymentStatus(distributePaymentResponse.get_txId());		

		/**
		 *Freeze or Unfreeze either all tokens, all stable coins or both in a wallet address
		 *with the request object @FreezeWalletReqBO and 
		 *receive the asynchronous response object @AsyncRespBO
		 *
		 *Verify the request status with the @transaction_id as part of the request object and
		 *receive the response object @FreezeOrUnfreezeStatusRespBO
		 */
		FreezeWalletReqBO freezeRequest = new FreezeWalletReqBO(FreezeWalletReqBO.FreezeFunctionEnum.FREEZE);
		freezeRequest.setCondition(FreezeWalletReqBO.FreezeTokenTypeEnum.TOKEN);
		freezeRequest.setTargetAddress(_investorWalletAddress);
		freezeRequest.setFreezeAuthorityAddress(_freezeKeyAddress);
		AsyncRespBO freezeResponse = _chainConn.freezeOrUnfreezeWallet(freezeRequest, _freezeKey);
		log.debug("freeze request Transaction id: " + freezeResponse.get_txId());
		
		FreezeOrUnfreezeStatusRespBO freezeStatus = _chainConn.getFreezeOrUnfreezeWalletStatus(freezeResponse.get_txId());
				
		/**
		 *Get the transactions between block heights per query
		 *with the request object @TxHistoryBetweenBlocksReqBO and receive the synchronous 
		 *response object @TxHistoryRespBO
		 */
		long startBlock = 300L;
		long endBlock = 315L;
		TxHistoryBetweenBlocksReqBO queryBlockBO = new TxHistoryBetweenBlocksReqBO(startBlock,endBlock);
		TxHistoryRespBO TxsInBlocks = new TxHistoryRespBO(_chainConn.getTxsBetweenBlocks(queryBlockBO));
		
		/**
		 *GGet the latest Block Height with the synchronous response object @LatestBlockRespBO
		 */
		LatestBlockRespBO latestBlockHeight = _chainConn.getLatestBlockHeight();		
		
	}
	private static void WalletSampleUsage() {
		/**
		 *Investors withdrawing or transferring tokens to a different wallet address
		 *with the request object @TransferTokenReqBO and 
		 *receive the asynchronous response object @AsyncRespBO
		 *
		 *Verify the request status with the @transaction_id as part of the request object and
		 *receive the response object @TransferTokenStatusRespBO
		 */
		TokenUnit transferAmount = new TokenUnit(SAMPLE_TOKEN, new BigDecimal(10));
		TransferTokenSample transferInfo = new TransferTokenSample(_tokenCustodyAddress,_investorWalletAddress,transferAmount);
		TransferTokenReqBO transferRequest = transferInfo.getTransferTokenRequest();
		AsyncRespBO transferResponse = _walletConn.transferToken(transferRequest, _issuerSignKey);
		log.debug("transfer request Transaction Id:" + transferResponse.get_txId());
		
		TransferTokenStatusRespBO transferStatus = _walletConn.getTransferTokenStatus(transferResponse.get_txId());
		
		/**
		 *Investors query their wallet balance
		 *with the request object @WalletBalanceReqBO and receive the synchronous 
		 *response object @WalletBalanceRespBO
		 */
		WalletBalanceReqBO investorWalletBalanceReq = new WalletBalanceReqBO(_sponsorWalletAddress,SAMPLE_TOKEN);
		WalletBalanceRespBO investorWalletBalanceResponse = _walletConn.getWalletBalance(investorWalletBalanceReq);
			
					
		/**
		 * Get the full transaction history for:
		 * a) a token in all wallets
		 * b) all tokens in a wallet
		 * c) a token in a wallet
		 * 
		 * Query the full transaction history with the request object @TxHistoryReqBO and
		 * receive the synchronous response object @TxHistoryRespBO
		 */		
		String historyTokenCode = "USD";
		TxHistoryReqBO txHistoryA = new TxHistoryReqBO(historyTokenCode,false);
		TxHistoryReqBO txHistoryB = new TxHistoryReqBO(_tokenCustodyAddress,true);
		TxHistoryReqBO txHistoryC = new TxHistoryReqBO(_tokenCustodyAddress,historyTokenCode); 
		TxHistoryRespBO txHist2 = new TxHistoryRespBO(_walletConn.getTxHistoryForWalletAndOrCurrency(txHistoryB));
		
	}
	
}
