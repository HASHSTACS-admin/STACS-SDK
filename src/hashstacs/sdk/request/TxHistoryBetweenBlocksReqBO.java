package hashstacs.sdk.request;

import com.hashstacs.client.bo.TxQueryWithHeightBO;
import com.hashstacs.sdk.crypto.GspECKey;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TxHistoryBetweenBlocksReqBO extends ReqBO {

	private TxQueryWithHeightBO _origReqObj;
	private long _startHeight;
	private long _endHeight;
	
	/**
	 * returns a maximum of 10 rows in this version
	 * 10 rows counting from the endHeight parameter
	 * 
	 * @param startHeight
	 * @param endHeight
	 */
	public TxHistoryBetweenBlocksReqBO(long startHeight, long endHeight) {
		_origReqObj = new TxQueryWithHeightBO();
		_origReqObj.setStartHeight(startHeight);
		_origReqObj.setEndHeight(endHeight);
	}
	
	public long getStartHeight() {
		return _startHeight;
	}
	public long getEndHeight() {
		return _endHeight;
	}
	public TxQueryWithHeightBO getOrigReqObj() {
		return _origReqObj;
	}
	
	@Override
	public Boolean isValidationSuccessful(GspECKey ecKey) {
		if(!EndHeightBeforeStartHeight()) {			
			return false;
		}
		return true;
	}
	
	private Boolean EndHeightBeforeStartHeight() {
		if(_endHeight>=_startHeight) {
			return true;
		}
		log.debug("EndHeightBeforeStartHeight failed.");
		return false;
	}

}
