package wewoAPI;
import com.amazonaws.services.lambda.runtime.Context;

import modelPOJO.Application;
import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.ResponseObject;
public class ApplicantsController {
	JsonList<String> GetApplicants(IDObject taskid, Context context)
	{
		return null;
	}
	
	void PostApplications(Application application, Context context)
	{
		
	}
	ResponseObject<String> GetApplication(IDObject object, Context context)
	{
		return null;
	}

}
