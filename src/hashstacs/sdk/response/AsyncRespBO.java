package hashstacs.sdk.response;

import lombok.Getter;

@Getter
public class AsyncRespBO {

	private String _responseCode;
	private String _txId;
		
	public AsyncRespBO(String respCode, String txId) {
		_responseCode=respCode;
		_txId=txId;
	}
	
	/**
	 * retrieves corresponding message to the response codes
	 * @return
	 */
	public String getAsyncRespMsg() {
		
		for(asyncResponseCodesEnum code : asyncResponseCodesEnum.values()) {
			if(_responseCode.compareTo(code.getRespCode())==0) {
				return code.getRespMessage();
			}
		}
		
		return "no response code found";
	}
	
	/**
	 * Current list of response codes for asynchronous requests
	 * @author Hashstacs Solutions Engineering
	 *
	 */
	public enum asyncResponseCodesEnum {
		SUCCESS("SUCCESS","000000"),
		FAILED_QUERY("FAILED_QUERY","-1"),
		EMPTY_DATA_FIELD("EMPTY_DATA_FIELD","-100"),
		TX_ID_IN_USE("TX_ID_IN_USE","000001"),
		MISSING_PARAMS("MISSING_PARAMS","100001"),
		TRANSFER_CANNOT_OCCUR_DURING_SUBSCRIPTION_PERIOD("TRANSFER_CANNOT_OCCUR_DURING_SUBSCRIPTION_PERIOD","200005"),
		TOKEN_CODE_ALREADY_EXISTS("TOKEN_CODE_ALREADY_EXISTS","200017"),
		SMART_CONTRACT_EXECUTION_FAILURE("SMART_CONTRACT_EXECUTION_FAILURE","200035"),//no permission or subscribe error
		INSUFFICIENT_FUNDS("INSUFFICIENT_FUNDS","200036"),
		INSUFFICIENT_FUNDS_FOR_DISTRIBUTION("INSUFFICIENT_FUNDS_FOR_DISTRIBUTION","200052"),
		UNFREEZE_FAILED("UNFREEZE_FAILED","300042"),
		GRANT_PERMISSION_SUCCESS("GRANT_PERMISSION_SUCCESS","300050"),
		GRANT_PERMISSION_VERIFIED_SUCCESS("GRANT_PERMISSION_VERIFIED_SUCCESS","300051")
		//NEW_FIELD("NEW_FIELD","case-sensitive-key-here")
		;
		
		private final String _respMessage;
		private final String _respCode;
		
		asyncResponseCodesEnum(String key, String value) {
			this._respMessage = key;
			this._respCode = value;
		}
		
		public String getRespMessage() {
			return _respMessage;
		}
		public String getRespCode() {
			return _respCode;
		}
	}
}
