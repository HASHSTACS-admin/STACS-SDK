package hashstacs.sdk.response.txtype;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.response.RespBO;
import hashstacs.sdk.util.StacsResponseEnums.GetTokenHoldersResponseEnum;
import lombok.Setter;

@Setter
public class TokenHoldersTypeBO extends RespBO {

	private String _walletAddress;
	private String _amount;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public TokenHoldersTypeBO() {
		
	}
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		GetTokenHoldersResponseEnum respProperty = (GetTokenHoldersResponseEnum) T;
		switch(respProperty) {
		case WALLET_ADDRESS:
			setWalletAddress(attrValue);
			break;
		case AMOUNT:
			setAmount(attrValue);
		}

	}
	
	public void setWalletAddress(String value) {
		_walletAddress=value;
	}
	public void setAmount(String value) {
		_amount=value;
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
		_rawJsonObject= (JSONObject) value;
	}

}
