package hashstacs.sdk.response;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import hashstacs.sdk.util.StacsResponseEnums.LatestBlockResponseEnum;

public class LatestBlockRespBO extends RespBO {

	private Integer _blockHeight;
	private Date _createTime;
	
	private String _rawRespCode;
	private String _rawMsg;
	private JSONObject _rawJsonObject;
	
	public LatestBlockRespBO() {
		
	}
	public Integer getBlockHeight() {
		return _blockHeight;
	}
	public Date getCreateTime() {
		return _createTime;
	}
	public String getRawRespCode() {
		return _rawRespCode;
	}
	public String getRawMsg() {
		return _rawMsg;
	}
	public JSONObject getRawJsonObj() {
		return _rawJsonObject;
	}
	
	private void setBlockHeight(String value) {
		if(value!=null) {
			_blockHeight = Integer.parseInt(value);
		}
	}
	private void setCreateTime(String value) {
		if(value !=null) {
			_createTime = new Date(Long.parseLong(value));
		}
	}
	
	@Override
	public void setAtttribute(Enum<?> T, String attrValue) {
		LatestBlockResponseEnum respProperty = (LatestBlockResponseEnum)T;
		switch(respProperty) {
		case BLOCK_CREATION_TIME:
			setCreateTime(attrValue);
			break;
		case BLOCK_HEIGHT:
			setBlockHeight(attrValue);
			break;
		}
	}
	@Override
	public void setRawRespCode(String value) {
		_rawRespCode=value;
	}
	@Override
	public void setRawMsg(String value) {
		_rawMsg=value;
	}
	@Override
	public void setRawJSONObj(Object value) {
		_rawJsonObject = (JSONObject) value;
	}

}
