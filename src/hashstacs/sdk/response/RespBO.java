package hashstacs.sdk.response;

public abstract class RespBO {
	
	/**
	 * Flexibly sets all object attributes based on the response key
	 * 
	 * Changes to the returned Response keys can be maintained here in the response key field, e,g, "amount","address" 
	 * 
	 * @param T
	 * @param attrValue
	 */
	public abstract void setAtttribute (Enum<?> T, String attrValue);
	
	/**
	 * Saves the generic response code and message from the response
	 * for debugging and troubleshooting purposes
	 * @param value
	 */
	public abstract void setRawRespCode(String value);
	public abstract void setRawMsg(String value);
	
	/**
	 * save the raw json values into the respBO object
	 * @param value
	 */
	public abstract void setRawJSONObj(Object value);
}
