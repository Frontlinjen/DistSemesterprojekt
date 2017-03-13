package DatabaseController;

import java.util.List;

public interface TaskTagsDAO {
	
	List<TaskTagsDTO> getTaskTagsList(String TaskID) throws DALException;
	List<TaskTagsDTO> getTagsTasksList() throws DALException;
	int createTaskTags(TaskTagsDTO tas) throws DALException;
	int deleteTaskTags(int TagID, String TaskID) throws DALException;
	
}
