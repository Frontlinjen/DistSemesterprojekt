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
private Map<Integer, CommentDTO> database = new HashMap<Integer, CommentDTO>();
	
	public CommentDTO getComment(int taskId, int commentId) throws DALException, BadRequestException {
		return database.get(commentId);
	}

	public List<CommentDTO> getCommentList(int taskId) throws DALException, BadRequestException {
		return new ArrayList<CommentDTO>(database.values());
	}

	public int createComment(CommentDTO com) throws DALException, BadRequestException {
		database.put(com.getID(),com); //Exception if task does not exist
		return database.size() - 1;
	}

	public int updateComment(CommentDTO com) throws DALException, BadRequestException {
		database.put(com.getID(),com); //Exception if task does not exist
		return 1;
	}

	public int deleteComment(int taskId, int commentId) throws DALException, BadRequestException {
		database.remove(commentId);
		return 1;
	}
}
