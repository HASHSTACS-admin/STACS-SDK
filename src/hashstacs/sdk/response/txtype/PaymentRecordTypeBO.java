package hashstacs.sdk.response.txtype;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import hashstacs.sdk.response.RespBO;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.TokenUnit;
import hashstacs.sdk.util.StacsResponseEnums.PaymentRecordTypeBOEnum;
import hashstacs.sdk.wallet.PaymentRecipientRecord;
import lombok.Setter;

@Setter
public class PaymentRecordTypeBO extends RespBO {

	private String _tokenCode;
	
	private String _paymentCurrency;
	private BigDecimal _totalAmountPayable;
	private TokenUnit _payment;
	
	private Date _createTime;
	private Integer _blockHeight;
	private List<PaymentRecipientRecord> _recipientRecords;
	private String _msg;
	private String _payerAddress;
	private String _status;
	private String _txId;
	private Date _updateTime;
	
	private String _callbackUrl;
	private String _signature;
	private String _signedValue;
	private String _size;
	
	private String _rawRespCode;
	private String _rawMsg;
	
	public PaymentRecordTypeBO() {
		
	}
	public String getTokenCode() {
		return _tokenCode;
	}
	public TokenUnit getTotalPaymentDetails() {
		return _payment;
	}
	public Date getCreationTime() {
		return _createTime;
	}
	public Date getUpdateTime() {
		return _updateTime;
	}
	public Integer getBlockHeight() {
		return _blockHeight;
	}
	public List<PaymentRecipientRecord> getPaymentRecipientDetails() {
		return _recipientRecords;
	}
	public String getMsg() {
		return _msg;
	}
	public String getPayerAddress() {
		return _payerAddress;
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
	private void setBlockHeight(String value) {
		if(value!=null) {
			_blockHeight = Integer.parseInt(value);
		}
	}
	private void setTotalAmountPayable(String value) {
		if(value!=null) {
			_totalAmountPayable = new BigDecimal(value);
		}
	}
	private void setUpdateTime(String value) {
		if(value !=null) {
			_updateTime = new Date(Long.parseLong(value));
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
		_payment = new TokenUnit(_paymentCurrency,_totalAmountPayable);
	}

	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		PaymentRecordTypeBOEnum respProperty = (PaymentRecordTypeBOEnum)T;
		switch(respProperty) {
		case CALLBACK_URL:
			set_callbackUrl(attrValue);
			break;
		case CREATE_TIME:
			setCreateTime(attrValue);
			break;
		case BLOCK_HEIGHT:
			setBlockHeight(attrValue);
			break;
		case PAYMENT_CURRENCY:
			set_paymentCurrency(attrValue);
			break;
		case MESSAGE:
			set_msg(attrValue);
			break;
		case PAYER_ADDRESS:
			set_payerAddress(attrValue);
			break;
		case SIGNATURE:
			set_signature(attrValue);
			break;
		case SIGNED_VALUE:
			set_signedValue(attrValue);
			break;
		case SIZE:
			set_size(attrValue);
			break;
		case STATUS:
			set_status(attrValue);
			break;
		case TOKEN_CODE:
			set_tokenCode(attrValue);
			break;
		case TOTAL_AMOUNT_PAYABLE:
			setTotalAmountPayable(attrValue);
			break;
		case TX_ID:
			set_txId(attrValue);
			break;
		case UPDATE_TIME:
			setUpdateTime(attrValue);
			break;
		default:
			//log error
			break;
		}
		if(_paymentCurrency!=null && _totalAmountPayable!=null) {
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
