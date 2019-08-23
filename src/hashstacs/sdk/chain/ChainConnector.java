package hashstacs.sdk.chain;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hashstacs.client.StoClient;
import com.hashstacs.client.WalletClient;
import com.hashstacs.sdk.crypto.GspECKey;
import com.hashstacs.sdk.wallet.dock.bo.CasDecryptReponse;

import hashstacs.sdk.request.DistributePaymentReqBO;
import hashstacs.sdk.request.FreezeWalletReqBO;
import hashstacs.sdk.request.GeneratePaymentRecordReqBO;
import hashstacs.sdk.request.GrantSubscribePermReqBO;
import hashstacs.sdk.request.IssueTokenReqBO;
import hashstacs.sdk.request.SubscribeReqBO;
import hashstacs.sdk.request.TxHistoryBetweenBlocksReqBO;
import hashstacs.sdk.response.AsyncRespBO;
import hashstacs.sdk.response.DistributePaymentStatusRespBO;
import hashstacs.sdk.response.FreezeOrUnfreezeStatusRespBO;
import hashstacs.sdk.response.GeneratePaymentRecordRespBO;
import hashstacs.sdk.response.GrantSubscribePermStatusRespBO;
import hashstacs.sdk.response.IssueTokenStatusRespBO;
import hashstacs.sdk.response.LatestBlockRespBO;
import hashstacs.sdk.response.SubscribeStatusRespBO;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.StacsResponseEnums.DistributePaymentStatusResponseEnum;
import hashstacs.sdk.util.StacsResponseEnums.FreezeOrUnfreezeStatusResponseEnum;
import hashstacs.sdk.util.StacsResponseEnums.GrantSubscribePermStatusResponseEnum;
import hashstacs.sdk.util.StacsResponseEnums.IssueTokenStatusResponseEnum;
import hashstacs.sdk.util.StacsResponseEnums.LatestBlockResponseEnum;
import hashstacs.sdk.util.StacsResponseEnums.PaymentRecordStatusResponseEnum;
import hashstacs.sdk.util.StacsResponseEnums.SubscribeStatusResponseEnum;
import hashstacs.sdk.util.StacsResponseEnums.WalletTokenTxHistoryResponseEnum;
import hashstacs.sdk.wallet.TxHistoryRecord;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChainConnector {
	
	private static ChainConnector _bcConn = null;
	
	private static StoClient _bcBaseConn = null;
	private static WalletClient _wBaseConn = null;

	private ChainConnector(String chainPubKey, String merchantPriKey, String aesKey, String merchantId, String url) {
		_bcBaseConn = new StoClient(chainPubKey,merchantPriKey,aesKey,merchantId,url);
		_wBaseConn = new WalletClient(chainPubKey,merchantPriKey,aesKey,merchantId,url);
	}
	
	public static ChainConnector getConn() {
		return _bcConn;
	}
	
	public static ChainConnector initConn(String chainPubKey, String merchantPriKey, String aesKey, String merchantId, String gatewayUrl) {
		if(_bcConn==null) {
			_bcConn = new ChainConnector(chainPubKey, merchantPriKey, aesKey, merchantId, gatewayUrl);
		}
		return _bcConn;
	}
	
	/**
	 * returns all transactions within a specified range of blocks, inclusive of the start block specified
	 * @param walletQueryVO
	 * @return
	 */
	public TxHistoryRecord getTxsBetweenBlocks(TxHistoryBetweenBlocksReqBO betweenBlocksBO) {
		if(!betweenBlocksBO.isValidationSuccessful(null)) {
			log.debug("request validation failed.");
			return null;
		}
		
		TxHistoryRecord txHist = new TxHistoryRecord();
		CasDecryptReponse rawResponse;
		try {
			rawResponse = _wBaseConn.queryTxsByHeight(betweenBlocksBO.getOrigReqObj());
			txHist.setRawMsg(rawResponse.getMsg());
			txHist.setRawRespCode(rawResponse.getRespCode());
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				return txHist;
			} else if(newObj.isEmpty()){
				return txHist;
			}
			txHist.setRawJsonObject(newObj);
			//this query returns 4 lists as of v1.0, to be extracted independently and stored in TxHistoryRecord			
			for(WalletTokenTxHistoryResponseEnum respProperty : WalletTokenTxHistoryResponseEnum.values()) {
				//for each type of list, get all the transaction rows and stored them into the List object
				JSONArray list = StacsUtil.getTxHistoryList(newObj,respProperty);
				switch(respProperty) {
				case SUBSCRIPTION_LIST:
					txHist.setSubscriptionTxHist(StacsUtil.getSubscribeHistoryFields(list)); 
					break;
				case ISSUANCE_LIST:
					txHist.setIssueTxHist(StacsUtil.getIssueHistoryFields(list));
					break;
				case PAYMENT_RECORD_LIST:
					txHist.setPaymentRecordTxHist(StacsUtil.getPaymentRecordHistoryFields(list));
					break;
				case TRANSFER_LIST:
					txHist.setTransferTxHist(StacsUtil.getTransferHistoryFields(list));
					break;
				default:
					break;
				}
			}
			return txHist;
			
		} catch (IOException e) {
			log.debug(e.toString());
		}
		return null;
	}
	
	/**
	 * Get the latest block height of the blockchain
	 * @return
	 */
	public LatestBlockRespBO getLatestBlockHeight() {
		CasDecryptReponse rawResponse;
		//translate JSON response data into Java Object Types
		LatestBlockRespBO latestBlockBO = new LatestBlockRespBO();
		try {
			rawResponse = _wBaseConn.queryMaxHeight();
			latestBlockBO.setRawMsg(rawResponse.getMsg());
			latestBlockBO.setRawRespCode(rawResponse.getRespCode());
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				return latestBlockBO;
			} else if(newObj.isEmpty()){
				return latestBlockBO;
			}
			latestBlockBO.setRawJSONObj(newObj);
			//changes to the JSON response keys are maintained in the ResponseBO objects, use these keys to retrieve all available fields
			for(LatestBlockResponseEnum respProperty : LatestBlockResponseEnum.values()) {
				latestBlockBO.setAtttribute(respProperty, (String)newObj.getString(respProperty.getRespKey()));
			}
			return latestBlockBO;
		} catch (IOException e) {
			log.error(e.toString());
		}
		return null;
	}
	
	public AsyncRespBO issueToken(IssueTokenReqBO issueTokenBO, GspECKey signKey) {
		
		if(!issueTokenBO.isValidationSuccessful(signKey)) {
			log.debug("request validation failed.");
			return null;
		}
		
		CasDecryptReponse rawResponse;
		String txId = issueTokenBO.get_txId();
		try {
			rawResponse = _bcBaseConn.issueCoin(issueTokenBO.get_origReqObj(), signKey);
			AsyncRespBO asyncResponse = new AsyncRespBO(rawResponse.getRespCode(),txId);
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				//check that the response is successful
				if(asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespMessage())!=0) {
					log.debug("failed response");
				}
			} else if(newObj.isEmpty()){
				//check that the response is successful
				if(asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespMessage())!=0) {
					log.debug("failed response");
				}
			}
			asyncResponse.setRawJsonObject(newObj);
			return asyncResponse;
			
		} catch (IOException e) {
			log.error(e.toString());
		}
		AsyncRespBO failedAsyncResponse = new AsyncRespBO(AsyncRespBO.asyncResponseCodesEnum.FAILED_QUERY.getRespCode(),txId);
		return failedAsyncResponse;
	}
	
	public IssueTokenStatusRespBO getIssueTokenStatus(String txId) {
		CasDecryptReponse rawResponse;
		//translate JSON response data into Java Object Types
		IssueTokenStatusRespBO issueStatusBO = new IssueTokenStatusRespBO();
		try {
			rawResponse = _bcBaseConn.issueQuery(txId);
			issueStatusBO.set_rawMsg(rawResponse.getMsg());
			issueStatusBO.set_rawRespCode(rawResponse.getRespCode());
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				return issueStatusBO;
			} else if(newObj.isEmpty()){
				return issueStatusBO;
			}
			issueStatusBO.setRawJSONObj(newObj);
			//changes to the JSON response keys are maintained in the ResponseBO objects, use these keys to retrieve all available fields
			for(IssueTokenStatusResponseEnum respProperty : IssueTokenStatusResponseEnum.values()) {
				issueStatusBO.setAtttribute(respProperty, (String)newObj.getString(respProperty.getRespKey()));
			}
		return issueStatusBO;
		} catch (IOException e) {
			log.error(e.toString());
		}
		return null;
	}
	
	public AsyncRespBO grantSubscriptionPermission(GrantSubscribePermReqBO subscribePermBO, GspECKey signKey) {
		
		if(!subscribePermBO.isValidationSuccessful(signKey)) {
			log.debug("request validation failed.");
			return null;
		}
		
		String txId = subscribePermBO.get_txId();
		CasDecryptReponse rawResponse;
		try {
			rawResponse = _bcBaseConn.privilege(subscribePermBO.get_origReqObj(), signKey);
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			AsyncRespBO asyncResponse = new AsyncRespBO(rawResponse.getRespCode(),txId);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				//check that the response status is successful
				if(
					!((asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.GRANT_PERMISSION_SUCCESS.getRespMessage())==0) |
					(asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.GRANT_PERMISSION_VERIFIED_SUCCESS.getRespMessage())==0)
				)) {
					log.debug("returned response is not successful.");
				}
			} else if(newObj.isEmpty()){
				//check that the response status is successful
				if(
					!((asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.GRANT_PERMISSION_SUCCESS.getRespMessage())==0) |
					(asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.GRANT_PERMISSION_VERIFIED_SUCCESS.getRespMessage())==0)
				)) {
					log.debug("returned response is not successful.");
				}
			}
			asyncResponse.setRawJsonObject(newObj);
			return asyncResponse;
		} catch (IOException e) {
			log.error(e.toString());
		}
		AsyncRespBO failedAsyncResponse = new AsyncRespBO(AsyncRespBO.asyncResponseCodesEnum.FAILED_QUERY.getRespCode(),txId);
		return failedAsyncResponse;
	}
	
	public GrantSubscribePermStatusRespBO getGrantSubscriptionPermissionStatus(String txId) {
		CasDecryptReponse rawResponse;
		//translate JSON response data into Java Object Types
		GrantSubscribePermStatusRespBO grantStatusBO = new GrantSubscribePermStatusRespBO();
		try {
			rawResponse = _bcBaseConn.privilegeQuery(txId);
			grantStatusBO.setRawMsg(rawResponse.getMsg());
			grantStatusBO.setRawRespCode(rawResponse.getRespCode());
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				return grantStatusBO;
			} else if(newObj.isEmpty()) {
				return grantStatusBO;
			}
			grantStatusBO.setRawJSONObj(newObj);
			//changes to the JSON response keys are maintained in the ResponseBO objects, use these keys to retrieve all available fields
			for(GrantSubscribePermStatusResponseEnum respProperty : GrantSubscribePermStatusResponseEnum.values()) {
				grantStatusBO.setAtttribute(respProperty, (String)newObj.getString(respProperty.getRespKey()));
			}
			return grantStatusBO;
		} catch (IOException e) {
			log.error(e.toString());
		}
		return null;
	}
	
	public AsyncRespBO subscribeToToken(SubscribeReqBO subscribeBO, GspECKey signKey) {
		if(!subscribeBO.isValidationSuccessful(signKey)) {
			log.debug("request validation failed.");
			return null;
		}

		String txId = subscribeBO.get_txId();
		CasDecryptReponse rawResponse;
		try {
			rawResponse = _bcBaseConn.subscribe(subscribeBO.get_origReqObj(), signKey);
			AsyncRespBO asyncResponse = new AsyncRespBO(rawResponse.getRespCode(),txId);
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				//check that the response is successful
				if(asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespMessage())!=0) {
					log.debug("failed response");
				} 
			} else if(newObj.isEmpty()) {
				//check that the response is successful
				if(asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespMessage())!=0) {
					log.debug("failed response");
				} 
			}
			asyncResponse.setRawJsonObject(newObj);
			return asyncResponse;
		} catch (IOException e) {
			log.error(e.toString());
		}
		AsyncRespBO failedAsyncResponse = new AsyncRespBO(AsyncRespBO.asyncResponseCodesEnum.FAILED_QUERY.getRespCode(),txId);
		return failedAsyncResponse;
	}
	
	public SubscribeStatusRespBO getSubscriptionStatus(String txId) {
		CasDecryptReponse rawResponse;
		//translate JSON response data into Java Object Types
		SubscribeStatusRespBO subscribeStatusBO = new SubscribeStatusRespBO();
		try {
			rawResponse = _bcBaseConn.subscribeQuery(txId);
			subscribeStatusBO.setRawMsg(rawResponse.getMsg());
			subscribeStatusBO.setRawRespCode(rawResponse.getRespCode());
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				return subscribeStatusBO;
			} else if(newObj.isEmpty()) {
				return subscribeStatusBO;
			}
			subscribeStatusBO.setRawJSONObj(newObj);
			
			//changes to the JSON response keys are maintained in the ResponseBO objects, use these keys to retrieve all available fields
			for(SubscribeStatusResponseEnum respProperty : SubscribeStatusResponseEnum.values()) {
				subscribeStatusBO.setAtttribute(respProperty, (String)newObj.getString(respProperty.getRespKey()));
			}
			return subscribeStatusBO;
		} catch (IOException e) {
			log.error(e.toString());		
		}
		return null;
	}
	
	public AsyncRespBO generatePaymentRecord(GeneratePaymentRecordReqBO paymentRecordBO,GspECKey signKey) {
		if(!paymentRecordBO.isValidationSuccessful(signKey)) {
			log.debug("request validation failed.");
			return null;
		}
		
		String txId = paymentRecordBO.getTxId();
		CasDecryptReponse rawResponse;
		try {
			rawResponse = _bcBaseConn.settlRequest(txId);
			AsyncRespBO asyncResponse = new AsyncRespBO(rawResponse.getRespCode(),txId);
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				if(asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespMessage())!=0) {
					log.debug("failed response");
				}
			}else if(newObj.isEmpty()) {
				if(asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespMessage())!=0) {
					log.debug("failed response");
				}
			}
			asyncResponse.setRawJsonObject(newObj);
			return asyncResponse;
		} catch (IOException e) {
			log.error(e.toString());
		}
		AsyncRespBO failedAsyncResponse = new AsyncRespBO(AsyncRespBO.asyncResponseCodesEnum.FAILED_QUERY.getRespCode(),txId);
		return failedAsyncResponse;
	}
	
	public GeneratePaymentRecordRespBO getPaymentRecordStatus(String txId) {
		CasDecryptReponse rawResponse;
		//translate JSON response data into Java Object Types
		GeneratePaymentRecordRespBO paymentRecordStatusBO = new GeneratePaymentRecordRespBO();
		try {
			rawResponse = _bcBaseConn.settlRequestQuery(txId);
			paymentRecordStatusBO.set_rawMsg(rawResponse.getMsg());
			paymentRecordStatusBO.set_rawRespCode(rawResponse.getRespCode());
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				return paymentRecordStatusBO;
			}else if(newObj.isEmpty()) {
				return paymentRecordStatusBO;
			}
			paymentRecordStatusBO.setRawJSONObj(newObj);
			//changes to the JSON response keys are maintained in the ResponseBO objects, use these keys to retrieve all available fields
			for(PaymentRecordStatusResponseEnum respProperty : PaymentRecordStatusResponseEnum.values()) {
				paymentRecordStatusBO.setAtttribute(respProperty, (String)newObj.getString(respProperty.getRespKey()));
			}return paymentRecordStatusBO;
		} catch (IOException e) {
			log.error(e.toString());
		}
		return null;
	}
	
	public AsyncRespBO distributePayment(DistributePaymentReqBO paymentBO, GspECKey signKey) {
		if(!paymentBO.isValidationSuccessful(signKey)) {
			log.debug("request validation failed.");
			return null;
		}
		
		String txId = paymentBO.get_txId();
		CasDecryptReponse rawResponse;
		try {
			rawResponse = _bcBaseConn.settlInterest(paymentBO.get_origReqObj(), signKey);
			AsyncRespBO asyncResponse = new AsyncRespBO(rawResponse.getRespCode(),txId);
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				//check that the response is successful
				if(asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespMessage())!=0) {
					log.debug("failed response");
				} 
			} else if(newObj.isEmpty()) {
				//check that the response is successful
				if(asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespMessage())!=0) {
					log.debug("failed response");
				} 
			}
			asyncResponse.setRawJsonObject(newObj);
			return asyncResponse;
		} catch (IOException e) {
			log.error(e.toString());
		}
		AsyncRespBO failedAsyncResponse = new AsyncRespBO(AsyncRespBO.asyncResponseCodesEnum.FAILED_QUERY.getRespCode(),txId);
		return failedAsyncResponse;
	}
	
	public DistributePaymentStatusRespBO getDistributePaymentStatus(String txId) {
		CasDecryptReponse rawResponse;
		//translate JSON response data into Java Object Types
		DistributePaymentStatusRespBO distributePaymentStatusBO = new DistributePaymentStatusRespBO();
		try {
			rawResponse = _bcBaseConn.settlInterestQuery(txId);
			distributePaymentStatusBO.setRawMsg(rawResponse.getMsg());
			distributePaymentStatusBO.setRawRespCode(rawResponse.getRespCode());
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				return distributePaymentStatusBO;
			} else if(newObj.isEmpty()) {
				return distributePaymentStatusBO;
			}
			distributePaymentStatusBO.setRawJSONObj(newObj);
			//changes to the JSON response keys are maintained in the ResponseBO objects, use these keys to retrieve all available fields
			for(DistributePaymentStatusResponseEnum respProperty : DistributePaymentStatusResponseEnum.values()) {
				distributePaymentStatusBO.setAtttribute(respProperty, (String)newObj.getString(respProperty.getRespKey()));
			}return distributePaymentStatusBO;
		} catch (IOException e) {
			log.error(e.toString());
		}
		return null;
	}
	
	public AsyncRespBO freezeOrUnfreezeWallet(FreezeWalletReqBO freezeBO, GspECKey freezeKey) {
		if(!freezeBO.isValidationSuccessful(freezeKey)) {
			log.debug("request validation failed.");
			return null;
		}
		
		String txId = freezeBO.get_txId();
		CasDecryptReponse rawResponse;
		try {
			rawResponse = _bcBaseConn.frozeAccount(freezeBO.get_origReqObj(), freezeKey);
			AsyncRespBO asyncResponse = new AsyncRespBO(rawResponse.getRespCode(),txId);
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				//check that the response is successful
				if(asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespMessage())!=0) {
					log.debug("failed response");
				} 
			}else if(newObj.isEmpty()) {
				//check that the response is successful
				if(asyncResponse.getAsyncRespMsg().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespMessage())!=0) {
					log.debug("failed response");
				} 
			}
			asyncResponse.setRawJsonObject(newObj);
			return asyncResponse;
		} catch (IOException e) {
			log.error(e.toString());
		}
		AsyncRespBO failedAsyncResponse = new AsyncRespBO(AsyncRespBO.asyncResponseCodesEnum.FAILED_QUERY.getRespCode(),txId);
		return failedAsyncResponse;
	}
	
	public FreezeOrUnfreezeStatusRespBO getFreezeOrUnfreezeWalletStatus(String txId) {
		CasDecryptReponse rawResponse;
		//translate JSON response data into Java Object Types
		FreezeOrUnfreezeStatusRespBO freezeOrUnfreezeStatusBO = new FreezeOrUnfreezeStatusRespBO();
		try {
			rawResponse = _bcBaseConn.frozeQuery(txId);
			freezeOrUnfreezeStatusBO.setRawMsg(rawResponse.getMsg());
			freezeOrUnfreezeStatusBO.setRawRespCode(rawResponse.getRespCode());
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				return freezeOrUnfreezeStatusBO;
			} else if(newObj.isEmpty()) {
				return freezeOrUnfreezeStatusBO;
			}	
			freezeOrUnfreezeStatusBO.setRawJSONObj(newObj);
			//changes to the JSON response keys are maintained in the ResponseBO objects, use these keys to retrieve all available fields
			for(FreezeOrUnfreezeStatusResponseEnum respProperty : FreezeOrUnfreezeStatusResponseEnum.values()) {
				freezeOrUnfreezeStatusBO.setAtttribute(respProperty, (String)newObj.getString(respProperty.getRespKey()));
			}return freezeOrUnfreezeStatusBO;
		} catch (IOException e) {
			log.error(e.toString());
		}
		return null;
	}
	
}
