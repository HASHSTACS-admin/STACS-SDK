package hashstacs.sdk.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hashstacs.sdk.crypto.GspECKey;
import com.hashstacs.sdk.wallet.dock.bo.CasDecryptReponse;

import hashstacs.sdk.response.AsyncRespBO;
import hashstacs.sdk.response.txtype.IssuanceTxTypeBO;
import hashstacs.sdk.response.txtype.PaymentRecordTypeBO;
import hashstacs.sdk.response.txtype.SubscriptionTypeBO;
import hashstacs.sdk.response.txtype.TransferTokenTypeBO;
import hashstacs.sdk.util.StacsResponseEnums.IssuanceTxTypeBOEnum;
import hashstacs.sdk.util.StacsResponseEnums.PaymentRecordTypeBOEnum;
import hashstacs.sdk.util.StacsResponseEnums.SubscriptionTypeBOEnum;
import hashstacs.sdk.util.StacsResponseEnums.TransferTokenTypeBOEnum;
import hashstacs.sdk.util.StacsResponseEnums.WalletTokenTxHistoryResponseEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * Add new properties in the getConfigProperty function and in the ConfigEnums enums
 * @author Hashstacs Solutions Engineering
 *
 */
@Slf4j
public class StacsUtil {

	//Configuration File Keys
	private static String _configProperties;
	
	private static FileReader _propertiesFile;
	private static Properties _properties;
	
	private final static SimpleDateFormat _exDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	/**
	 * Wallet Generation 
	 */
	public static void createWallet() {
		GspECKey newKey = new GspECKey();
		System.out.println("Public Key: " + newKey.getPublicKeyAsHex());
		System.out.println("Private Key: " + StacsUtil.toHex(newKey.getPrivKeyBytes()));
		System.out.println("Wallet Address: " + newKey.getHexAddress());
	}
	
	/**
	 * loads the absolute file path of the properties file for the first time
	 */
	public static void loadPropertiesFileAbsLocation(String location) {
		if(_configProperties==null) {
			_configProperties = location;	
		}
	}
	
	/**
	 * returns property files stored in the configuration file
	 * @param configProperty
	 * @return
	 */
	public static String getConfigProperty(ConfigEnums configProperty) {
		initializeProperties();
		
		//add new properties in the config file here
		switch(configProperty) {
		case NODE_PUBKEY:
			return _properties.getProperty(ConfigEnums.NODE_PUBKEY.getValue());
		case NODE_PRIKEY:
			return _properties.getProperty(ConfigEnums.NODE_PRIKEY.getValue());
		case NODE_MERCHANTID:
			return _properties.getProperty(ConfigEnums.NODE_MERCHANTID.getValue());
		case NODE_AESKEY:
			return _properties.getProperty(ConfigEnums.NODE_AESKEY.getValue());
		case NODE_GATEWAY:
			return _properties.getProperty(ConfigEnums.NODE_GATEWAY.getValue());
		case SPONSOR_KEY:
			return _properties.getProperty(ConfigEnums.SPONSOR_KEY.getValue());
		case ISSUER_KEY:
			return _properties.getProperty(ConfigEnums.ISSUER_KEY.getValue());
		case INVESTOR_KEY:
			return _properties.getProperty(ConfigEnums.INVESTOR_KEY.getValue());
		case FREEZE_PERMISSION_KEY:
			return _properties.getProperty(ConfigEnums.FREEZE_PERMISSION_KEY.getValue());
		default:
			break;
		}
		
		return null;
	}
	
	/**
	 * return the date format 
	 * @return
	 */
	public static SimpleDateFormat getDateFormat() {
		return _exDateFormat;
	}
	
	/**
	 * converts byte array to String
	 * typically used to convert keys to readable format
	 * @param bytes
	 * @return
	 */
	public static String toHex(byte[] bytes) {
	    BigInteger bi = new BigInteger(1, bytes);
	    return String.format("%0" + (bytes.length << 1) + "X", bi);
	}
	
	/**
	 * converts Response Data object into JSONObject 
	 * @param resp
	 * @return
	 */
	public static JSONObject getDataParamsFromResponse(CasDecryptReponse resp) {
		JSONObject newObj = JSONObject.parseObject((String)resp.getData());
		return newObj;
	}
	
	/**
	 * initializes the configuration properties file
	 */
	private static void initializeProperties() {
		//if properties file has not been initialized, skip the remainder steps
		if(_configProperties == null) {
			log.error("config properties file has not been loaded.");
			return ;
		}
		
		//if properties has already been initialized, skip the remainder steps
		if(_propertiesFile != null | _properties != null) {
			 return ;
		}
		
		//setup the properties needed from the configuration file
		try {
			_propertiesFile = new FileReader(_configProperties);
			
		} catch (FileNotFoundException e) {
			log.error(e.toString());
		} 
	
		//update the properties object based on the configuration file
		try {
			_properties = new Properties();
			_properties.load(_propertiesFile);
		} catch (IOException e) {
			log.error(e.toString());
		}
		
	}
	
	public static enum ConfigEnums {
		
		//add new properties from the config file here, values are as follows: ENUM,key
		NODE_PUBKEY("NODE_PUBKEY","node_pubkey"),
		NODE_PRIKEY("NODE_PRIKEY","node_prikey"),
		NODE_MERCHANTID("NODE_MERCHANTID","node_merchantid"),
		NODE_AESKEY("NODE_AESKEY","node_aeskey"),
		NODE_GATEWAY("NODE_GATEWAY","node_gateway"),
		SPONSOR_KEY("SPONSOR_KEY","sponsor_key"),
		ISSUER_KEY("ISSUER_KEY","issuer_key"),
		INVESTOR_KEY("INVESTOR_KEY","investor_key"),
		FREEZE_PERMISSION_KEY("FREEZE_PERMISSION_KEY","freeze_key")
		;
		
