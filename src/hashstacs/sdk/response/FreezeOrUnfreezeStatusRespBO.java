package hashstacs.sdk.response;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.util.StacsResponseEnums.FreezeOrUnfreezeStatusResponseEnum;
import lombok.Setter;

@Setter
public class FreezeOrUnfreezeStatusRespBO extends RespBO {

	private String _txId;
	private AsyncRespBO.asyncResponseCodesEnum _resultCode;
	private String _message;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public FreezeOrUnfreezeStatusRespBO() {
		
	}
	public String getTxId() {
		return _txId;
	}
	public AsyncRespBO.asyncResponseCodesEnum getResultCode() {
		return _resultCode;
	}
	public String getMessage() {
		return _message;
	}
	public String getRawRespCode() {
		return _rawRespCode;
	}
	public String getRawMsg() {
		return _rawMsg;
	}
	public JSONObject getRawJsonObject() {
		return _rawJsonObject;
	}
	
	public void setResultCode(String value) {
		for(AsyncRespBO.asyncResponseCodesEnum code : AsyncRespBO.asyncResponseCodesEnum.values()) {
			if(value.compareTo(code.getRespCode())==0) {
				_resultCode=code;
			}
		}
	}
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		FreezeOrUnfreezeStatusResponseEnum respProperty = (FreezeOrUnfreezeStatusResponseEnum)T;
		switch(respProperty) {
		case RESULT_CODE:
			setResultCode(attrValue);
			break;
		case MESSAGE:
			set_message(attrValue);
			break;
		case TXID:
			set_txId(attrValue);
			break;
		}
	}

	public void setRawRespCode(String value) {
		_rawRespCode = value;
	}
	public void setRawMsg(String value) {
		_rawMsg=value;
	}
	@Override
	public void setRawJSONObj(Object value) {
		_rawJsonObject = (JSONObject) value;
	}
}
