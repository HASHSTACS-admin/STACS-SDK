package hashstacs.sdk.request;

import com.hashstacs.sdk.crypto.GspECKey;

public class GeneratePaymentRecordReqBO extends ReqBO {

	private String _txId;
	private final String PAYMENT_RECORD_ATTRIBUTE = "snapshot";//"settlRequest";
	
	public GeneratePaymentRecordReqBO() {
		_txId = GspECKey.generate64TxId(PAYMENT_RECORD_ATTRIBUTE + System.currentTimeMillis());
	}
	
	public String getTxId() {
		return _txId;
	}
	
	@Override
	public Boolean isValidationSuccessful(GspECKey ecKey) {
		// TODO additional validation methods to be added here
		return true;
	}

}
