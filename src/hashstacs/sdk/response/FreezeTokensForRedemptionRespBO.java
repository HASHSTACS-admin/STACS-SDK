package hashstacs.sdk.response;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.util.StacsResponseEnums.FreezeTokensForRedemptionResponseEnum;
import lombok.Setter;

@Setter
public class FreezeTokensForRedemptionRespBO extends RespBO {
	
	private String _txId;
	private String _message;
	private String _blockHeight;
	private String _status;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public FreezeTokensForRedemptionRespBO() {
		
	}
	
	public String getTxId() {
		return _txId;
	}
	
	public String getMessage() {
		return _message;
	}
	
	public String getBlockHeight() {
		return _blockHeight;
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
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		FreezeTokensForRedemptionResponseEnum respProperty = (FreezeTokensForRedemptionResponseEnum)T;
		switch(respProperty) {
		case BLOCK_HEIGHT:
			set_blockHeight(attrValue);
			break;
		case TXID:
			set_txId(attrValue);
			break;
		case STATUS:
			set_status(attrValue);
			break;
		case MESSAGE:
			set_message(attrValue);
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
		_rawJsonObject=(JSONObject) value;
	}

}
