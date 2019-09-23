package hashstacs.sdk.test.dto;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hashstacs.sdk.IssueTokenSample;
import hashstacs.sdk.request.IssueTokenReqBO;
import hashstacs.sdk.test.BaseSetupTest;

public class IssueTokenReqBOTest {
	
	private IssueTokenReqBO _issueTokenReqBO;
		
		
	@BeforeClass
	public static void initialize() {
		BaseSetupTest.setup();
	}
	
	/**
	 * Setup the IssueTokenReqBO object before each test
	 */
	@Before
	public void constructBaseTokenReqBO() {
		IssueTokenSample issueTokenInfo = new IssueTokenSample(BaseSetupTest._sponsorWalletAddress,BaseSetupTest._tokenCustodyAddress,BaseSetupTest.TEST_TOKEN);
		_issueTokenReqBO = issueTokenInfo.getIssueRequest();
	}
	
	@Test
	public void checkRequestValidation() {
		assertTrue(_issueTokenReqBO.isValidationSuccessful(BaseSetupTest._sponsorSignKey));
	}
}
