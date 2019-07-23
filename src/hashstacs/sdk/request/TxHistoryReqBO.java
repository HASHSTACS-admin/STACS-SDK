package hashstacs.sdk.request;

import com.hashstacs.client.bo.CurrencyQueryBO;
import com.hashstacs.sdk.crypto.GspECKey;

import lombok.Getter;

@Getter
public class TxHistoryReqBO extends ReqBO {
	
	private String _walletAddress;
	private String _tokenCode;
	
	private CurrencyQueryBO _origReqObj = new CurrencyQueryBO();
	
	public TxHistoryReqBO(String walletAddress, String tokenCode) {
		_walletAddress=walletAddress;
		_origReqObj.setAddress(_walletAddress);
		_tokenCode=tokenCode;
		_origReqObj.setCurrency(_tokenCode);
	}
	public TxHistoryReqBO(String walletAddressOrTokenCode, Boolean isWalletAddress) {
		if(isWalletAddress) {
			_walletAddress=walletAddressOrTokenCode;
			_origReqObj.setAddress(_walletAddress);
		} else {
			_tokenCode=walletAddressOrTokenCode;
			_origReqObj.setCurrency(_tokenCode);
		}
	}
	
	@Override
	public Boolean isValidationSuccessful(GspECKey ecKey) {
		// TODO To add additional validation checks
		return true;
	}

}