		private final String _property;
		private final String _value;
		
		ConfigEnums(String key, String value) {
			this._property = key;
			this._value = value;
		}
		public String getProperty() {
			return _property;
		}
		public String getValue() {
			return _value;
		}
	}
	
	/**
	 * converts raw response into a JSONObject
	 * @param rawResponse
	 * @return
	 */
	public static JSONObject getRespObject(CasDecryptReponse rawResponse) {
		
		//check if returned response is null 
		if(rawResponse ==null) {
			return null;
		}
		//if returned response has an error, return a null object
		if(rawResponse.getRespCode().compareTo(AsyncRespBO.asyncResponseCodesEnum.SUCCESS.getRespCode())==1) {
			return null;
		}
		
		return JSONObject.parseObject((String)rawResponse.getData());
	}
	
	/**
	 * returns a JSONArray object from a JSONObject based on keys from the Enum input
	 * @param newObj
	 * @param txHistEnum
	 * @return
	 */
	public static JSONArray getTxHistoryList(JSONObject newObj, WalletTokenTxHistoryResponseEnum txHistEnum) {
		String txHistoryListString = newObj.getString(txHistEnum.getRespKey());
		JSONArray txHistoryList = JSONObject.parseArray(txHistoryListString);
		
		return txHistoryList;
	}
	
	/**
	 * this method takes in the JSON Array containing list of subscription transactions from the response 
	 * and converts the JSON data into a Java object
	 * @param rawList
	 * @return
	 */
	public static List<SubscriptionTypeBO> getSubscribeHistoryFields(JSONArray rawList) {
		List<SubscriptionTypeBO> list = new ArrayList<SubscriptionTypeBO>();
		
		for(int i=0;i<rawList.size();i++) {
			JSONObject tx = JSONObject.parseObject(rawList.getString(i));
			SubscriptionTypeBO txBO = new SubscriptionTypeBO();
			//go over each Enum defined for each JSON key and store the attribute into the object
			for(SubscriptionTypeBOEnum respProperty : SubscriptionTypeBOEnum.values()) {
				txBO.setAtttribute(respProperty, (String)tx.getString(respProperty.getRespKey()));
			}
			list.add(txBO);
		}
		return list;
	}
	
	/**
	 * this method takes in the JSON Array containing list of token issuance transactions from the response 
	 * and converts the JSON data into a Java object
	 * @param rawList
	 * @return
	 */
	public static List<IssuanceTxTypeBO> getIssueHistoryFields(JSONArray rawList) {
		List<IssuanceTxTypeBO> list = new ArrayList<IssuanceTxTypeBO>();
		
		for(int i=0;i<rawList.size();i++) {
			JSONObject tx = JSONObject.parseObject(rawList.getString(i));
			IssuanceTxTypeBO txBO = new IssuanceTxTypeBO();
			//go over each Enum defined for each JSON key and store the attribute into the object
			for(IssuanceTxTypeBOEnum respProperty : IssuanceTxTypeBOEnum.values()) {
				txBO.setAtttribute(respProperty, (String)tx.getString(respProperty.getRespKey()));
			}
			list.add(txBO);
		}
		return list;
	}
	
	/**
	 * this method takes in the JSON Array containing list of payment record transactions from the response 
	 * and converts the JSON data into a Java object
	 * @param rawList
	 * @return
	 */
	public static List<PaymentRecordTypeBO> getPaymentRecordHistoryFields(JSONArray rawList) {
		List<PaymentRecordTypeBO> list = new ArrayList<PaymentRecordTypeBO>();
		
		for(int i=0;i<rawList.size();i++) {
			JSONObject tx = JSONObject.parseObject(rawList.getString(i));
			PaymentRecordTypeBO txBO = new PaymentRecordTypeBO();
			//go over each Enum defined for each JSON key and store the attribute into the object
			for(PaymentRecordTypeBOEnum respProperty : PaymentRecordTypeBOEnum.values()) {
				txBO.setAtttribute(respProperty, (String)tx.getString(respProperty.getRespKey()));
			}
			list.add(txBO);
		}
		return list;
	}
	
	/**
	 * this method takes in the JSON Array containing list of transfer transactions from the response 
	 * and converts the JSON data into a Java object
	 * @param rawList
	 * @return
	 */
	public static List<TransferTokenTypeBO> getTransferHistoryFields(JSONArray rawList) {
		List<TransferTokenTypeBO> list = new ArrayList<TransferTokenTypeBO>();
		
		for(int i=0;i<rawList.size();i++) {
			JSONObject tx = JSONObject.parseObject(rawList.getString(i));
			TransferTokenTypeBO txBO = new TransferTokenTypeBO();
			//go over each Enum defined for each JSON key and store the attribute into the object
			for(TransferTokenTypeBOEnum respProperty : TransferTokenTypeBOEnum.values()) {
				txBO.setAtttribute(respProperty, (String)tx.getString(respProperty.getRespKey()));
			}
			list.add(txBO);
		}
		return list;
	}
	
	public static String getBizModelInfo(Object obj) {
		  int features = 0;
	      features |= SerializerFeature.DisableCircularReferenceDetect.getMask();
	      features |= SerializerFeature.WriteMapNullValue.getMask();
	      features |= SerializerFeature.SortField.getMask();        
	      features |= SerializerFeature.MapSortField.getMask();
	      int JSON_GENERATE_FEATURES = features;
	      
	      String originMsg = JSON.toJSONString(obj, JSON_GENERATE_FEATURES, new SerializerFeature[0]); 
	      
	      return originMsg;
	}
}
