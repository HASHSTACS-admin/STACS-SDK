package hashstacs.sdk;

import java.math.BigDecimal;

import hashstacs.sdk.request.DistributePaymentReqBO;
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
	public DistributePaymentsSample(String paymentCurrency, String payerWalletAddress, String tokenCode, String paymentRecordTxId) {
		_distributePaymentRequest = new DistributePaymentReqBO(paymentCurrency,paymentRecordTxId);
		//payerWalletAddress is the sponsor of the issued token
		_distributePaymentRequest.setPayerWalletAddress(payerWalletAddress);
		_distributePaymentRequest.setTokenCode(tokenCode);
		
		//adding payee wallet addresses and the amount due to each of them to the list
		String payee1Addr="76c654f0693ad09ae606b25367dd334ff5af4a56";
		TokenUnit payee1Amount = new TokenUnit("USD", new BigDecimal("10"));
		_distributePaymentRequest.addRecipientDetails(payee1Addr, payee1Amount);
		
		String payee2Addr="5d8c0ffdd402ee0172ff4a9c10f6087dfec1338f";
		TokenUnit payee2Amount = new TokenUnit("USD", new BigDecimal("20"));
		_distributePaymentRequest.addRecipientDetails(payee2Addr, payee2Amount);
	}
	
	public DistributePaymentReqBO getDistributePaymentRequest() {
		return _distributePaymentRequest;
	}
}
