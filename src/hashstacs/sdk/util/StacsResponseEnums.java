package hashstacs.sdk.util;

/**
 * This class contains all defined mapped attributes (keys) in the returned response (JSON)
 * Changes or additions to the returned response should be updated here
 * @author Hashstacs Solutions Engineering
 *
 */
public class StacsResponseEnums {

	public enum RawResponseEnum {
		RESP_CODE("RESP_CODE","respCode"),
		RESP_MSG("RESP_MSG","msg")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		RawResponseEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
	}
	
	public enum WalletBalanceResponseEnum {
		TOTAL_AMOUNT("TOTAL_AMOUNT","amount"),
		ADDRESS("ADDRESS","address"),
		FREEZE_AMOUNT("FREEZE_AMOUNT","freezeAmount"),
		CURRENCY("CURRENCY","currency")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		WalletBalanceResponseEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
	}
	
	public enum PaymentRecordTypeBOEnum {
		CALLBACK_URL("CALLBACK_URL","callbackUrl"),
		CREATE_TIME("CREATE_TIME","createTime"),
		BLOCK_HEIGHT("BLOCK_HEIGHT","height"),
		PAYMENT_CURRENCY("PAYMENT_CURRENCY","interestCurrency"),
		RECIPIENT_RECORDS("RECIPIENT_RECORDS","interests"),
		MESSAGE("MESSAGE","msg"),
		PAYER_ADDRESS("PAYER_ADDRESS","payAddr"),
		SIGNATURE("SIGNATURE","sign"),
		SIGNED_VALUE("SIGNED_VALUE","signValue"),
		SIZE("SIZE","size"),
		STATUS("STATUS","status"),
		TOKEN_CODE("TOKEN_CODE","tokenSymbol"),
		TOTAL_AMOUNT_PAYABLE("TOTAL_AMOUNT_PAYABLE","totalAmount"),
		TX_ID("TX_ID","txId"),
		UPDATE_TIME("UPDATE_TIME","updateTime")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		PaymentRecordTypeBOEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
	}
	
	public enum IssuanceTxTypeBOEnum {
		SPONSOR("SPONSOR","agency"),
		SPONSOR_ADDRESS("SPONSOR_ADDRESS","issueAddress"),
		TOKEN_CUSTODY_ADDRESS("TOKEN_CUSTODY_ADDRESS","ownerAddress"), 
		ISSUER("ISSUER","issuer"), 
		TOKEN_CODE("TOKEN_CODE","currency"),
		TOTAL_TOKEN_SUPPLY("TOTAL_TOKEN_SUPPLY","amount"),
		AVAILABLE_AMOUNT_FOR_SUBSCRIPTION("AVAILABLE_AMOUNT_FOR_SUBSCRIPTION","enableExchangeAmount"),
		PAYMENT_CURRENCY("PAYMENT_CURRENCY","exchangeCurrency"),
		PAYMENT_AMOUNT("PAYMENT_AMOUNT","exchangeRatio"),
		TX_FEE_IN_PAYMENT_CURRENCY("TX_FEE_IN_PAYMENT_CURRENCY","fee"),
		MAX_SUBSCRIPTION_AMOUNT("MAX_SUBSCRIPTION_AMOUNT","maxExchangeAmount"),
		MIN_SUBSCRIPTION_AMOUNT("MIN_SUBSCRIPTION_AMOUNT","minExchangeAmount"),
		BIZ_MODEL("BIZ_MODEL","bizModel"),
		BLOCK_HEIGHT("BLOCK_HEIGHT","blockHeight"),
		CREATE_TIME("CREATE_TIME","createTime"),
		DATA("DATA","data"),
		SUBSCRIPTION_START_DATE("SUBSCRIPTION_START_DATE","exchangeStartDate"),
		SUBSCRIPTION_END_DATE("SUBSCRIPTION_END_DATE","exchangeEndDate"),
		IS_STABLE_COIN("IS_STABLE_COIN","isStable"),
		NUM_OF_LOCK_DAYS("NUM_OF_LOCK_DAYS","lockDays"),
		STATUS("STATUS","status"),
		TOKEN_INFO_VO("TOKEN_INFO_VO","tokenInfoVO"),
		TX_ID("TX_ID","txId"),
		SMART_CONTRACT_TYPE("SMART_CONTRACT_TYPE","type"),
		UPDATE_TIME("UPDATE_TIME","updateTime"),
		VERSION("VERSION","version")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		IssuanceTxTypeBOEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
		
	}
	
