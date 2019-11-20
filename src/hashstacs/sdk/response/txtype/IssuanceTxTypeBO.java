package hashstacs.sdk.response.txtype;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.response.RespBO;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.TokenUnit;
import hashstacs.sdk.util.StacsResponseEnums.IssuanceTxTypeBOEnum;
import lombok.Setter;

@Setter
public class IssuanceTxTypeBO extends RespBO {

	private BigDecimal _totalTokenSupply;
	private BigDecimal _availableAmountForSubscription;
	private String _tokenCode;
	private TokenUnit _totalIssuedTokens;
	private TokenUnit _availableIssuedTokens;
	
	private String _paymentCurrency;
	private BigDecimal _paymentAmount;
	private BigDecimal _txFeeInPaymentCurrency;
	private TokenUnit _payment;
	private TokenUnit _txFee;	
	
	private Double _maxSubscriptionAmount;
	private Double _minSubscriptionAmount;
	private Date _subscriptionEndDate;
	private Date _subscriptionStartDate;
	
	private String _sponsorAddress;
	private String _sponsor;
	private String _tokenCustodyAddress;
	private String _issuer;
	
	private Integer _blockHeight;
	private Date _createTime;
	private String _bizModel;
	private Object _data;
	private Boolean _isStableCoin;
	private Integer _numOfLockDays;	
	private String _status;
	private Object _tokenInfoVO;
	private String _txId;
	private String _smartContractType;
	private Date _updateTime;
	private String _version;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public IssuanceTxTypeBO() {
		
	}
	public TokenUnit getTotalIssuedSupply() {
		return _totalIssuedTokens;
	}
	public TokenUnit getAvailableIssuedSupply() {
		return _availableIssuedTokens;
	}
	public TokenUnit getTokenUnitPrice() {
		return _payment;
	}
	public TokenUnit getTokenTxFee() {
		return _txFee;
	}
	public Date getSubscriptionStartDate() {
		return _subscriptionStartDate;
	}
	public Date getSubscriptionEndDate() {
		return _subscriptionEndDate;
	}
	public Double getMaxSubscriptionAmount() {
		return _maxSubscriptionAmount;
	}
	public Double getMinSubscriptionAmount() {
		return _minSubscriptionAmount;
	}
	public String getSponsorName() {
		return _sponsor;
	}
	public String getSponsorAddress() {
		return _sponsorAddress;
	}
	public String getIssuerName() {
		return _issuer;
	}
	public String getTokenCustodyAddress() {
		return _tokenCustodyAddress;
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
	public Boolean isStableCoin() {
		return _isStableCoin;
	}
	public Integer getNumOfLockDays() {
		return _numOfLockDays;
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
	private void setTotalTokenSupply(String value) {
		if(value!=null) {
			_totalTokenSupply = new BigDecimal(value);
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
				set_createTime(new Date(Long.parseLong(value)));
			}
			//otherwise date is in a readable format
			else {
				try {
					set_createTime(StacsUtil.getDateFormat().parse(value));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private void setAvailableAmountForSubscription(String value) {
		if(value!=null) {
			_availableAmountForSubscription = new BigDecimal(value);
		}
	}
	private void setSubscriptionEndDate(String value) {
		if(value !=null) {
			//if date is in a long format
			if(value.matches("\\d*")) {
				set_subscriptionEndDate(new Date(Long.parseLong(value)));
			}
			//otherwise date is in a readable format
			else {
				try {
					set_subscriptionEndDate(StacsUtil.getDateFormat().parse(value));
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
	private void setSubscriptionStartDate(String value) {
		if(value !=null) {
			//if date is in a long format
			if(value.matches("\\d*")) {
				set_subscriptionStartDate(new Date(Long.parseLong(value)));
			}
			//otherwise date is in a readable format
			else {
				try {
					set_subscriptionStartDate(StacsUtil.getDateFormat().parse(value));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private void setTxFeeInPaymentCurrency(String value) {
		if(value!=null) {
			_txFeeInPaymentCurrency = new BigDecimal(value);
		}
	}
	private void setIsStableCoin(String value) {
		if(value.compareTo("TRUE")==0) {
			_isStableCoin = true;
		}
		else if(value.compareTo("false")==0) {
			_isStableCoin = false;
		}
	}
	private void setNumOfLockDays(String value) {
		if(value!=null) {
			_numOfLockDays = Integer.parseInt(value);
		}
	}
	private void setMaxSubscriptionAmount(String value) {
		if(value!=null) {
			_maxSubscriptionAmount = Double.parseDouble(value);
		}
	}
	private void setMinSubscriptionAmount(String value) {
		if(value!=null) {
			_minSubscriptionAmount = Double.parseDouble(value);
		}
	}
	private void setUpdateTime(String value) {
		if(value !=null) {
			//if date is in a long format
			if(value.matches("\\d*")) {
				set_updateTime(new Date(Long.parseLong(value)));
			}
			//otherwise date is in a readable format
			else {
				try {
					set_updateTime(StacsUtil.getDateFormat().parse(value));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void setTotalIssuedTokens() {
		_totalIssuedTokens = new TokenUnit(_tokenCode,_totalTokenSupply);
	}
	private void setAvailableIssuedTokens() {
		_availableIssuedTokens = new TokenUnit(_tokenCode,_availableAmountForSubscription);
	}
	private void setPayment() {
		_payment = new TokenUnit(_paymentCurrency,_paymentAmount);
	}
	private void setTxFee() {
		_txFee = new TokenUnit(_paymentCurrency,_txFeeInPaymentCurrency);
	}
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		IssuanceTxTypeBOEnum respProperty = (IssuanceTxTypeBOEnum)T;
		switch(respProperty) {
		case SPONSOR:
			set_sponsor(attrValue);
			break;
		case TOTAL_TOKEN_SUPPLY:
			setTotalTokenSupply(attrValue);
			break;
		case BIZ_MODEL:
			set_bizModel(attrValue);
			break;
		case BLOCK_HEIGHT:
			setBlockHeight(attrValue);
			break;
		case CREATE_TIME:
			setCreateTime(attrValue);
			break;
		case TOKEN_CODE:
			set_tokenCode(attrValue);
			break;
		case DATA:
			set_data(attrValue);
			break;
		case AVAILABLE_AMOUNT_FOR_SUBSCRIPTION:
			setAvailableAmountForSubscription(attrValue);
			break;
		case PAYMENT_CURRENCY:
			set_paymentCurrency(attrValue);
			break;
		case SUBSCRIPTION_END_DATE:
			setSubscriptionEndDate(attrValue);
			break;
		case PAYMENT_AMOUNT:
			setPaymentAmount(attrValue);
			break;	
		case SUBSCRIPTION_START_DATE:
			setSubscriptionStartDate(attrValue);
			break;
		case TX_FEE_IN_PAYMENT_CURRENCY:
			setTxFeeInPaymentCurrency(attrValue);
			break;
		case IS_STABLE_COIN:
			setIsStableCoin(attrValue);
			break;
		case SPONSOR_ADDRESS:
			set_sponsorAddress(attrValue);
			break;
		case ISSUER:
			set_issuer(attrValue);
			break;
		case NUM_OF_LOCK_DAYS:
			setNumOfLockDays(attrValue);
			break;
		case MAX_SUBSCRIPTION_AMOUNT:
			setMaxSubscriptionAmount(attrValue);
			break;
		case MIN_SUBSCRIPTION_AMOUNT:
			setMinSubscriptionAmount(attrValue);
			break;	
		case TOKEN_CUSTODY_ADDRESS:
			set_tokenCustodyAddress(attrValue);
			break;
		case STATUS:
			set_status(attrValue);
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
		case VERSION:
			set_version(attrValue);
			break;
		}
		if(_tokenCode!=null) {
			if(_totalTokenSupply!=null) {
				setTotalIssuedTokens();
			}
			if(_availableAmountForSubscription!=null) {
				setAvailableIssuedTokens();
			}
		}
		if(_paymentCurrency!=null) {
			if(_paymentAmount!=null) {
				setPayment();
			}
			if(_txFeeInPaymentCurrency!=null) {
				setTxFee();
			}
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

	public String getBizModel() {
		return _bizModel;
	}
}
