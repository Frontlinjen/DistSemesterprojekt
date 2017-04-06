package DatabaseController;

import java.util.List;

public interface ApplicationRepository {
	
	ApplicationDTO getApplication(int id) throws DALException;
	List<String> getApplicationList(int id) throws DALException;
	int createApplication(ApplicationDTO app) throws DALException;
	int updateApplication(ApplicationDTO app) throws DALException;
	int deleteApplication(int id) throws DALException;
}
