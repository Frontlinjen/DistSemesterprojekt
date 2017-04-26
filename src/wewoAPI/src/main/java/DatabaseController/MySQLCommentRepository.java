package DatabaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLCommentRepository implements CommentRepository{
	private final String GET_COMMENT = "SELECT * FROM Comments WHERE (taskId = ? AND commentId = ?);";
	private final String DELETE_COMMENT = "DELETE * FROM Comments WHERE (taskId = ? AND commentId = ?);";
	private final String CREATE_COMMENT = "INSERT INTO Comments(commentId, taskId, ownerId, text, date) VALUES (?, ?, ?, ?, ?);";
	private final String UPDATE_COMMENT = "UPDATE Comments SET  commentId = '?', taskId =  '?', ownerId = '?', text = '?', date = '?' WHERE (taskId = ? AND commentId = ?);";
	
	public MySQLCommentRepository() throws DALException{
		DatabaseConnector.RegisterStatement("GET_COMMENT", GET_COMMENT);
		DatabaseConnector.RegisterStatement("CREATE_COMMENT", CREATE_COMMENT);
		DatabaseConnector.RegisterStatement("UPDATE_COMMENT", UPDATE_COMMENT);
		DatabaseConnector.RegisterStatement("DELETE_COMMENT", DELETE_COMMENT);
	}
	
	
	public CommentDTO getComment(int taskId, int commentId) throws DALException {
		PreparedStatement statement;
		try {
			statement = DatabaseConnector.getPreparedStatement("GET_COMMENT");
			statement.setInt(1, taskId);
			statement.setInt(2, commentId);

			ResultSet rs = statement.executeQuery();

			if (!rs.first()) 
				throw new DALException("Task med id " + taskId + " har ikke en kommentar med id " + commentId);
			
			return generate(rs);
			
		}
		catch (SQLException e) {throw new DALException(e); }
	}

	public List<CommentDTO> getCommentList(int taskId) throws DALException {
		List<CommentDTO> list = new ArrayList<CommentDTO>();
		ResultSet rs = DatabaseConnector.doQuery("SELECT * FROM Comments;");
		try
		{
			while (rs.next()) 
			{
				CommentDTO comment = generate(rs);
				list.add(comment);
			}
		} 
		catch (SQLException e) { throw new DALException(e.getMessage()); }
		return list;
	}

	//(commentId, taskId, ownerId, text, date)
	public int createComment(CommentDTO com) throws DALException {
		try {
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("CREATE_COMMENT");
			statement.setInt(1, com.getID());
			statement.setInt(1, com.getTaskID());
			statement.setString(1, com.getOwnerId());
			statement.setString(1, com.getText());
			statement.setDate(1, com.getDate());

			int res = statement.executeUpdate();
			
			return res;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	//(commentId, taskId, ownerId, text, date)
	public int updateComment(CommentDTO com) throws DALException {
		try{
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("UPDATE_COMMENT");
			statement.setInt(1, com.getID());
			statement.setInt(1, com.getTaskID());
			statement.setString(1, com.getOwnerId());
			statement.setString(1, com.getText());
			statement.setDate(1, com.getDate());
			return statement.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

	public int deleteComment(int taskId, int commentId) throws DALException {
		try{
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("DELETE_COMMENT");
			statement.setInt(1, taskId);
			statement.setInt(2, commentId);
			return statement.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	private CommentDTO generate(ResultSet rs) throws SQLException
	{
		CommentDTO comment = new CommentDTO();
		comment.setText(rs.getString("Text"));
		comment.setDate(rs.getDate("Date"));
		comment.setOwnerId(rs.getString("OwnerId"));
		comment.setTaskID(rs.getInt("TaskID"));
		comment.setID(rs.getInt("ID"));
		return comment;
	}

}
