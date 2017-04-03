package wewoAPI;
import com.amazonaws.services.lambda.runtime.Context;

import modelPOJO.Application;
import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.ResponseObject;
public class ApplicantsController {
	public JsonList<String> GetApplicants(IDObject taskid, Context context)
	{
		return null;
	}
	
	public void PostApplications(Application application, Context context)
	{
		
	}
	public 	ResponseObject<String> GetApplication(IDObject object, Context context)
	{
		return null;
	}

}
