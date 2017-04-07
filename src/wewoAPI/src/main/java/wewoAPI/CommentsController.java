package wewoAPI;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;

import DatabaseController.CommentDTO;
import DatabaseController.CommentRepository;
import DatabaseController.DALException;
import DatabaseController.MySQLCommentRepository;
import DatabaseController.TaskDTO;
import DatabaseController.TaskRespository;
import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
import modelPOJO.Comment;
import modelPOJO.DoubleIDObject;
import modelPOJO.FindDataObject;
import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.Task;

class CommentController{
	CommentRepository repository;
	TaskRespository taskRepo;
	
	public CommentController()
	{
		repository = new MySQLCommentRepository();
		taskRepo = new TaskRespository();
	}
	
	public CommentController(CommentRepository repository)
	{
		this.repository = repository;
	}
	
	private void verifyLogin(Context context) throws UnauthorizedException
	{
		if(context.getIdentity() == null || context.getIdentity().getIdentityId().isEmpty())
		{
			throw new UnauthorizedException("Invalid login");
		}	
	}
	
	public IDObject createComment(Comment comment, Context context) throws UnauthorizedException{
		 
		verifyLogin(context);
		
		IDObject newCommentID = new IDObject();
		
		CommentDTO dto = CommentDTO.fromModel(comment);
		try {
			repository.createComment(dto);
			newCommentID.setID(dto.getID());
			return newCommentID;
		}
		catch(ForeignKeyFailed e)
		{
			throw new exceptions.BadRequestException("No such task");
			//TODO Find ud af om det er en ugyldig task eller om brugeren ikke er oprettet i databasen?
			try{
				rep
			}
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newCommentID;
		
	}
	
	public JsonList<Comment> getCommentList(IDObject taskId, Context context){
		List<CommentDTO> dtoList;
		List<Comment> commentList = new ArrayList<Comment>();
		try {
			dtoList = repository.getCommentList(taskId.getID());
			for(CommentDTO dto: dtoList){
				Comment com = new Comment();
				com.setText(dto.getText());
				com.setOwner(dto.getOwnerId());
				com.setDate(dto.getDate());
				commentList.add(com);
			}
			JsonList<Comment> resultJson = new JsonList<Comment>();
			resultJson.setElements(commentList);
			return resultJson;
			
		}catch(ForeignKeyFailed e)
		{
			throw new exceptions.BadRequestException("No such task");
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public Comment getComment(DoubleIDObject SharedId, Context context)
	{
		CommentDTO dto;
		try {
			dto = repository.getComment(SharedId.getFirstID(), SharedId.getSecondID());
			Comment comment = new Comment();
			comment.setText(dto.getText());
			comment.setDate(dto.getDate());
			comment.setOwner(dto.getOwnerId());
			return comment;
			
		}catch(ForeignKeyFailed e)
		{
			throw new exceptions.BadRequestException("No such task");
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateComment(Comment comment, Context context) throws UnauthorizedException, NotFoundException
	{
		verifyLogin(context);
		
		try {
			CommentDTO dto = repository.getComment(comment.getTaskID(),comment.getID());
			if(dto==null)
			{
				throw new exceptions.NotFoundException("No such comment");
			}
			if(dto.getOwnerId().equals(context.getIdentity().getIdentityId())){
				dto = new CommentDTO()
						.setText(comment.getText())
						.setDate(comment.getDate())
						.setOwnerId(comment.getOwner());
				repository.updateComment(dto);
			}
		}catch(ForeignKeyFailed e)
		{
			throw new exceptions.BadRequestException("No such task");
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int deleteComment(Comment comment, Context context) throws UnauthorizedException
	{
		verifyLogin(context);
		
		try {
			CommentDTO commentDTO = repository.getComment(comment.getTaskID(), comment.getID());
			if(commentDTO.getOwnerId().equals(context.getIdentity().getIdentityId())){
				repository.deleteComment(comment.getTaskID(), comment.getID());
			}
		}catch(ForeignKeyFailed e)
		{
			throw new exceptions.BadRequestException("No such task");
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
}
