package mockRepositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DatabaseController.CommentDTO;
import DatabaseController.CommentRepository;
import DatabaseController.DALException;
import DatabaseController.MySQLException.ForeignKeyException;
import DatabaseController.TaskDTO;
import exceptions.BadRequestException;

public class MockCommentsRepository implements CommentRepository{

//HashMap<primary_key, CommentDTO> database = new HashMap<primary_key, CommentDTO>();
private List<List<CommentDTO>> database = new ArrayList<List<CommentDTO>>(420);

public MockCommentsRepository(){
	for(int i = 0; i < 420; i++){
		database.add(i, null);
	}
	database.add(420, new ArrayList<CommentDTO>(360));
	for(int i = 0; i < 360; i++){
		database.get(420).add(i, null);
	}
}

	public CommentDTO getComment(int taskId, int commentId) throws DALException {
		return database.get(taskId).get(commentId);
	}

	public List<CommentDTO> getCommentList(int taskId) throws DALException {
		return new ArrayList<CommentDTO>(database.get(taskId));
	}

	public int createComment(CommentDTO com) throws DALException {
		database.get(com.getTaskID()).add(com.getID(),com);
		return database.size() - 1;
	}

	public int updateComment(CommentDTO com) throws DALException {
		database.get(com.getTaskID()).add(com.getID(),com);
		return 1;
	}

	public int deleteComment(int taskId, int commentId) throws DALException {
		database.remove(commentId);
		return 1;
	}
}
