package hashstacs.sdk.request;

import com.hashstacs.client.bo.BuybackFrozenBO;
import com.hashstacs.sdk.crypto.GspECKey;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class FreezeTokensForRedemptionReqBO extends ReqBO {
	private String _sponsorAddress;
	private String _tokenCode;
	private String _txId;
	
	private BuybackFrozenBO _origReqObj = new BuybackFrozenBO();
	
	public FreezeTokensForRedemptionReqBO(String sponsorAddress, String tokenCode) {
		_sponsorAddress=sponsorAddress;
		_tokenCode=tokenCode;
		
		_origReqObj.setIssueAddress(sponsorAddress);
		_origReqObj.setTokenSymbol(tokenCode);
	}
	
	@Override
	public Boolean isValidationSuccessful(GspECKey ecKey) {
		if(doesSponsorAddressBelongToSponsorKey(ecKey)) {
			generateTxId();
			return true;
		}
		return false;
	}
	
	private Boolean doesSponsorAddressBelongToSponsorKey(GspECKey sponsorKey) {
		if(_sponsorAddress == null) {
			return false;
		}
		else if(_sponsorAddress.isEmpty()) {
			return false;
		}
		else if(sponsorKey.getHexAddress().compareTo(_sponsorAddress)!=0) {
			return false;
		}
		return true;
	}
	
	private void generateTxId() {
		_txId = GspECKey.generate64TxId("buybackFrozen_id_" + System.currentTimeMillis());
		_origReqObj.setTxId(_txId);
	}

}
