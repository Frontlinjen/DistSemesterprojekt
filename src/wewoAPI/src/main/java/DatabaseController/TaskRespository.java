package DatabaseController;

import java.util.List;

public interface TaskRespository {

	TaskDTO getTask(int id) throws DALException;
	List<TaskDTO> queryTasks(List<Integer> tags) throws DALException;
	List<TaskDTO> getTaskList() throws DALException;
	int createTask(TaskDTO tas) throws DALException;
	int updateTask(TaskDTO tas) throws DALException;
	int deleteTask(int id) throws DALException;
}
