package hashstacs.sdk.request;

import com.hashstacs.client.bo.SubscribeBO;
import com.hashstacs.sdk.crypto.GspECKey;

import hashstacs.sdk.util.TokenUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class SubscribeReqBO extends ReqBO {
	
	private TokenUnit _tokenSubscription;
	private TokenUnit _payment;
	private String _subscribeMsg;
	private String _subscriberWalletAddress;
	private String _txId;
	private SubscribeBO _origReqObj;
	
	public SubscribeReqBO() {
		_origReqObj = new SubscribeBO();
		_origReqObj.setCallBackUrl("http://callback:7073/v1/subscribe/callback");
		_origReqObj.setOutOrderSourceCode("outOrderSourceCodeNotInUse");
	}
	
	public void SetTokenSubscription(TokenUnit token) {
		_tokenSubscription = token;
		_origReqObj.setTokenCode(_tokenSubscription.get_currency());
		_origReqObj.setTokenNum(_tokenSubscription.get_amount().longValue());
	}
	
	public void setPayment(TokenUnit payment) {
		_payment = payment;
		_origReqObj.setCurrency(_payment.get_currency());
		_origReqObj.setTokenAmount(_payment.get_amount().toString());
	}
	
	public void setMessage(String message) {
		_subscribeMsg = message;
		_origReqObj.setMsg(_subscribeMsg);
	}
	public void setSubscriberWalletAddress(String address) {
		_subscriberWalletAddress = address;
		_origReqObj.setAddress(_subscriberWalletAddress);
	}
    
	/**
	 * Additional validations based on issued tokens to be added later
	 * @author Hashstacs Solutions Engineering
	 */
	@Override
	public Boolean isValidationSuccessful(GspECKey ecKey) {
		if(!verifyPermission(ecKey)) {
			return false;
		}
		generateTxId();
		return true;
	}
	
	private Boolean verifyPermission(GspECKey ecKey) {
		if(ecKey.getHexAddress().compareTo(_subscriberWalletAddress)==0) {
			return true;
		}
		log.debug("verifyPermission failed.");
		return false;
	}
	
	private String generateTxId() {
		_txId = GspECKey.generate64TxId(_origReqObj.getSignMessage() + System.currentTimeMillis());
		_origReqObj.setReqId(_txId);
		_origReqObj.setOutOrderNo(_txId);
		return _txId;
	}

}
