package hashstacs.sdk.response;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.util.StacsResponseEnums.DistributePaymentStatusResponseEnum;
import lombok.Setter;

@Setter
public class DistributePaymentStatusRespBO extends RespBO {

	private Integer _blockHeight;
	private String _msg;
	private String _signature;
	private String _status;
	private BigDecimal _totalAmount;
	private String _txId;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public DistributePaymentStatusRespBO() {
		
	}
	public Integer getBlockHeight() {
		return _blockHeight;
	}
	public String getMsg() {
		return _msg;
	}
	public String getStatus() {
		return _status;
	}
	public BigDecimal getTotalAmount() {
		return _totalAmount;
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
	
	private void setBlockHeight(String value) {
		if(value!=null) {
			_blockHeight = Integer.parseInt(value);
		}
	}
	
	private void setTotalAmount(String value) {
		_totalAmount = new BigDecimal(value);
	}
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		DistributePaymentStatusResponseEnum respProperty = (DistributePaymentStatusResponseEnum)T;
		switch(respProperty) {
		case BLOCK_HEIGHT:
			setBlockHeight(attrValue);
			break;
		case MESSAGE:
			set_msg(attrValue);
			break;
		case SIGNATURE:
			set_signature(attrValue);
			break;
		case TOTAL_AMOUNT:
			setTotalAmount(attrValue);
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
