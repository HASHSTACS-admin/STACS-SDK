package hashstacs.sdk;

import java.math.BigDecimal;
import java.text.ParseException;

import hashstacs.sdk.request.IssueTokenReqBO;
import hashstacs.sdk.util.StacsUtil;
import hashstacs.sdk.util.TokenUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IssueTokenSample {
	
	/*The request object to be sent from the SDK to the STACS Native network*/
	private IssueTokenReqBO _newTokenRequest;
	
	/**
	 * Sample parameters required for issuing a token on a STACS Native network is 
	 * provided here 
	 * @param sponsorWalletAddress
	 * @param tokenCustodyAddress
	 */
	public IssueTokenSample(String sponsorWalletAddress, String tokenCustodyAddress, String tokenCode) {
		//define total issuance amount
		BigDecimal totalAmount = new BigDecimal(10000000000L);
		TokenUnit issueAmount = new TokenUnit(tokenCode,totalAmount);
		
		//define reserved amount that will not be available for subscription
		BigDecimal reserveAmount = new BigDecimal(1000000000L);
		TokenUnit reservedAmount = new TokenUnit(tokenCode,reserveAmount);
		
		//define the price per unit and the currency for subscription
		String paymentCurrency = "USD";
		BigDecimal paymentPerUnit = new BigDecimal(1.5);
		TokenUnit pricePerTokenUnit = new TokenUnit(paymentCurrency,paymentPerUnit);
		
		//define the lot size for subscription
		int lotSize = 10;
		_newTokenRequest = new IssueTokenReqBO(issueAmount,lotSize);
		_newTokenRequest.setReservedAmount(reservedAmount);
		_newTokenRequest.setTokenUnitPricing(pricePerTokenUnit);
		
		//set the type of smart contract to be issued
		_newTokenRequest.setTokenType(IssueTokenReqBO.TokenTypeEnum.BONDS);
				
		//specify sponsor and issuer wallet addresses and names
		//sponsor will have the permission to grant subscription privileges to any wallet addresses 
		_newTokenRequest.setSponsorAddress(sponsorWalletAddress);
		_newTokenRequest.setSponsorName("Hashstacs");
		_newTokenRequest.setTokenCustodyAddress(tokenCustodyAddress);
		_newTokenRequest.setIssuerName("hashstacs");
		
		//set subscription limits
		_newTokenRequest.setMaxSubscriptionLots(10);
		_newTokenRequest.setMinSubscriptionLots(1);
		_newTokenRequest.setNumOfDaysLocked(1);
		
		//start and end date for subscription to be set here
		try {
			_newTokenRequest.setSubscriptionStartDate(StacsUtil.getDateFormat().parse("2019-07-14 09:00:00"));
			_newTokenRequest.setSubscriptionEndDate(StacsUtil.getDateFormat().parse("2019-08-30 18:00:00"));
		} catch (ParseException e) {
			log.error(e.toString());
		}
					
	}
	
	public IssueTokenReqBO getIssueRequest() {
		return _newTokenRequest;
	}
	
}
