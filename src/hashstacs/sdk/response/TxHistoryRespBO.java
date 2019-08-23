package hashstacs.sdk.response;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.wallet.TxHistoryRecord;

public class TxHistoryRespBO extends RespBO {
	
	private TxHistoryRecord _txHistList;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public TxHistoryRespBO(TxHistoryRecord record) {
		_txHistList = record;		
	}
	
	public TxHistoryRecord getTxHistList() {
		return _txHistList;
	}
	public String getRawRespCode() {
		return _rawRespCode;
	}
	public String getRawMessage() {
		return _rawMsg;
	}
	public JSONObject getRawJsonObject() {
		return _rawJsonObject;
	}
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		//handled in the individual txtype BO objects

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
