package hashstacs.sdk.wallet;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.response.txtype.IssuanceTxTypeBO;
import hashstacs.sdk.response.txtype.PaymentRecordTypeBO;
import hashstacs.sdk.response.txtype.SubscriptionTypeBO;
import hashstacs.sdk.response.txtype.TransferTokenTypeBO;
import lombok.Getter;

/**
 * This object contains a list of returned response group by history type for the TxHistory call
 * Changes or additions to the returned response should be updated here
 * 
 * @author Hashstacs Solutions Engineering
 *
 */
@Getter
public class TxHistoryRecord {

	private List<IssuanceTxTypeBO> issueTxHistory = new ArrayList<IssuanceTxTypeBO>();
	private List<PaymentRecordTypeBO> paymentRecordTxHistory = new ArrayList<PaymentRecordTypeBO>();
	private List<SubscriptionTypeBO> subscribeTxHistory = new ArrayList<SubscriptionTypeBO>();
	private List<TransferTokenTypeBO> transferTxHistory = new ArrayList<>();
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public TxHistoryRecord() {
		
	}
	
	public void setRawJsonObject(JSONObject obj) {
		_rawJsonObject = obj;
	}
	
	public void setIssueTxHist(List<IssuanceTxTypeBO> list) {
		issueTxHistory.addAll(list);
	}
	public void setPaymentRecordTxHist(List<PaymentRecordTypeBO> list) {
		paymentRecordTxHistory.addAll(list);
	}
	public void setSubscriptionTxHist(List<SubscriptionTypeBO> list) {
		subscribeTxHistory.addAll(list);
	}
	public void setTransferTxHist(List<TransferTokenTypeBO> list) {
		transferTxHistory.addAll(list);
	}
	

	public void setRawRespCode(String value) {
		_rawRespCode = value;
	}
	public void setRawMsg(String value) {
		_rawMsg=value;
	}
	
}
