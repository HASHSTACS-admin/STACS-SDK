package hashstacs.sdk.request;

import com.hashstacs.client.bo.StoPrivilegeBO;
import com.hashstacs.sdk.crypto.GspECKey;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class GrantSubscribePermReqBO extends ReqBO {
	
	private String _tokenCode;
	private String _tokenSponsorAddress;
	private String _subscriberWalletAddress;
	private String _txId;
	
	private StoPrivilegeBO _origReqObj;
	
	public GrantSubscribePermReqBO() {
		_origReqObj = new StoPrivilegeBO();
		_origReqObj.setCallBackUrl("http://callback:7070");
	}
	
	public void setTokenCode(String value) {
		_tokenCode=value;
		_origReqObj.setStoCode(value);
	}
	
	public void setTokenSponsorAddress(String value) {
		_tokenSponsorAddress=value;
		_origReqObj.setFromAddress(value);
	}
	
	public void setSubscriberWalletAddress(String value) {
		_subscriberWalletAddress=value;
		_origReqObj.setAddress(value);
	}
	
	private String generateTxId() {
		_txId=GspECKey.generate64TxId(_origReqObj.getSignMessage() + System.currentTimeMillis());
		_origReqObj.setTxId(_txId);
		return _txId;
	}

	@Override
	public Boolean isValidationSuccessful(GspECKey ecKey) {
		//If the token sponsor address is invalid
		if(!verifyPermission(ecKey)) {
			return false;
		}
		generateTxId();
		return true;
	}
	
	/**
	 * verifies that the token sponsor has the authority to grant permission
	 * @param ecKey
	 * @return
	 */
	private Boolean verifyPermission(GspECKey ecKey) {
		if(ecKey.getHexAddress().compareTo(_tokenSponsorAddress)==0) {
			return true;
		}
		log.debug("verifyPermission failed.");
		return false;
	}
	
}
