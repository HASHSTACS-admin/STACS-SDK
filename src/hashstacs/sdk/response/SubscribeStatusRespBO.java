package hashstacs.sdk.response;

import hashstacs.sdk.util.StacsResponseEnums.SubscribeStatusResponseEnum;
import lombok.Setter;

@Setter
public class SubscribeStatusRespBO extends RespBO {
	
	private String _blockHash;
	private AsyncRespBO.asyncResponseCodesEnum _resultCode;
	private String _resultMsg;
	private String _outOrderSourceCode;
	private String _txId;
	private String _status;
	
	private String _rawRespCode;
	private String _rawMsg;
	
	public SubscribeStatusRespBO() {
		
	}
	
	public String getBlockHash() {
		return _blockHash;
	}
	
	public AsyncRespBO.asyncResponseCodesEnum getResultCode() {
		return _resultCode;
	}
	
	public String getResultMsg() {
		return _resultMsg;
	}
	public String getTxId() {
		return _txId;
	}
	public String getStatus() {
		return _status;
	}
	public String getRawRespCode() {
		return _rawRespCode;
	}
	public String getRawMsg() {
		return _rawMsg;
	}
	
	
	private void setResultCode(String value) {
		for(AsyncRespBO.asyncResponseCodesEnum code : AsyncRespBO.asyncResponseCodesEnum.values()) {
			if(value.compareTo(code.getRespCode())==0) {
				_resultCode=code;
			}
		}
	}
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		SubscribeStatusResponseEnum respProperty = (SubscribeStatusResponseEnum)T;
		switch(respProperty) {
		case BLOCK_HASH:
			set_blockHash(attrValue);
			break;
		case RESULT_CODE:
			setResultCode(attrValue);
			break;
		case RESULT_MSG:
			set_resultMsg(attrValue);
			break;
		case OUT_ORDER_SOURCE_CODE:
			set_outOrderSourceCode(attrValue);
			break;
		case TXID:
			set_txId(attrValue);
			break;
		case STATUS:
			set_status(attrValue);
			break;
		}
	}

	@Override
	public void setRawRespCode(String value) {
		_rawRespCode=value;
	}

	@Override
	public void setRawMsg(String value) {
		_rawMsg=value;
	}

}
