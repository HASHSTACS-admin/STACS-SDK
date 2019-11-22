package hashstacs.sdk.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hashstacs.client.bo.SettlDetailBO;
import com.hashstacs.client.bo.SettlInterestBO;
import com.hashstacs.sdk.crypto.GspECKey;

import hashstacs.sdk.response.txtype.TokenHoldersTypeBO;
import hashstacs.sdk.util.TokenUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class DistributePaymentReqBO extends ReqBO {

	private String _tokenCode;
	private String _payerWalletAddress;
	private TokenUnit _paymentPerToken;
	private String _txId;
	
	private TokenUnit _totalPayment;
	private Integer _NumOfRecipients;
	private String _paymentRecordTxId;
	
	private List<SettlDetailBO> _paymentDetails;
	
	
	private SettlInterestBO _origReqObj;
	
	public DistributePaymentReqBO(String paymentRecordTxId, TokenUnit paymentPerToken) {
		_origReqObj = new SettlInterestBO();
		_paymentDetails = new ArrayList<SettlDetailBO>();
		_paymentPerToken = paymentPerToken;
		_origReqObj.setInterestCurrency(_paymentPerToken.get_currency());
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
	
	public Boolean addAllRecipientDetails(List<TokenHoldersTypeBO> allRecipients) {
		for(TokenHoldersTypeBO recipient : allRecipients) {
			if(recipient.get_walletAddress().compareTo(_payerWalletAddress)!=0) {
				SettlDetailBO newEntry = new SettlDetailBO();
				newEntry.setAddress(recipient.get_walletAddress());
				//calculate the right amount to be paid out
				BigDecimal numOfTokensHeldByRecipient = new BigDecimal(recipient.get_amount());
				BigDecimal recipientPayout = numOfTokensHeldByRecipient.multiply(_paymentPerToken.get_amount());
				newEntry.setAmount(recipientPayout.toString());
				_paymentDetails.add(newEntry);
			}
		}
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
		_txId = GspECKey.generate64TxId(_origReqObj.toString());
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
		_totalPayment = new TokenUnit(_paymentPerToken.get_currency(),totalAmount);
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

}
