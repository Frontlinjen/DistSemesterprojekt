package DatabaseController;

import java.util.List;

public interface TaskDAO {

	TaskDTO getTask(String id) throws DALException;
	List<TaskDTO> getTaskList() throws DALException;
	int createTask(TaskDTO tas) throws DALException;
	int updateTask(TaskDTO tas) throws DALException;
	int deleteTask(String id) throws DALException;
}
