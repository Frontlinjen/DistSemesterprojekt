package DatabaseController;

import java.util.List;

public interface ApplicationRepository {
	
	ApplicationDTO getApplication(String applierID, int taskID) throws DALException;
	List<String> getApplicationList(int id) throws DALException;
	int createApplication(ApplicationDTO app) throws DALException;
	int updateApplication(ApplicationDTO app) throws DALException;
	int deleteApplication(String applierID, int taskID) throws DALException;
}
