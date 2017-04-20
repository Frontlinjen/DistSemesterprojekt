package wewoAPI;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;

import DatabaseController.AccountRepository;
import DatabaseController.CommentDTO;
import DatabaseController.CommentRepository;
import DatabaseController.DALException;
import DatabaseController.MySQLAccountRepository;
import DatabaseController.MySQLCommentRepository;
import DatabaseController.MySQLException;
import DatabaseController.MySQLException.ForeignKeyException;
import DatabaseController.MySQLTaskRepository;
import DatabaseController.TaskDTO;
import DatabaseController.TaskRespository;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
import modelPOJO.Comment;
import modelPOJO.DoubleIDObject;
import modelPOJO.FindDataObject;
import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.Task;

public class CommentsController{
	CommentRepository repository;
	TaskRespository taskRepo;
	AccountRepository accountRepo;
	
	public CommentsController()
	{
		repository = new MySQLCommentRepository();
		taskRepo = new MySQLTaskRepository();
		accountRepo = new MySQLAccountRepository();
	}
	
	public CommentsController(CommentRepository repository)
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
		catch(ForeignKeyException e)
		{
			//Is the task legit
			try {
				if(taskRepo.getTask(comment.getTaskID()) == null){
					throw new exceptions.BadRequestException("No such task");
				}
			} catch (DALException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Is the user legit
			try {
				if(accountRepo.getAccount(comment.getOwner()) == null){
					throw new exceptions.UnauthorizedException("Unauthorized");
				}
			} catch (DALException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadRequestException e) {
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
			
		}catch(ForeignKeyException e)
		{
			//Is the task legit
			try {
				if(taskRepo.getTask(taskId.getID()) == null){
					throw new exceptions.BadRequestException("No such task");
				}
			} catch (DALException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public Comment getComment(DoubleIDObject SharedId, Context context) throws NotFoundException
	{
		CommentDTO dto;
		try {
			dto = repository.getComment(SharedId.getFirstID(), SharedId.getSecondID());
			Comment comment = new Comment();
			comment.setText(dto.getText());
			comment.setDate(dto.getDate());
			comment.setOwner(dto.getOwnerId());
			return comment;
			
		}catch(ForeignKeyException e)
		{
			//Is the task legit
			try {
				if(taskRepo.getTask(SharedId.getFirstID()) == null){
					throw new exceptions.BadRequestException("No such task");
				}
			} catch (DALException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadRequestException e) {
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
		}catch(ForeignKeyException e)
		{
			//Is the task legit
			try {
				if(taskRepo.getTask(comment.getTaskID()) == null){
					throw new exceptions.BadRequestException("No such task");
				}
			} catch (DALException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Is the user legit
			try {
				if(accountRepo.getAccount(comment.getOwner()) == null){
					throw new exceptions.UnauthorizedException("Unauthorized");
				}
			} catch (DALException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int deleteComment(Comment comment, Context context) throws UnauthorizedException, NotFoundException
	{
		verifyLogin(context);
		
		try {
			CommentDTO commentDTO = repository.getComment(comment.getTaskID(), comment.getID());
			if(commentDTO.getOwnerId().equals(context.getIdentity().getIdentityId())){
				repository.deleteComment(comment.getTaskID(), comment.getID());
			}
		}catch(ForeignKeyException e)
		{
			//Is the task legit
			try {
				if(taskRepo.getTask(comment.getTaskID()) == null){
					throw new exceptions.BadRequestException("No such task");
				}
			} catch (DALException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Is the user legit
			try {
				if(accountRepo.getAccount(comment.getOwner()) == null){
					throw new exceptions.UnauthorizedException("Unauthorized");
				}
			} catch (DALException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
