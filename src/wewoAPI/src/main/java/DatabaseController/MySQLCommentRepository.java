package DatabaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLCommentRepository implements CommentRepository{
	//TODO: Fix statements
	private final String GET_COMMENT = "SELECT * FROM Tasks WHERE id = ?;";
	private final String CREATE_COMMENT = "INSERT INTO Tasks(title, description, price, ECT, supplies, urgent"
									 + "street, zipcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
	private final String UPDATE_COMMENT = "UPDATE ansat SET  ID = '?', title =  '?', description = '?', price = '?', ECT = '?', supplies " +
									   "= '?', urgent = '?', street = '?', zipcode = '?' WHERE ID = '?';";
	public MySQLCommentRepository(){
		DatabaseConnector.RegisterStatement("GET_COMMENT", GET_COMMENT);
		DatabaseConnector.RegisterStatement("CREATE_COMMENT", CREATE_COMMENT);
		DatabaseConnector.RegisterStatement("UPDATE_COMMENT", UPDATE_COMMENT);
		
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
		ResultSet rs = DatabaseConnector.doQuery("SELECT * FROM Tasks;"); //TODO FIX
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

	public int createComment(CommentDTO com) throws DALException {
		try {
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("CREATE_COMMENT");
			statement.setString(1, com.getText());
			statement.setInt(2, com.getOwnerId());
			statement.setDate(3, com.getDate());

			int res = statement.executeUpdate();
			
			//TODO Not implementedyet
			/*
			ResultSet rs = DatabaseConnector.doQuery("SELECT LAST_INSERT_ID();");
			rs.first();
			int ID = rs.getInt("last_insert_id()");
			com.id = ID; */
			return res;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int updateComment(CommentDTO com) throws DALException {
		try{
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("UPDATE_COMMENT");
			statement.setString(1, com.getText());
			statement.setInt(2, com.getOwnerId());
			statement.setDate(3, com.getDate());
			return statement.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

	public int deleteComment(int taskId, int commentId) throws DALException {
		return DatabaseConnector.doUpdate("DELETE FROM Tasks WHERE ID = " + id + ";"); //TODO Fix
	}
	
	private CommentDTO generate(ResultSet rs) throws SQLException
	{
		CommentDTO comment = new CommentDTO();
		comment.setText(rs.getString("Text")).setDate(rs.getDate("Date")).setOwnerId(rs.getInt("OwnerId"));
		return comment;
	}

}
