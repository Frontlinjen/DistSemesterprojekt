package mockRepositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.cj.core.exceptions.MysqlErrorNumbers;

import DatabaseController.ApplicationDTO;
import DatabaseController.ApplicationRepository;
import DatabaseController.DALException;

public class MockApplicationRepository implements ApplicationRepository {
	
	
	private Map<Integer, HashMap<String, ApplicationDTO>> database = new HashMap<Integer, HashMap<String, ApplicationDTO>>();

	public MockApplicationRepository(){
		database.put(5,  new HashMap<String, ApplicationDTO>());
	}
	
	public ApplicationDTO getApplication(String id, int i) throws DALException {
		try{
			return database.get(i).get(id);
		}
		catch(NullPointerException e)
		{
			return null;
		}
	}

	public List<String> getApplicationList(int id) throws DALException {
		List<String> applicantList = new ArrayList<String>();
		applicantList.addAll(applicantList);
		return applicantList;
	}
	public int createApplication(ApplicationDTO app) throws DALException {
		if(database.containsKey(app.getTaskid())){
			database.get(app.getTaskid()).put(app.getApplierid(), app);
		}
		return 0; //error
		}

	public int updateApplication(ApplicationDTO app) throws DALException {
		if(database.containsKey(app.getTaskid())){
			database.get(app.getApplierid()).get(app.getApplierid()).getApplicationMessage();
		}
		return 0; //error
	}

	public int deleteApplication(String id, int i) throws DALException {
		ApplicationDTO app = new ApplicationDTO();
		if(database.containsKey(app.getTaskid())){
			database.remove(i);
		}
		return 1;
	}



}
