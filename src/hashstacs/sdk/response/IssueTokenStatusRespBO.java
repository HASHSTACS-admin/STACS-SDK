package hashstacs.sdk.response;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.util.StacsResponseEnums.IssueTokenStatusResponseEnum;
import lombok.Setter;

@Setter
public class IssueTokenStatusRespBO extends RespBO {
	
	private String _smartContractAddr;
	private String _tokenCode;
	private AsyncRespBO.asyncResponseCodesEnum _resultCode;
	private String _resultMsg;
	private String _status;
	private String _txId;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public IssueTokenStatusRespBO() {
		
	}
	public String getSmartContractAddress() {
		return _smartContractAddr;
	}
	public String getTokenCode() {
		return _tokenCode;
	}
	public AsyncRespBO.asyncResponseCodesEnum getResultCode() {
		return _resultCode;
	}
	public String getResultMsg() {
		return _resultMsg;
	}
	public String getStatus() {
		return _status;
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
		IssueTokenStatusResponseEnum respProperty = (IssueTokenStatusResponseEnum)T;
		switch(respProperty) {
		case SMART_CONTRACT_ADDRESS:
			set_smartContractAddr(attrValue);
			break;
		case TOKEN_CODE:
			set_tokenCode(attrValue);
			break;
		case RESULT_CODE:
			setResultCode(attrValue);
			break;
		case RESULT_MSG:
			set_resultMsg(attrValue);
			break;
		case STATUS:
			set_status(attrValue);
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
