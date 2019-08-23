package hashstacs.sdk.response;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.util.StacsResponseEnums.GrantSubscribePermStatusResponseEnum;
import lombok.Setter;

@Setter
public class GrantSubscribePermStatusRespBO extends RespBO {

	private AsyncRespBO.asyncResponseCodesEnum _resultCode;
	private String _resultMsg;
	private String _txId;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public GrantSubscribePermStatusRespBO() {
		
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
	public String getRawRespCode() {
		return _rawRespCode;
	}
	public String getRawMsg() {
		return _rawMsg;
	}
	public JSONObject getRawJsonObject() {
		return _rawJsonObject;
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
		GrantSubscribePermStatusResponseEnum respProperty = (GrantSubscribePermStatusResponseEnum)T;
		switch(respProperty) {
		case RESULT_CODE:
			setResultCode(attrValue);
			break;
		case RESULT_MSG:
			set_resultMsg(attrValue);
			break;
		case TXID:
			set_txId(attrValue);
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
	@Override
	public void setRawJSONObj(Object value) {
		_rawJsonObject = (JSONObject) value;
	}

}
