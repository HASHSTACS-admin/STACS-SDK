package hashstacs.sdk;

import hashstacs.sdk.request.TransferTokenReqBO;
import hashstacs.sdk.util.TokenUnit;

public class TransferTokenSample {
	private TransferTokenReqBO _transferTokenRequest;
	
	public TransferTokenSample(String senderWalletAddress, String recipientWalletAddress, TokenUnit transferAmount) {
		_transferTokenRequest = new TransferTokenReqBO();
		_transferTokenRequest.setRecipientWalletAddress(recipientWalletAddress);
		_transferTokenRequest.setSenderWalletAddress(senderWalletAddress);
		_transferTokenRequest.setTransferAmount(transferAmount);
	}
	
	public TransferTokenReqBO getTransferTokenRequest() {
		return _transferTokenRequest;
	}
}
