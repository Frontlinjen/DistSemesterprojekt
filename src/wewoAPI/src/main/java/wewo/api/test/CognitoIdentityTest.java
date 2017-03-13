package wewo.api.test;

public class CognitoIdentityTest implements com.amazonaws.services.lambda.runtime.CognitoIdentity{


	public String getIdentityId() {
		// TODO Auto-generated method stub
		String username = "TestName";
		return username;
	}

	public String getIdentityPoolId() {
		// TODO Auto-generated method stub
		return null;
	}

}
