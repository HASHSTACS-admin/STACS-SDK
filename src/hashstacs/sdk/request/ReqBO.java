package hashstacs.sdk.request;

import com.hashstacs.sdk.crypto.GspECKey;

public abstract class ReqBO {

	public abstract Boolean isValidationSuccessful(GspECKey ecKey);

}
