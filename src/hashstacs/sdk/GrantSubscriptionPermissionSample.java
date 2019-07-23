package hashstacs.sdk;

import hashstacs.sdk.request.GrantSubscribePermReqBO;

public class GrantSubscriptionPermissionSample {

	/*The request object to be sent from the SDK to the STACS Native network*/
	private GrantSubscribePermReqBO _grantSubscribePermission;
	
	/**
	 *  Sample parameters required for issuing a token on a STACS Native network is 
	 * provided here 
	 * @param investorWalletAddress
	 * @param tokenCode
	 * @param tokenSponsorWalletAddress
	 */
	public GrantSubscriptionPermissionSample(String investorWalletAddress, String tokenCode, String tokenSponsorWalletAddress) {
		_grantSubscribePermission = new GrantSubscribePermReqBO();
		_grantSubscribePermission.setSubscriberWalletAddress(investorWalletAddress);
		_grantSubscribePermission.setTokenCode(tokenCode);
		_grantSubscribePermission.setTokenSponsorAddress(tokenSponsorWalletAddress);
	}
	
	public GrantSubscribePermReqBO getGrantSubscriptionPermissionRequest() {
		return _grantSubscribePermission;
	}
}
