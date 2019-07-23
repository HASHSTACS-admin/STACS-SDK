package hashstacs.sdk.request;

import java.util.Date;

import com.hashstacs.client.bo.FrozeBO;
import com.hashstacs.client.bo.FrozeEnum;
import com.hashstacs.sdk.crypto.GspECKey;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class FreezeWalletReqBO extends ReqBO {

	private String _freezeAuthorityAddress;
	private String _targetAddress;
	private Date _freezeEndTime; //optional for freeze
	private String _txId;
	private FreezeTokenTypeEnum _condition;
	private FreezeFunctionEnum _freezeFunction;
	
	private FrozeBO _origReqObj = new FrozeBO();
	
	public FreezeWalletReqBO(FreezeFunctionEnum typeOfFreezeFunction) {
		_freezeFunction = typeOfFreezeFunction;
		if(_freezeFunction==FreezeFunctionEnum.UNFREEZE) {
			_freezeEndTime=new Date(0L);
			_origReqObj.setEndTime(0L);
		}
		else if(_freezeFunction==FreezeFunctionEnum.FREEZE) {
			
		} else {
			//TODO log an error
		}
		_origReqObj.setCallBackUrl("http://callback:8080/freeze-result");
	}
	
	public void setFreezeAuthorityAddress(String address) {
		_freezeAuthorityAddress = address;
		_origReqObj.setFromAddress(_freezeAuthorityAddress);
	}
	public void setTargetAddress(String address) {
		_targetAddress = address;
		_origReqObj.setAddress(_targetAddress);
	}
	public void setCondition(FreezeTokenTypeEnum tokenType) {
		_condition=tokenType; 
		_origReqObj.setCurrency(_condition.getRespKey().getType());
	}
	public void setEndTime(Date freezeEndTime) {
		if(_freezeFunction==FreezeFunctionEnum.FREEZE) {
			_freezeEndTime = freezeEndTime;
			_origReqObj.setEndTime(_freezeEndTime.getTime());
		}
	}    
    
	@Override
	public Boolean isValidationSuccessful(GspECKey ecKey) {
		if(!verifyPermission(ecKey)) {
			return false;
		}
		generateTxId();
		return true;
	}
	
	private void generateTxId() {
		_txId=GspECKey.generate64TxId(_origReqObj.getSignMessage() + System.currentTimeMillis());
		_origReqObj.setTxId(_txId);
	}
	
	private Boolean verifyPermission(GspECKey freezeKey) {
		if(freezeKey.getHexAddress().compareTo(_freezeAuthorityAddress)==0) {
			return true;
		}
		log.debug("verifyPermission failed.");
		return false;
	}
	
	public enum FreezeFunctionEnum {
		FREEZE,
		UNFREEZE
		; 
	}
	
	public enum FreezeTokenTypeEnum {
		STABLE_COIN("TOKEN",FrozeEnum.TOKEN),
		TOKEN("TOKEN",FrozeEnum.STO),
		ALL_TOKEN_TYPES("ALL_TOKEN_TYPES",FrozeEnum.ALL_TYPE)
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final FrozeEnum _respKey;
		
		FreezeTokenTypeEnum(String key, FrozeEnum value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public FrozeEnum getRespKey() {
			return _respKey;
		}
		
	}

}
