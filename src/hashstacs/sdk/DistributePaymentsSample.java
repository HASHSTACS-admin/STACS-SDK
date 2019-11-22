package hashstacs.sdk;

import java.math.BigDecimal;

import hashstacs.sdk.request.DistributePaymentReqBO;
import hashstacs.sdk.response.GetTokenHoldersRespBO;
import hashstacs.sdk.util.TokenUnit;

public class DistributePaymentsSample {
	
	/*The request object to be sent from the SDK to the STACS Native network*/
	private DistributePaymentReqBO _distributePaymentRequest;
	
	/**
	 *  Sample parameters required for issuing a token on a STACS Native network is 
	 * provided here 
	 * @param paymentCurrency
	 * @param payerWalletAddress
	 * @param tokenCode
	 * @param paymentRecordTxId
	 */
	public DistributePaymentsSample(String paymentCurrency, String payerWalletAddress, String tokenCode, String paymentRecordTxId, GetTokenHoldersRespBO tokenHolders) {
		
		TokenUnit paymentPerToken = new TokenUnit(paymentCurrency,new BigDecimal("2"));
		_distributePaymentRequest = new DistributePaymentReqBO(paymentRecordTxId,paymentPerToken);
		//payerWalletAddress is the sponsor of the issued token
		_distributePaymentRequest.setPayerWalletAddress(payerWalletAddress);
		_distributePaymentRequest.setTokenCode(tokenCode);
		
		_distributePaymentRequest.addAllRecipientDetails(tokenHolders.getTokenHolders());
	}
	
	public DistributePaymentReqBO getDistributePaymentRequest() {
		return _distributePaymentRequest;
	}
}
