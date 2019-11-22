package hashstacs.sdk.request;

import com.hashstacs.client.bo.SnapshotQueryRequest;
import com.hashstacs.sdk.crypto.GspECKey;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class GetTokenHoldersReqBO extends ReqBO {
	private String _tokenCode;
	private String _paymentRecordTxId;
	
	private SnapshotQueryRequest _origReqObj; 
	
	public GetTokenHoldersReqBO(String paymentRecordTxId,String tokenCode) {
		_origReqObj = new SnapshotQueryRequest();
		_paymentRecordTxId=paymentRecordTxId;
		_tokenCode=tokenCode;
		_origReqObj.setSnapshotId(_paymentRecordTxId);
		_origReqObj.setTokenSymbol(_tokenCode);
		_origReqObj.setPage(1);
		_origReqObj.setSize(20);
	}

	@Override
	public Boolean isValidationSuccessful(GspECKey ecKey) {
	
		return true;
	}

}
