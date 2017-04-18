package DatabaseController;

import java.util.List;

import DatabaseController.MySQLException.ForeignKeyException;
import exceptions.BadRequestException;

public interface CommentRepository {
	
	CommentDTO getComment(int taskId, int commentId) throws DALException, ForeignKeyException, BadRequestException;
	List<CommentDTO> getCommentList(int taskId) throws DALException, ForeignKeyException, BadRequestException;
	int createComment(CommentDTO com) throws DALException, ForeignKeyException, BadRequestException;
	int updateComment(CommentDTO com) throws DALException, ForeignKeyException, BadRequestException;
	int deleteComment(int taskId, int commentId) throws DALException, ForeignKeyException, BadRequestException;
}
