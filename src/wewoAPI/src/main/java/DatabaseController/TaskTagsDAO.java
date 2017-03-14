package DatabaseController;

import java.util.List;

public interface TaskTagsDAO {
	
	List<TaskTagsDTO> getTaskTagsList(int TaskID) throws DALException;
	List<TaskTagsDTO> getTagsTasksList(List<Integer> tags) throws DALException;
	int createTaskTags(TaskTagsDTO tas) throws DALException;
	int deleteTaskTags(int TagID, int TaskID) throws DALException;
	
}
