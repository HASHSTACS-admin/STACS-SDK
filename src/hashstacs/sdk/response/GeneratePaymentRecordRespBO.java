package hashstacs.sdk.response;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.util.StacsResponseEnums.PaymentRecordStatusResponseEnum;
import lombok.Setter;

@Setter
public class GeneratePaymentRecordRespBO extends RespBO {

	private Integer _blockHeight;
	private String _txId;
	private String _status;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public GeneratePaymentRecordRespBO() {
		
	}
	public Integer getBlockHeight() {
		return _blockHeight;
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
	public JSONObject getRawJsonObject() {
		return _rawJsonObject;
	}
	
	private void setBlockHeight(String value) {
		if(value!=null) {
			_blockHeight = Integer.parseInt(value);
		}
	}
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		PaymentRecordStatusResponseEnum respProperty = (PaymentRecordStatusResponseEnum)T;
		switch(respProperty) {
		case BLOCK_HEIGHT:
			setBlockHeight(attrValue);
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
	@Override
	public void setRawJSONObj(Object value) {
		_rawJsonObject = (JSONObject) value;
	}

}
