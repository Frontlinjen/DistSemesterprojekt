package DatabaseController;

import java.util.List;

public interface CommentRepository {
	
	CommentDTO getComment(int taskId, int commentId) throws DALException;
	List<CommentDTO> getCommentList(int taskId) throws DALException;
	int createComment(CommentDTO com) throws DALException;
	int updateComment(CommentDTO com) throws DALException;
	int deleteComment(int taskId, int commentId) throws DALException;
}
