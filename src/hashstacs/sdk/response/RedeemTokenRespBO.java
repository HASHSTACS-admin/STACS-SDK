package hashstacs.sdk.response;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.util.StacsResponseEnums.RedeemTokenResponseEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RedeemTokenRespBO extends RespBO {

	private String _errorMessage;
	private String _totalTokenQty;
	private String _txId;
	private String _totalPaymentAmount;
	private String _blockHeight;
	private String _status;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public RedeemTokenRespBO() {
		
	}
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		RedeemTokenResponseEnum respProperty = (RedeemTokenResponseEnum) T;
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
		case ERROR_MESSAGE:
			set_errorMessage(attrValue);
			break;
		case TOTAL_TOKEN_QTY:
			set_totalTokenQty(attrValue);
			break;
		case TOTAL_PAYMENT_AMOUNT:
			set_totalPaymentAmount(attrValue);
			break;
		}
	}

	@Override
	public void setRawRespCode(String value) {
		_rawRespCode =value;
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
