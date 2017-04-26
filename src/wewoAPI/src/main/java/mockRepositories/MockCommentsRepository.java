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
	for(int i = 0; i < 100; i++){
		List<CommentDTO> a = new ArrayList<CommentDTO>();
		a.add(null);
		a.add(null);
		a.add(null);
		a.add(null);
		a.add(null);
		a.add(null);
		database.add(a);
	}
}

	public CommentDTO getComment(int taskId, int commentId) throws DALException {
		try{
			System.out.println(database.toString());
			System.out.println(taskId + " " + commentId);
			return database.get(taskId).get(commentId);
		}
		catch(IndexOutOfBoundsException e){
			return null;
		}
	}

	public List<CommentDTO> getCommentList(int taskId) throws DALException {
		return new ArrayList<CommentDTO>(database.get(taskId));
	}

	public int createComment(CommentDTO com) throws DALException {
		database.get(com.getTaskID()).add(com.getID(),com);
		System.out.println(database.toString());
		return database.size() - 1;
	}

	public int updateComment(CommentDTO com) throws DALException {
		System.out.println(com.getTaskID() + " " + com.getID());
		database.get(com.getTaskID()).set(com.getID(),com);
		return 1;
	}

	public int deleteComment(int taskId, int commentId) throws DALException {
		database.remove(commentId);
		return 1;
	}
}
