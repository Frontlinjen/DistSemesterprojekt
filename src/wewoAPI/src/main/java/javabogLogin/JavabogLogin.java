package javabogLogin;
import java.net.URI;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.*;
import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityRequest;
import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityResult;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleWithWebIdentityRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleWithWebIdentityResult;


public class JavabogLogin {
	final static String PoolID = "eu-west-1:b407e22a-76a1-41bc-87f2-0d9278f62fb4";
	final static String domain = "login.javabog.dk";
	
	public static AWSCredentials GetCredentials() {
		return new BasicAWSCredentials("AKIAJTY44JUGY3LRS6WQ", "+nNnEhFl5dfE5xXpcj/DfoTUdRXwjwC3/vgN7W+p");
		
	}
	public static String Authenticate(LoginData login){
		AmazonCognitoIdentity client = new AmazonCognitoIdentityClient(GetCredentials());
		client.setRegion(Region.getRegion(Regions.EU_WEST_1));
		
		GetOpenIdTokenForDeveloperIdentityRequest requestData = new GetOpenIdTokenForDeveloperIdentityRequest();
		requestData.setIdentityPoolId(PoolID);
		requestData.addLoginsEntry(domain, login.username);
		
		
		GetOpenIdTokenForDeveloperIdentityResult result = client.getOpenIdTokenForDeveloperIdentity(requestData);
		return result.getToken();//System.out.println("Got ID: " + result.getIdentityId() + " and token: " + result.getToken());
	}
	
	//"Client part" should not be included in the lambda
	public static void main(String[] args) throws Exception  {
		LoginData data = new LoginData();
		data.username = "Jeiner";
		String token = JavabogLogin.Authenticate(data);
		AWSSecurityTokenService client = new AWSSecurityTokenServiceClient();
		AssumeRoleWithWebIdentityRequest req = new AssumeRoleWithWebIdentityRequest();
		req.setRoleArn("arn:aws:iam::619517212226:role/Cognito_WEWOUsersAuth_Role");
		req.setRoleSessionName("Jeiner");
		req.setWebIdentityToken(token);
		AssumeRoleWithWebIdentityResult result =  client.assumeRoleWithWebIdentity(req);
	}
}
