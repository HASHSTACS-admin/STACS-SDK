package hashstacs.sdk.response;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.util.TokenUnit;
import hashstacs.sdk.util.StacsResponseEnums.WalletBalanceResponseEnum;

public class WalletBalanceRespBO extends RespBO {

	private BigDecimal _totalAmount;
	private BigDecimal _freezeAmount;
	private BigDecimal _availableAmount;
	private String _currency;
	
	private String _address;
	private TokenUnit _tokenTotalSupply;
	private TokenUnit _tokenFrozenAmount;
	private TokenUnit _tokenAvailableAmount;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public WalletBalanceRespBO() {
		
	}
	
	public TokenUnit getAvailableSubscriptionAmount() {
		return _tokenAvailableAmount;
	}
	public TokenUnit getFrozenAmount() {
		return _tokenFrozenAmount;
	}
	public TokenUnit getTotalSupply() {
		return _tokenTotalSupply;
	}
	public String getWalletAddress() {
		return _address;
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
	
	/**
	 * Custom Setter Logic
	 * All incoming parameters are String types, conversion to be done here
	 * 
	 * New addition to the returned Response data fields can be maintained here
	 * 
	 */
	
	private void setTotalAmount(String value) {
		if(value!=null) {
			_totalAmount = new BigDecimal(value);
		}
	}
	private void setAddress(String value) {
		_address = value;
	}
	private void setFreezeAmount(String value) {
		if(value!=null) {
			_freezeAmount = new BigDecimal(value);
		}
	}
	private void setCurrency(String value) {
		_currency = value;
	}
	
	private void setTokenTotalSupply() {
		_tokenTotalSupply = new TokenUnit(_currency,_totalAmount);
	}
	private void setTokenFrozenAmount() {
		_tokenFrozenAmount = new TokenUnit(_currency,_freezeAmount);
	}
	private void setTokenAvailableAmount() {
		_availableAmount = _totalAmount.subtract(_freezeAmount);
		_tokenAvailableAmount = new TokenUnit(_currency,_availableAmount);
	}
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		WalletBalanceResponseEnum respProperty = (WalletBalanceResponseEnum)T;
		switch(respProperty) {
		case TOTAL_AMOUNT:
			setTotalAmount(attrValue);
			break;
		case ADDRESS:
			setAddress(attrValue);
			break;
		case FREEZE_AMOUNT:
			setFreezeAmount(attrValue);
			break;
		case CURRENCY:
			setCurrency(attrValue);
			break;
		}
		if(_currency!=null && _totalAmount!=null && _freezeAmount!=null) {
			setTokenTotalSupply();
			setTokenFrozenAmount();
			setTokenAvailableAmount();
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
