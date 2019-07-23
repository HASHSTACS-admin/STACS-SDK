package hashstacs.sdk.wallet;

public class PaymentRecipientRecord {

	private String _recipientAddr;
	private Double _amount;
	private String _currency;
	
	public PaymentRecipientRecord(String recipientAddress, Double amount, String tokenCurrency) {
		_recipientAddr = recipientAddress;
		_amount = amount;
		_currency = tokenCurrency;
	}
	
	public String getRecipientAddress() {
		return _recipientAddr;
	}
	public Double getAmount() {
		return _amount;
	}
	public String getCurrency() {
		return _currency;
	}
}
