package DatabaseController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import DatabaseController.DALException.ForeignKeyException;
import exceptions.BadRequestException;

public interface CommentRepository {
	//Task/{id}/comments/{commentID}
	CommentDTO getComment(int taskId, int commentId) throws DALException;
	HashMap<String, String> getCommentList(int taskId) throws DALException;
	int createComment(CommentDTO com) throws DALException;
	int updateComment(CommentDTO com) throws DALException;
	int deleteComment(int taskId, int commentId) throws DALException;
}
