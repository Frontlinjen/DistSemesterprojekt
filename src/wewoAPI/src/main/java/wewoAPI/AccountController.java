package wewoAPI;

import com.amazonaws.services.lambda.runtime.Context;
import modelPOJO.Account;
import modelPOJO.IDObject;
import modelPOJO.JsonList;

public class AccountController {
	Account GetAccount(IDObject userID, Context context)
	{
		return null;
	}
	void updateAccount(Account account, Context context)
	{
		
	}
	JsonList<IDObject> GetApplicants(IDObject taskid, Context context)
	{
		return null;
	}
	JsonList<IDObject> GetCreatedTasks(IDObject userID, Context context) {
		return null;
	}
	void createAccount(Account account, Context context){
	
	}
}
