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
private List<List<CommentDTO>> database = new ArrayList<List<CommentDTO>>(419);

public MockCommentsRepository(){
	database.add(420, new ArrayList<CommentDTO>(359));
}

	public CommentDTO getComment(int taskId, int commentId) throws DALException, BadRequestException {
		return database.get(taskId).get(commentId);
	}

	public List<CommentDTO> getCommentList(int taskId) throws DALException, BadRequestException {
		return new ArrayList<CommentDTO>(database.get(taskId));
	}

	public int createComment(CommentDTO com) throws DALException, BadRequestException {
		database.get(com.getTaskID()).add(com.getID(),com); //Exception if task does not exist
		return database.size() - 1;
	}

	public int updateComment(CommentDTO com) throws DALException, BadRequestException {
		database.get(com.getTaskID()).add(com.getID(),com); //Exception if task does not exist
		return 1;
	}

	public int deleteComment(int taskId, int commentId) throws DALException, BadRequestException {
		database.remove(commentId);
		return 1;
	}
}
