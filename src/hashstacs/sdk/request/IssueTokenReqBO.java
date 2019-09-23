package hashstacs.sdk.request;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.hashstacs.client.bo.ContractEnum;
import com.hashstacs.client.bo.IssueCoinRequestBO;
import com.hashstacs.sdk.crypto.GspECKey;

import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.TokenUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class IssueTokenReqBO extends ReqBO {
	
	private String _sponsorAddress; 
	private String _tokenCustodyAddress; 
	private String _issuerName; 
	private String _sponsorName;
	
	private TokenUnit _tokenAndTotalSupply;
	private TokenUnit _tokenAndReserveAmount;
	
	private TokenUnit _tokenUnitPricing;
	
	private TokenTypeEnum _tokenType;
	private int _maxSubscribeLots; 
	private int _minSubscribeLots; 
	private int _numOfDaysLocked;
	
	private String _txId;
	
	private Date _subscriptionStartDate;
	private Date _subscriptionEndDate;
	
	private int _lotSize; 
	
	private IssueCoinRequestBO _origReqObj;
		
	public IssueTokenReqBO(TokenUnit tokenAndTotalSupply, int lotSize) {
		_lotSize=lotSize;
		_tokenAndTotalSupply = tokenAndTotalSupply;
		_origReqObj = new IssueCoinRequestBO();
		_origReqObj.setCurrency(_tokenAndTotalSupply.get_currency());
		_origReqObj.setCurrencyName(_tokenAndTotalSupply.get_currency());//
		_origReqObj.setVersion("1.0.0");
		_origReqObj.setAmount(_tokenAndTotalSupply.get_amount().toString());
	}
	
	/**
	 * the gspkey of this address is required to grant subscription permission to 
	 * investor wallet accounts before those investors can subscribe
	 * @param address
	 */
	public void setSponsorAddress(String address) {
		_sponsorAddress=address;
		_origReqObj.setIssueAddress(_sponsorAddress);
	}
	public void setTokenCustodyAddress(String address) {
		_tokenCustodyAddress=address;
		_origReqObj.setOwnerAddress(_tokenCustodyAddress);
	}
	public void setIssuerName(String name) {
		_issuerName = name;
		_origReqObj.setIssuer(_issuerName);
	}
	public void setSponsorName(String name) {
		_sponsorName = name;
		_origReqObj.setAgency(_sponsorName);
	}
	public void setReservedAmount(TokenUnit tokenAndReserveAmount) {
		if(tokenAndReserveAmount.compareTokenUnitCurrencies(_tokenAndTotalSupply, tokenAndReserveAmount)) {
			_tokenAndReserveAmount = tokenAndReserveAmount;
			BigDecimal availableAmount = _tokenAndTotalSupply.get_amount().subtract(_tokenAndReserveAmount.get_amount());
			_origReqObj.setEnableExchangeAmount(availableAmount.toString());
		}
	}
	public void setTokenType(TokenTypeEnum value) {
		_tokenType=value;
		
		if(value==TokenTypeEnum.STABLE) {
			//_origReqObj.setIsStable(true);
			_origReqObj.setContractType(ContractEnum.STABLE_TOKEN.getType());//
		}
		else {
			//_origReqObj.setIsStable(false);
			_origReqObj.setType(_tokenType.toString());
			_origReqObj.setContractType(ContractEnum.CENTRALIZED_STO.getType());//
		}
	}
	public void setMaxSubscriptionLots(int numOfLots) {
		_maxSubscribeLots=numOfLots;
		int maxSubscribeTokens = _maxSubscribeLots*_lotSize;
		_origReqObj.setMaxExchangeAmount(Integer.toString(maxSubscribeTokens));
	}
	public void setMinSubscriptionLots(int numOfLots) {
		_minSubscribeLots=numOfLots;
		int minSubscribeTokens = _minSubscribeLots*_lotSize;
		_origReqObj.setMinExchangeAmount(Integer.toString(minSubscribeTokens));
	}
	public void setNumOfDaysLocked(int numOfDays) {
		_numOfDaysLocked=numOfDays;
		_origReqObj.setLockDays(Integer.valueOf(_numOfDaysLocked));
	}
	public void setTokenUnitPricing(TokenUnit tokenUnitPricing) {
		_tokenUnitPricing = tokenUnitPricing;
		_origReqObj.setExchangeRatio(_tokenUnitPricing.get_amount());
		_origReqObj.setExchangeCurrency(_tokenUnitPricing.get_currency());
	}
	public void setSubscriptionStartDate(Date startDate) {
		_subscriptionStartDate=startDate;
		_origReqObj.setExchangeStartDate(convertDateToUTCString(_subscriptionStartDate));
	}
	public void setSubscriptionEndDate(Date endDate) {
		_subscriptionEndDate=endDate;
		_origReqObj.setExchangeEndDate(convertDateToUTCString(_subscriptionEndDate));
	}
		
	/**
	 * business logic verification for token issuance:
	 * Ensure reserved amount is less than total amount
	 * 
	 * @author Hashstacs Solutions Engineering
	 *
	 */
	
	public Boolean isValidationSuccessful(GspECKey ecKey) {
		//check that all verification is done
		if(!(
			verifyIssuanceAndReservedAmount() && 
			verifyPriceInfo() &&
			verifySubscriptionDate() &&
			verifySubscribeLots() &&
			priceCurrencyAndTokenCode() &&
			verifyPermission(ecKey) 
			)
		) {
			return false;
		}
		//update final request and send over
		generateTxId();
		
		return true;
	}

	private String generateTxId() {
		_txId = GspECKey.generate64TxId(_origReqObj.getSignMessage());
		_origReqObj.setTxId(_txId);
		_origReqObj.setBizModel(StacsUtil.getBizModelInfo(_origReqObj));
		return _txId;
	}
	
	private String convertDateToUTCString(Date date) {
		DateFormat formatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		return formatterUTC.format(date);
	}
	
	/**
	 * verifies that the sponsor has permission to perform this action
	 * @param ecKey
	 * @return
	 */
	private Boolean verifyPermission(GspECKey ecKey) {
		if(ecKey.getHexAddress().compareTo(_sponsorAddress)==0) {
			return true;
		}
		log.debug("verifyPermission failed.");
		return false;
	}
	
	/**
	 * ensures the available amount for subscription is greater than zero
	 * available amount is the difference between total and reserve 
	 * @return
	 */
	private Boolean verifyIssuanceAndReservedAmount() {
		if(_tokenAndTotalSupply.get_amount().compareTo(_tokenAndReserveAmount.get_amount()) < 0 ) {
			log.debug("verifyIssuanceAndReservedAmount failed.");
			return false;
		}
		return true;
	}
	/**
	 * ensures that the price per token is a positive number
	 * @return
	 */
	private Boolean verifyPriceInfo() {
		if(_tokenUnitPricing.get_amount().intValue()<0) {
			log.debug("verifyPriceInfo failed.");
			return false;
		}
		return true;
	}
	/**
	 * ensure that the subscription date for the token is:
	 * 1) not null
	 * 2) start date is before the end date
	 * @return
	 */
	private Boolean verifySubscriptionDate() {
		if(_subscriptionEndDate == null || _subscriptionStartDate ==null) {
			log.debug("verifySubscriptionDate failed.");
			return false;
		}
		if(_subscriptionEndDate.before(_subscriptionStartDate)) {
			log.debug("verifySubscriptionDate failed.");
			return false;
		}
		return true;
	}
	/**
	 * check that the maximum subscription lots is less than the minimum subscription lots
	 * @return
	 */
	private Boolean verifySubscribeLots() {
		if(_maxSubscribeLots<_minSubscribeLots) {
			log.debug("verifySubscribeLots failed.");
			return false;
		}
		return true;
	}
	/**
	 * check that the token can only be bought with currencies that is not itself 
	 * e.g. a XYZ token cannot be purchased with XYZ tokens
	 * @return
	 */
	private Boolean priceCurrencyAndTokenCode() {
		if(_tokenAndTotalSupply.get_currency().compareTo(_tokenUnitPricing.get_currency())==0) {
			log.debug("priceCurrencyAndTokenCode failed.");
			return false;
		} 
		return true;
	}
	
	public enum TokenTypeEnum {
		OPEN_ENDED_FUND("OPEN_ENDED_FUND"),
		BONDS("BONDS"),
		STOCKS("STOCKS"),
		STABLE("STABLE")
		;
		
		private String stringValue;
		
		TokenTypeEnum(String stringValue) {
			this.stringValue = stringValue;
		}
	
	}

}
