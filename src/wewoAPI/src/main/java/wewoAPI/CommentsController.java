package wewoAPI;

import java.sql.Date;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;

import DatabaseController.CommentDTO;
import DatabaseController.CommentRepository;
import DatabaseController.DALException;
import DatabaseController.MySQLCommentRepository;
import DatabaseController.MySQLTaskRespository;
import DatabaseController.TaskDTO;
import DatabaseController.TaskRespository;
import exceptions.UnauthorizedException;
import modelPOJO.Comment;
import modelPOJO.FindDataObject;
import modelPOJO.IDObject;
import modelPOJO.Task;

class CommentController{
	public IDObject createComment(Comment comment, Context context) throws UnauthorizedException{
		if(context.getIdentity() == null || context.getIdentity().getIdentityId().isEmpty())
		{
			throw new UnauthorizedException();
		}
		IDObject newCommentID = new IDObject();
		
		CommentRepository dao = new MySQLCommentRepository();
		CommentDTO dto = CommentDTO.fromModel(comment);
		try {
			dao.createComment(dto);
			newCommentID.setID(dto.getId()); //TODO Implement Id
			return newCommentID;
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newCommentID;
		
	}
	
	public List<Comment> getCommentList(IDObject taskId, Context context) throws UnauthorizedException{
		if(context.getIdentity() == null || context.getIdentity().getIdentityId().isEmpty())
		{
			throw new UnauthorizedException();
		}
		//Do same as getComment but with a forloop interating every comment in task
	}
	
	public Comment getComment(DoubleIDObject SharedId, Context context)
	{
		CommentRepository dao = new MySQLCommentRepository();
		CommentDTO dto;
		try {
			dto = dao.getComment(SharedId.getFirstId(), SharedId.getSecondId());
			Comment comment = new Comment();
			comment.setText(dto.getText());
			comment.setDate(dto.getDate());
			comment.setOwner(dto.getOwnerId());
			return comment;
			
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateComment(Comment comment, Context context)
	{
		CommentRepository dao = new MySQLCommentRepository();
		
		try {
			CommentDTO dto = dao.getComment(comment.getID());
			if(dto.getOwnerId().equals(context.getIdentity())){ //Are we sure that Id should be an integer?
				dto = new CommentDTO()
						.setText(comment.getText())
						.setDate(comment.getDate())
						.setOwnerId(comment.getOwner());
				dao.updateComment(dto);
			}
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int deleteTask(DoubleIDObject id, Context context)
	{
		CommentRepository dao = new MySQLCommentRepository();
		
		try {
			CommentDTO comment = dao.getComment(id.getFirstId(), id.getSecondId());
			if(comment.getOwnerId().equals(context.getIdentity())){
				dao.deleteComment(id.getFirstId(), id.getSecondId());
			}
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
}
