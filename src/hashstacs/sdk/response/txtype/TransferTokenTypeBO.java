package hashstacs.sdk.response.txtype;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.response.RespBO;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.TokenUnit;
import hashstacs.sdk.util.StacsResponseEnums.TransferTokenTypeBOEnum;
import lombok.Setter;

@Setter
public class TransferTokenTypeBO extends RespBO {

	private BigDecimal _transferAmount;
	private String _transferCurrency;
	private TokenUnit _transfer;
	
	private BigDecimal _txFee;
	
	private Integer _blockHeight;
	private Date _reqCreateTime;
		
	private String _fromAddr;
	private String _resultCode;
	private String _resultMsg;
	private String _status;
	private String _toAddr;
	private Date _txCreatedTime;
	private String _txId;
	private String _smartContractType;
	
	private Object _data;
	private String _randomNum;
	private String _version;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public TransferTokenTypeBO() {
		
	}
	public TokenUnit getTransferAmount() {
		return _transfer;
	}
	public BigDecimal getTxFee() {
		return _txFee;
	}
	public Integer getBlockHeight() {
		return _blockHeight;
	}
	public Date getRequestCreationTime() {
		return _reqCreateTime;
	}
	public Date getTxCreationTime() {
		return _txCreatedTime;
	}
	public String getSenderWalletAddress() {
		return _fromAddr;
	}
	public String getRecipientWalletAddress() {
		return _toAddr;
	}
	public String getResultCode() {
		return _resultCode;
	}
	public String getResultMsg() {
		return _resultMsg;
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
	
	private void setAmount(String value) {
		if(value!=null) {
			_transferAmount = new BigDecimal(value);
		}
	}
	private void setBlockHeight(String value) {
		if(value!=null) {
			_blockHeight = Integer.parseInt(value);
		}
	}
	private void setReqCreateTime(String value) {
		if(value !=null) {
			//if date is in a long format
			if(value.matches("\\d*")) {
				_reqCreateTime = new Date(Long.parseLong(value));
			}
			//otherwise date is in a readable format
			else {
				try {
					_reqCreateTime =StacsUtil.getDateFormat().parse(value);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void setTxFee(String value) {
		if(value!=null) {
			_txFee = new BigDecimal(value);
		}
	}
	
	private void setTxCreatedTime(String value) {
		if(value !=null) {
			//if date is in a long format
			if(value.matches("\\d*")) {
				_txCreatedTime = new Date(Long.parseLong(value));
			}
			//otherwise date is in a readable format
			else {
				try {
					_txCreatedTime =StacsUtil.getDateFormat().parse(value);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void setTransferAmount() {
		_transfer = new TokenUnit(_transferCurrency,_transferAmount);
	}
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		TransferTokenTypeBOEnum respProperty = (TransferTokenTypeBOEnum)T;
		switch(respProperty) {
		case AMOUNT:
			setAmount(attrValue);
			break;
		case BLOCK_HEIGHT:
			setBlockHeight(attrValue);
			break;
		case REQ_CREATE_TIME:
			setReqCreateTime(attrValue);
			break;
		case CURRENCY:
			set_transferCurrency(attrValue);
			break;
		case DATA:
			set_data(attrValue);
			break;
		case TX_FEE:
			setTxFee(attrValue);
			break;
		case FROM_ADDR:
			set_fromAddr(attrValue);
			break;
		case RANDOM_NUM:
			set_randomNum(attrValue);
			break;
		case RESULT_CODE:
			set_resultCode(attrValue);
			break;
		case RESULT_MSG:
			set_resultMsg(attrValue);
			break;
		case STATUS:
			set_status(attrValue);
			break;
		case TO_ADDR:
			set_toAddr(attrValue);
			break;
		case TX_CREATED_TIME:
			setTxCreatedTime(attrValue);
			break;
		case TXID:
			set_txId(attrValue);
			break;
		case SMART_CONTRACT_TYPE:
			set_smartContractType(attrValue);
			break;
		case VERSION:
			set_version(attrValue);
			break;
		}
		if(_transferAmount !=null && _transferCurrency !=null) {
			setTransferAmount();
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
	@Override
	public void setRawJSONObj(Object value) {
		_rawJsonObject = (JSONObject) value;
	}

}
