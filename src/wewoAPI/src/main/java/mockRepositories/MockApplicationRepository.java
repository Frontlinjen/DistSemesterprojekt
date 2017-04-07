package mockRepositories;

import java.util.ArrayList;
import java.util.List;

import DatabaseController.ApplicationDTO;
import DatabaseController.ApplicationRepository;
import DatabaseController.DALException;

public class MockApplicationRepository implements ApplicationRepository {
	
	
	private List<ApplicationDTO> database = ArrayList<ApplicationDTO>();

	public ApplicationDTO getApplication(int id) throws DALException {
		try{
			return database.get(id);
		}
		catch(IndexOutOfBoundsException e)
		{
			return null;
		}
	}

	public List<String> getApplicationList(int id) throws DALException {
		return null;
	}

	public int createApplication(ApplicationDTO app) throws DALException {
		database.add(app);
		return database.size() - 1;
	}

	public int updateApplication(ApplicationDTO app) throws DALException {
		database.set(Integer.parseInt(app.getTaskid()), app);
		return 1;
	}

	public int deleteApplication(int id) throws DALException {
		database.remove(id);
		return 1;
	}

}
