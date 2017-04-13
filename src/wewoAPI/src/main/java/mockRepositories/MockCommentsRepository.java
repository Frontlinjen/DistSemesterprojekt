package mockRepositories;

import java.util.ArrayList;
import java.util.List;

import DatabaseController.CommentDTO;
import DatabaseController.DALException;
import DatabaseController.TaskDTO;

public class MockCommentsRepository {
private List<CommentDTO> database = new ArrayList<CommentDTO>();
	
	public CommentDTO getComment(int id) throws DALException {
		try{
			return database.get(id);
		}
		catch(IndexOutOfBoundsException e)
		{
			return null;
		}
	}

	public List<CommentDTO> getCommentList() throws DALException {
		return database;
	}

	public int createComment(CommentDTO com) throws DALException {
		database.add(com);
		return database.size() - 1;
	}

	public int updateComment(CommentDTO com) throws DALException {
		database.set(com.getID(), com);
		return 1;
	}

	public int deleteComment(int id) throws DALException {
		database.remove(id);
		return 1;
	}
}
