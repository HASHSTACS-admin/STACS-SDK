package hashstacs.sdk.wallet;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hashstacs.client.WalletClient;
import com.hashstacs.client.bo.TxIdBO;
import com.hashstacs.sdk.crypto.GspECKey;
import com.hashstacs.sdk.wallet.dock.bo.CasDecryptReponse;

import hashstacs.sdk.request.TransferTokenReqBO;
import hashstacs.sdk.request.TxHistoryReqBO;
import hashstacs.sdk.request.WalletBalanceReqBO;
import hashstacs.sdk.response.AsyncRespBO;
import hashstacs.sdk.response.TransferTokenStatusRespBO;
import hashstacs.sdk.response.WalletBalanceRespBO;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.StacsResponseEnums.TransferTokenTypeBOEnum;
import hashstacs.sdk.util.StacsResponseEnums.WalletBalanceResponseEnum;
import hashstacs.sdk.util.StacsResponseEnums.WalletTokenTxHistoryResponseEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WalletConnector {

	private static WalletConnector _wConn = null;
	private static WalletClient _wBaseConn = null;
		
	private WalletConnector(String chainPubKey, String merchantPriKey, String aesKey, String merchantId, String gatewayUrl) {
		_wBaseConn = new WalletClient(chainPubKey,merchantPriKey,aesKey,merchantId,gatewayUrl);
	}
	
	public static WalletConnector getConn() {
		return _wConn;
	}
	
	public static WalletConnector initConn(String chainPubKey, String merchantPriKey, String aesKey, String merchantId, String gatewayUrl) {
		if(_wConn==null) {
			_wConn = new WalletConnector(chainPubKey, merchantPriKey, aesKey, merchantId, gatewayUrl);
		}
		return _wConn;
	}
	
	/**
	 * transfer tokens between 2 wallets
	 * @param transactionInfoBO
	 * @param signKey
	 * @return
	 */
	public AsyncRespBO transferToken(TransferTokenReqBO transferTokenBO, GspECKey signKey) {
		
		if(!transferTokenBO.isValidationSuccessful(signKey)) {
			log.debug("request validation failed.");
			return null;
		}
		
		CasDecryptReponse rawResponse;
		String txId = transferTokenBO.get_txId();
	
		try {
			rawResponse = _wBaseConn.transfer(transferTokenBO.get_origReqObj(), signKey);
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
			return asyncResponse;
		} catch (IOException e) {
			log.error(e.toString());
		}
		AsyncRespBO failedAsyncResponse = new AsyncRespBO(AsyncRespBO.asyncResponseCodesEnum.FAILED_QUERY.getRespCode(),txId);
		return failedAsyncResponse;
	}
	
	/**
	 * query a wallet balance
	 * @param accountBalanceBO
	 * @return
	 */
	public WalletBalanceRespBO getWalletBalance(WalletBalanceReqBO walletBalanceBO) {
		
		CasDecryptReponse rawResponse;
		//translate JSON response data into Java Object Types
		WalletBalanceRespBO walletBalnceRespBO = new WalletBalanceRespBO();
		try {
			rawResponse = _wBaseConn.queryBalance(walletBalanceBO.get_origReqObj());
			walletBalnceRespBO.setRawMsg(rawResponse.getMsg());
			walletBalnceRespBO.setRawRespCode(rawResponse.getRespCode());
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				return walletBalnceRespBO;
			} else if(newObj.isEmpty()) {
				return walletBalnceRespBO;
			}
			//changes to the JSON response keys are maintained in the ResponseBO objects, use these keys to retrieve all available fields
			for(WalletBalanceResponseEnum respProperty : WalletBalanceResponseEnum.values()) {
				walletBalnceRespBO.setAtttribute(respProperty, (String)newObj.getString(respProperty.getRespKey()));
			}
			return walletBalnceRespBO;	
		} catch (IOException e) {
			log.error(e.toString());
		}
		return null;
	}
	
	/**
	 * get the status of a Transfer Token Transaction Request
	 * Transfer Token Transaction Request is an asynchronous request, use this method to query the 
	 * latest status of the request
	 * @param txId
	 * @return
	 */
	public TransferTokenStatusRespBO getTransferTokenStatus(String txId) {
		CasDecryptReponse rawResponse;
		//translate JSON response data into Java Object Types
		TransferTokenStatusRespBO transferTokenStatusBO = new TransferTokenStatusRespBO();
		try {
			TxIdBO txIdBO = new TxIdBO(txId);
			rawResponse = _wBaseConn.queryTxByTxId(txIdBO);
			transferTokenStatusBO.setRawMsg(rawResponse.getMsg());
			transferTokenStatusBO.setRawRespCode(rawResponse.getRespCode());
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				return transferTokenStatusBO;
			} else if(newObj.isEmpty()) {
				return transferTokenStatusBO;
			}
			//changes to the JSON response keys are maintained in the ResponseBO objects, use these keys to retrieve all available fields
			for(TransferTokenTypeBOEnum respProperty : TransferTokenTypeBOEnum.values()) {
				transferTokenStatusBO.setAtttribute(respProperty, (String)newObj.getString(respProperty.getRespKey()));
			}
			return transferTokenStatusBO;
		} catch (IOException e) {
			log.error(e.toString());
		}
		return null;
	}
	
	/**
	 * Get transaction history for either:
	 * 1) a wallet address
	 * 2) a currency symbol (stable coin or digital asset)
	 * 3) both of the above
	 * 
	 * @param walletQueryBO
	 * @return
	 */
	public TxHistoryRecord getTxHistoryForWalletAndOrCurrency(TxHistoryReqBO txHistBO) {
		TxHistoryRecord txHist = new TxHistoryRecord();
		CasDecryptReponse rawResponse;
		try {
			rawResponse = _wBaseConn.queryTxsByAddrCurrency(txHistBO.get_origReqObj());
			txHist.setRawMsg(rawResponse.getMsg());
			txHist.setRawRespCode(rawResponse.getRespCode());
			
			JSONObject newObj = StacsUtil.getRespObject(rawResponse);
			//ensure JSON object is not null or empty
			if(newObj==null) {
				return txHist;
			} else if(newObj.isEmpty()) {
				return txHist;
			}
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
			log.error(e.toString());
		}
		return null;
	}
	
}
