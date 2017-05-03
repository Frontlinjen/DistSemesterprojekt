package mockRepositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mysql.cj.core.exceptions.MysqlErrorNumbers;

import DatabaseController.ApplicationDTO;
import DatabaseController.ApplicationRepository;
import DatabaseController.DALException;

public class MockApplicationRepository implements ApplicationRepository {
	
	
	private Map<Integer, HashMap<String, ApplicationDTO>> database = new HashMap<Integer, HashMap<String, ApplicationDTO>>();

	public MockApplicationRepository(){
		database.put(0,  new HashMap<String, ApplicationDTO>());
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
		if(database.containsKey(id)){
			HashMap<String, ApplicationDTO> applicants = database.get(id);
			Iterator<Entry<String, ApplicationDTO>> it = applicants.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, ApplicationDTO> entry = it.next();
				applicantList.add(entry.getKey());
				System.out.println(entry.getKey());
			}
			return applicantList;
		}
		return null; //error
	}
	public int createApplication(ApplicationDTO app) throws DALException {
		if(database.containsKey(app.getTaskid())){
			database.get(app.getTaskid()).put(app.getApplierid(), app);
		}
		return 0; //error
		}

	public int updateApplication(ApplicationDTO app) throws DALException {
		if(database.containsKey(app.getTaskid())){
			database.get(app.getTaskid()).get(app.getApplierid()).setApplicationMessage(app.getApplicationMessage());
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
