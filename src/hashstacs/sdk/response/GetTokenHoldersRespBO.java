package hashstacs.sdk.response;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.response.txtype.TokenHoldersTypeBO;
import lombok.Setter;

@Setter
public class GetTokenHoldersRespBO extends RespBO {

	private List<TokenHoldersTypeBO> _tokenHolderRecord = new ArrayList<>();
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public GetTokenHoldersRespBO() {
		
	}
	
	public GetTokenHoldersRespBO(List<TokenHoldersTypeBO> tokenHolderRecord) {
		_tokenHolderRecord = tokenHolderRecord;
	}
	
	public List<TokenHoldersTypeBO> getTokenHolders() {
		return _tokenHolderRecord;
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
	
	public void appendToTokenHolderRecord(List<TokenHoldersTypeBO> additionalRecords) {
		_tokenHolderRecord.addAll(additionalRecords);
	}
	
	public void appendToTokenHolderRecord(TokenHoldersTypeBO additionalRecord) {
		_tokenHolderRecord.add(additionalRecord);
	}
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		// TODO Auto-generated method stub

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
