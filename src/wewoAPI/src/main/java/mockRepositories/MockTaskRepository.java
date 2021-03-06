package mockRepositories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import DatabaseController.DALException;
import DatabaseController.TaskDTO;
import DatabaseController.TaskRespository;

public class MockTaskRepository implements TaskRespository {
	private List<TaskDTO> database = new ArrayList<TaskDTO>();	
	public TaskDTO getTask(int id) throws DALException {
		try{
			return database.get(id);
		}
		catch(IndexOutOfBoundsException e)
		{
			return null;
		}
	}

	public List<TaskDTO> queryTasks(List<Integer> tags) throws DALException {
		List<TaskDTO> tasks = new ArrayList<TaskDTO>();
		for (int i = 0; i < database.size(); i++) {
			for (int j = 0; j < database.get(i).getTags().size(); j++) {
					if(tags.contains(database.get(i).getTags().get(j)))
					{
						tasks.add(database.get(i));
					}
			}
		}
		return tasks;
	}

	public List<TaskDTO> getTaskList() throws DALException {
		return database;
	}

	public int createTask(TaskDTO tas) throws DALException {
		List<Integer> tags = tas.getTags();
		for (int i = 0; i < tags.size(); i++) {
			if(tags.get(i) > Integer.MAX_VALUE*0.5)
			{
				throw new DALException.ForeignKeyException(null);
			}
		}
		database.add(tas);
		tas.setId(database.size() - 1);
		return database.size() - 1;
	}

	public int updateTask(TaskDTO tas) throws DALException {
		database.set(tas.getId(), tas);
		return 1;
	}

	public int deleteTask(int id) throws DALException {
		database.remove(id);
		return 1;
	}	
}
