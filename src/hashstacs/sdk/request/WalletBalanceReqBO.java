package hashstacs.sdk.request;

import com.hashstacs.client.bo.AccountBalanceBO;
import com.hashstacs.sdk.crypto.GspECKey;

import lombok.Getter;

@Getter
public class WalletBalanceReqBO extends ReqBO {

	private String _walletAddress;
	private String _tokenCode;
	
	private AccountBalanceBO _origReqObj = new AccountBalanceBO();
	
	public WalletBalanceReqBO(String walletAddress, String tokenCode) {
		_walletAddress = walletAddress;
		_origReqObj.setAddress(_walletAddress);
		_tokenCode = tokenCode;
		_origReqObj.setCurrency(_tokenCode);
	}
	
	@Override
	public Boolean isValidationSuccessful(GspECKey ecKey) {
		//TODO to add additional validation checks
		return true;
	}
	
	

}
