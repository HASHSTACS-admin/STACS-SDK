package hashstacs.sdk.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hashstacs.client.bo.SettlDetailBO;
import com.hashstacs.client.bo.SettlInterestBO;
import com.hashstacs.sdk.crypto.GspECKey;
import hashstacs.sdk.util.TokenUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class DistributePaymentReqBO extends ReqBO {

	private String _tokenCode;
	private String _payerWalletAddress;
	private String _paymentCurrency;
	private String _txId;
	
	private TokenUnit _totalPayment;
	private Integer _NumOfRecipients;
	private String _paymentRecordTxId;
	
	private List<SettlDetailBO> _paymentDetails;
	
	
	private SettlInterestBO _origReqObj;
	
	public DistributePaymentReqBO(String paymentCurrency,String paymentRecordTxId) {
		_origReqObj = new SettlInterestBO();
		_paymentDetails = new ArrayList<SettlDetailBO>();
		_paymentCurrency = paymentCurrency;
		_origReqObj.setInterestCurrency(_paymentCurrency);
		_paymentRecordTxId = paymentRecordTxId;
	}
	
	public void setTokenCode(String value) {
		_tokenCode = value;
		_origReqObj.setTokenSymbol(_tokenCode);
	}
	
	public void setPayerWalletAddress(String value) {
		_payerWalletAddress=value;
		_origReqObj.setPayAddr(_payerWalletAddress);
	}

	public Boolean addRecipientDetails(String walletAddress, TokenUnit receiveAmount) {
		//verify that the recipient details has the same currency as the payment currency
		if(!verifyCurrencyType(receiveAmount.get_currency())) {
			return false;
		}
		SettlDetailBO newEntry = new SettlDetailBO();
		newEntry.setAddress(walletAddress);
		newEntry.setAmount(receiveAmount.get_amount().toString());
		_paymentDetails.add(newEntry);
		
		return true;
	}
	
	@Override
	public Boolean isValidationSuccessful(GspECKey ecKey) {
		// TODO Additional verification parameters to be added here
		if(!verifyMinPayeeDetails()) {
			return false;
		}
		
		getTotalPayableAmount();
		generateTxId();
		
		return true;
	}
	
	private void generateTxId() {
		_origReqObj.setCallbackUrl("http://payment_record_/" + _paymentRecordTxId + "/distribute_payment_/" + _txId);
		_txId = GspECKey.generate64TxId(_origReqObj.getSignValue());
		_origReqObj.setTxId(_txId);
	}
	
	private void getTotalPayableAmount() {
		BigDecimal totalAmount = new BigDecimal("0");
		int numberOfPayees = 0;
		for(SettlDetailBO payee : _paymentDetails) {
			BigDecimal payeeAmount = new BigDecimal(payee.getAmount()); 
			totalAmount = totalAmount.add(payeeAmount);
			numberOfPayees++;
		}
		_origReqObj.setInterests(_paymentDetails);
		_totalPayment = new TokenUnit(_paymentCurrency,totalAmount);
		_origReqObj.setTotalAmount(totalAmount.toString());
		_NumOfRecipients = numberOfPayees;
		_origReqObj.setSize(_NumOfRecipients);	
	}
	
	private Boolean verifyMinPayeeDetails() {
		if(_paymentDetails.size() < 1) {
			log.debug("verifyMinPayeeDetails failed.");
			return false;
		}
		return true;
	}
	
	private Boolean verifyCurrencyType(String _currency) {
		//if the payment distribution currency was not set, return false
		if(_paymentCurrency == null) {
			log.debug("verifyCurrencyType failed.");
			return false;
		}
		//if the payment distribution currency is different from the input currency, return false
		if(_paymentCurrency.compareTo(_currency)!=0) {
			log.debug("verifyCurrencyType failed.");
			return false;
		}
			
		return true;
	}

}
