package hashstacs.sdk.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hashstacs.client.bo.BuybackBO;
import com.hashstacs.client.bo.BuybackDetailBO;
import com.hashstacs.sdk.crypto.GspECKey;

import hashstacs.sdk.response.txtype.TokenHoldersTypeBO;
import hashstacs.sdk.util.TokenUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class RedeemTokenReqBO extends ReqBO {
	private final static String TXID_GENERATOR = "BuyBack_id_";
	private String _txId;
	
	//this is the address that issued and held the actual tokens and where the payout will be from
	private String _redemptionAndPaymentAddress; 
	
	private TokenUnit _totalPaymentForRedemption;//payCurrency + totalPayAmount
	private TokenUnit _totalRedeemedTokens;//tokenSymbol + totalAmount
	
	private TokenUnit _paymentPerToken;
	private String _tokenToBeRedeemed;
	
	private Integer _numOfRecipients;
	
	private String _paymentRecordId;
	
	private List<BuybackDetailBO> _recipientDetails = new ArrayList<>();
	
	private BuybackBO _origReqObj = new BuybackBO();
	
	public RedeemTokenReqBO(String issuerAddress, String paymentRecordId, TokenUnit paymentPerToken, String tokenToBeRedeemed) {
		_paymentRecordId = paymentRecordId;
		_origReqObj.setSnapshotId(paymentRecordId);
		
		_redemptionAndPaymentAddress=issuerAddress;
		_origReqObj.setBuybackAddr(issuerAddress);
		_origReqObj.setPayAddr(issuerAddress);
		
		_paymentPerToken=paymentPerToken;
		_origReqObj.setPayCurrency(_paymentPerToken.get_currency());
		
		_tokenToBeRedeemed=tokenToBeRedeemed;
		_origReqObj.setTokenSymbol(tokenToBeRedeemed);
	}
	
	public void addRecipients(List<TokenHoldersTypeBO> allHolders) {	
		for(TokenHoldersTypeBO holder : allHolders) {
			//ignore the issuer wallet address
			if(holder.get_walletAddress().compareTo(_redemptionAndPaymentAddress)==0) {
				continue;
			} else {
				BuybackDetailBO recipient = new BuybackDetailBO();
				recipient.setAddress(holder.get_walletAddress());
				recipient.setAmount(holder.get_amount());
				
				BigDecimal tokenQty = new BigDecimal(holder.get_amount());
				BigDecimal totalPayment = tokenQty.multiply(_paymentPerToken.get_amount());
				recipient.setPayAmount(totalPayment.toString());
				_recipientDetails.add(recipient);
			}	
		}
		_origReqObj.setDetails(_recipientDetails);
	}

	@Override
	public Boolean isValidationSuccessful(GspECKey ecKey) {
		if(!TokenToRedeemNotEqualsPaymentCurrency()) {
			return false;
		} 
		else if(!hasRedemptionRecipients()) {
			return false;
		}
		getTotalNumOfRecipientsAndPayableAmount();
		generateTxId();
		return true;
	}
	
	private Boolean TokenToRedeemNotEqualsPaymentCurrency() {
		if(_tokenToBeRedeemed.compareTo(_paymentPerToken.get_currency())==0) {
			return false;
		}
		return true;
	}
	
	private Boolean hasRedemptionRecipients() {
		if(_recipientDetails.isEmpty()) {
			return false;
		}
		else {
			return true;
		}
	}
	
	
	private void getTotalNumOfRecipientsAndPayableAmount() {
		int numOfRecipients =0;
		BigDecimal totalPaymentAmount = new BigDecimal("0");
		BigDecimal totalTokenQty = new BigDecimal("0");
		
		for(BuybackDetailBO recipient : _recipientDetails) {
			numOfRecipients++;
			BigDecimal recipientAmount = new BigDecimal(recipient.getPayAmount()); 
			totalPaymentAmount = totalPaymentAmount.add(recipientAmount);
			BigDecimal recipientTokens = new BigDecimal(recipient.getAmount());
			totalTokenQty = totalTokenQty.add(recipientTokens);
		}
		_numOfRecipients=numOfRecipients;
		_origReqObj.setSize(_numOfRecipients);
		
		_totalPaymentForRedemption = new TokenUnit(_paymentPerToken.get_currency(),totalPaymentAmount);
		_origReqObj.setTotalPayAmount(totalPaymentAmount.toString());
		
		_totalRedeemedTokens = new TokenUnit(_tokenToBeRedeemed,totalTokenQty);
		_origReqObj.setTotalAmount(totalTokenQty.toString());
	}
	
	
	private void generateTxId() {
		_txId = GspECKey.generate64TxId(TXID_GENERATOR + System.currentTimeMillis());
		//_origReqObj.setCallbackUrl("http://payment_record_/" + _paymentRecordId + "/redemption_/" + _txId);
		_origReqObj.setCallbackUrl("callback_url");
		_origReqObj.setTxId(_txId);
	}
	

}