	public enum SubscriptionTypeBOEnum {
		SUBSCRIBER_ADDRESS("SUBSCRIBER_ADDRESS","address"),
		TOKEN_NUM("TOKEN_NUM","amount"),
		BLOCK_HEIGHT("BLOCK_HEIGHT","blockHeight"),
		CREATE_TIME("CREATE_TIME","createTime"),
		CURRENCY("CURRENCY","currencySymbol"),
		MESSAGE("MESSAGE","msg"),
		PAYMENT_AMOUNT("PAYMENT_AMOUNT","payment"),
		STATUS("STATUS","status"),
		TOKEN_CODE("TOKEN_CODE","symbol"),
		TOKEN_INFO_VO("TOKEN_INFO_VO","tokenInfoVO"),
		TX_ID("TX_ID","txId"),
		SMART_CONTRACT_TYPE("SMART_CONTRACT_TYPE","type"),
		UPDATE_TIME("UPDATE_TIME","updateTime"),
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		SubscriptionTypeBOEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
		
	}
	
	public enum TransferTokenTypeBOEnum {
		AMOUNT("AMOUNT","amount"),
		BLOCK_HEIGHT("BLOCK_HEIGHT","blockHeight"),
		REQ_CREATE_TIME("REQ_CREATE_TIME","createTime"),
		CURRENCY("CURRENCY","currency"),
		DATA("DATA","data"),
		TX_FEE("TX_FEE","fee"),
		FROM_ADDR("FROM_ADDR","fromAddr"),
		RANDOM_NUM("RANDOM_NUM","randomNum"),
		RESULT_CODE("RESULT_CODE","resultCode"),
		RESULT_MSG("RESULT_MSG","resultMsg"),
		STATUS("STATUS","status"),
		TO_ADDR("TO_ADDR","toAddr"),
		TX_CREATED_TIME("TX_CREATED_TIME","txCreatedTime"),
		TXID("TXID","txId"),
		SMART_CONTRACT_TYPE("SMART_CONTRACT_TYPE","type"),
		VERSION("VERSION","version")
		;
		

		private final String _respProperty;
		private final String _respKey;
		
		TransferTokenTypeBOEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
	}
	
	public enum WalletTokenTxHistoryResponseEnum {
		SUBSCRIPTION_LIST("SUBSCRIPTION_LIST","subscribeList"),
		ISSUANCE_LIST("ISSUANCE_LIST","issueList"),
		PAYMENT_RECORD_LIST("PAYMENT_RECORD_LIST","settlementList"),
		TRANSFER_LIST("TRANSFER_LIST","transferList")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		WalletTokenTxHistoryResponseEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
	}
	
	public enum LatestBlockResponseEnum {
		BLOCK_CREATION_TIME("BLOCK_CREATION_TIME","createTime"),
		BLOCK_HEIGHT("BLOCK_HEIGHT","height")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		LatestBlockResponseEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
	}
	
	public enum IssueTokenStatusResponseEnum {
		SMART_CONTRACT_ADDRESS("SMART_CONTRACT_ADDRESS","contractAddress"),
		TOKEN_CODE("TOKEN_CODE","currency"),
		RESULT_CODE("RESULT_CODE","resultCode"),
		RESULT_MSG("RESULT_MSG","resultMsg"),
		STATUS("STATUS","status"),
		TXID("TXID","txId")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		IssueTokenStatusResponseEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
	}
	
	public enum GrantSubscribePermStatusResponseEnum {
		RESULT_CODE("RESULT_CODE","code"),
		RESULT_MSG("RESULT_MSG","msg"),
		TXID("TXID","txId")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		GrantSubscribePermStatusResponseEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
	}
	
	public enum SubscribeStatusResponseEnum {
		BLOCK_HASH("BLOCK_HASH","blockHash"),
		RESULT_CODE("RESULT_CODE","code"),
		RESULT_MSG("RESULT_MSG","msg"),
		OUT_ORDER_SOURCE_CODE("OUT_ORDER_SOURCE_CODE","outOrderNo"),
		TXID("TXID","reqId"),
		STATUS("STATUS","status")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		SubscribeStatusResponseEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
	}
	
	public enum PaymentRecordStatusResponseEnum {
		BLOCK_HEIGHT("BLOCK_HEIGHT","height"),
		TXID("TXID","settlId"),
		STATUS("STATUS","status")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		PaymentRecordStatusResponseEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
	}
	
	public enum DistributePaymentStatusResponseEnum {
		BLOCK_HEIGHT("BLOCK_HEIGHT","height"),
		MESSAGE("MESSAGE","msg"),
		SIGNATURE("SIGNATURE","signValue"),
		TOTAL_AMOUNT("TOTAL_AMOUNT","totalAmount"),
		TXID("TXID","txId"),
		STATUS("STATUS","status")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		DistributePaymentStatusResponseEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
	}
	
	public enum FreezeOrUnfreezeStatusResponseEnum {
		RESULT_CODE("RESULT_CODE","code"),
		MESSAGE("MESSAGE","msg"),
		TXID("TXID","txId")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respProperty;
		private final String _respKey;
		
		FreezeOrUnfreezeStatusResponseEnum(String key, String value) {
			this._respProperty = key;
			this._respKey = value;
		}
		
		public String getRespProperty() {
			return _respProperty;
		}
		public String getRespKey() {
			return _respKey;
		}
	}
}
