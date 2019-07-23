package hashstacs.sdk.request;

import com.hashstacs.client.bo.TransactionInfoBO;
import com.hashstacs.sdk.crypto.GspECKey;
import com.hashstacs.sdk.crypto.SnowflakeIdWorker;

import hashstacs.sdk.util.TokenUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class TransferTokenReqBO extends ReqBO {

	private String _txId;
	private String _senderWalletAddress;
	private String _recipientWalletAddress;
	private TokenUnit _transferAmount;
	
	private TransactionInfoBO _origReqObj;
	
	public TransferTokenReqBO() {
		_origReqObj = new TransactionInfoBO();
		_origReqObj.setRandomNum(new SnowflakeIdWorker().nextId());
		_origReqObj.setTxCreateTime(System.currentTimeMillis());
	}
	
	public void setSenderWalletAddress(String value) {
		_senderWalletAddress=value;
		_origReqObj.setFromAddr(_senderWalletAddress);
	}
	public void setRecipientWalletAddress(String value) {
		_recipientWalletAddress=value;
		_origReqObj.setToAddr(_recipientWalletAddress);
	}
	public void setTransferAmount(TokenUnit transferAmount) {
		_transferAmount = transferAmount;
		_origReqObj.setCurrency(transferAmount.get_currency());
		_origReqObj.setAmount(transferAmount.get_amount().toString());
	}
		
	@Override
	public Boolean isValidationSuccessful(GspECKey ecKey) {
		if(!verifyPermission(ecKey)) {
			return false;
		}
		generateTxId();
		return true;
	}
	
	private Boolean verifyPermission(GspECKey ecKey) {
		if(ecKey.getHexAddress().compareTo(_senderWalletAddress)==0) {
			return true;
		}
		log.debug("verifyPermission failed.");
		return false;
	}
	
	private void generateTxId() {
		_txId=GspECKey.generate64TxId(_origReqObj.getOriginalTxId());
		_origReqObj.setTxId(_txId);
	}

}
