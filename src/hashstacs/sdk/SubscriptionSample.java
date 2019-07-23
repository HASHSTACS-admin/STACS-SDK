package hashstacs.sdk;

import java.math.BigDecimal;

import hashstacs.sdk.request.SubscribeReqBO;
import hashstacs.sdk.util.TokenUnit;

public class SubscriptionSample {

	/*The request object to be sent from the SDK to the STACS Native network*/
	private SubscribeReqBO _newSubscriptionRequest;
	
	/**
	 * Sample parameters required for issuing a token on a STACS Native network is 
	 * provided here 
	 * 
	 * @param investorWalletAddress
	 * @param tokenToSubscribe
	 */
	public SubscriptionSample(String investorWalletAddress, TokenUnit tokenToSubscribe) {
		_newSubscriptionRequest = new SubscribeReqBO();
		//set the investor's wallet address
		_newSubscriptionRequest.setSubscriberWalletAddress(investorWalletAddress);
		_newSubscriptionRequest.setMessage("subscription message");
 		
		//calculate the payment required and set the payment (hardcoded in this sample) 
		TokenUnit payment = new TokenUnit("USD",new BigDecimal(10));
 		_newSubscriptionRequest.setPayment(payment);
 		
 		//set the subscription amount including the token code and the amount of tokens to purchase
 		_newSubscriptionRequest.SetTokenSubscription(tokenToSubscribe);
	}
	
	public SubscribeReqBO getSubscriptionRequest() {
		return _newSubscriptionRequest;
	}
}
