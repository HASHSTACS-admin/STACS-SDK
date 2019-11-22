package hashstacs.sdk;

import java.math.BigDecimal;

import hashstacs.sdk.request.RedeemTokenReqBO;
import hashstacs.sdk.response.GetTokenHoldersRespBO;
import hashstacs.sdk.util.TokenUnit;

public class RedemptionSample {

	/*The request object to be sent from the SDK to the STACS Native network*/
	private RedeemTokenReqBO _redemptionRequest;
	
	public RedemptionSample(String paymentCurrency,String tokenCustodyAddress, String paymentRecordTxId, String tokenToBeRedeemed, GetTokenHoldersRespBO tokenHoldersResp) {
		TokenUnit redemptionPayment = new TokenUnit(paymentCurrency,new BigDecimal(2));
		_redemptionRequest = new RedeemTokenReqBO(tokenCustodyAddress,paymentRecordTxId,redemptionPayment,tokenToBeRedeemed);
		_redemptionRequest.addRecipients(tokenHoldersResp.getTokenHolders());
		
	}
	
	public RedeemTokenReqBO getRedemptionRequest() {
		return _redemptionRequest;
	}
}
