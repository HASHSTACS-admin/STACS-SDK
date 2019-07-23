package hashstacs.sdk.response.txtype;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import hashstacs.sdk.response.RespBO;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.TokenUnit;
import hashstacs.sdk.util.StacsResponseEnums.SubscriptionTypeBOEnum;
import lombok.Setter;

@Setter
public class SubscriptionTypeBO extends RespBO {

	private BigDecimal _tokenAmount;
	private String _tokenCurrency;
	private TokenUnit _token;
	
	private BigDecimal _paymentAmount;
	private String _paymentCurrency;
	private TokenUnit _payment;
	
	private String _subscriberAddress;
	private Integer _blockHeight;
	private Date _createTime;
	private String _msg;
	private String _status;
	private String _txId;
	private String _smartContractType;
	private Date _updateTime;
	
	private Object _tokenInfoVO;
	
	private String _rawRespCode;
	private String _rawMsg;
	
	public SubscriptionTypeBO() {
		
	}
	
	public TokenUnit getTokenSubscriptionDetails() {
		return _token;
	}
	public TokenUnit getPaymentDetails() {
		return _payment;
	}
	public String getSubscriberAddress() {
		return _subscriberAddress;
	}
	public Integer getBlockHeight() {
		return _blockHeight;
	}
	public Date getCreationTime() {
		return _createTime;
	}
	public Date getUpdateTime() {
		return _updateTime;
	}
	public String getMsg() {
		return _msg;
	}
	public String getStatus() {
		return _status;
	}
	public String getTxId() {
		return _txId;
	}
	public String getSmartContractType() {
		return _smartContractType;
	}
	public String getRawRespCode() {
		return _rawRespCode;
	}
	public String getRawMsg() {
		return _rawMsg;
	}
	
	private void setTokenNum(String value) {
		if(value!=null) {
			_tokenAmount = new BigDecimal(value);
		}
	}
	private void setBlockHeight(String value) {
		if(value!=null) {
			_blockHeight = Integer.parseInt(value);
		}
	}
	private void setCreateTime(String value) {
		if(value !=null) {
			//if date is in a long format
			if(value.matches("\\d*")) {
				_createTime = new Date(Long.parseLong(value));
			}
			//otherwise date is in a readable format
			else {
				try {
					_createTime =StacsUtil.getDateFormat().parse(value);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private void setPaymentAmount(String value) {
		if(value!=null) {
			_paymentAmount = new BigDecimal(value);
		}
	}
	private void setUpdateTime(String value) {
		if(value !=null) {
			//if date is in a long format
			if(value.matches("\\d*")) {
				_updateTime = new Date(Long.parseLong(value));
			}
			//otherwise date is in a readable format
			else {
				try {
					_updateTime =StacsUtil.getDateFormat().parse(value);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void setPayment() {
		_payment = new TokenUnit(_paymentCurrency,_paymentAmount);
	}
	private void setToken() {
		_token = new TokenUnit(_tokenCurrency,_tokenAmount);
	}
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		SubscriptionTypeBOEnum respProperty = (SubscriptionTypeBOEnum)T;
		switch(respProperty) {
		case SUBSCRIBER_ADDRESS:
			set_subscriberAddress(attrValue);
			break;
		case TOKEN_NUM:
			setTokenNum(attrValue);
			break;
		case BLOCK_HEIGHT:
			setBlockHeight(attrValue);
			break;
		case CREATE_TIME:
			setCreateTime(attrValue);
			break;
		case CURRENCY:
			set_paymentCurrency(attrValue);
			break;
		case MESSAGE:
			set_msg(attrValue);
			break;
		case PAYMENT_AMOUNT:
			setPaymentAmount(attrValue);
			break;
		case STATUS:
			set_status(attrValue);
			break;
		case TOKEN_CODE:
			set_tokenCurrency(attrValue);
			break;
		case TOKEN_INFO_VO:
			set_tokenInfoVO(attrValue);
			break;
		case TX_ID:
			set_txId(attrValue);
			break;
		case SMART_CONTRACT_TYPE:
			set_smartContractType(attrValue);
			break;
		case UPDATE_TIME:
			setUpdateTime(attrValue);
			break;
		}
		if(_tokenCurrency!=null && _tokenAmount!=null) {
			setToken();
		}
		if(_paymentCurrency!=null && _paymentAmount!=null) {
			setPayment();
		}
		
	}
	@Override
	public void setRawRespCode(String value) {
		_rawRespCode = value;
	}
	@Override
	public void setRawMsg(String value) {
		_rawMsg = value;
	}
}
