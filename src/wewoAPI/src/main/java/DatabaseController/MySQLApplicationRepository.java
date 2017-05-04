package DatabaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLApplicationRepository implements ApplicationRepository{

	private final String GET_APPLICATION = "SELECT * FROM Appliers WHERE TaskID = ? AND ApplierID = ?;";
	private final String CREATE_APPLICATION = "INSERT INTO Appliers(TaskID, ApplierID, appliermessage) VALUES (?, ?, ?);";
	private final String UPDATE_APPLICATION = "UPDATE Appliers SET  TaskID = ?, ApplierID =  ?, appliermessage = ?;";
	private final String GET_APPLICATION_LIST = "SELECT * FROM Appliers WHERE TaskID = ?;";
	private final String DELETE_APPLICATION = "DELETE FROM Application WHERE TaskID = ? AND ApplierID = ?;";
	
	public MySQLApplicationRepository() throws DALException{
		DatabaseConnector.RegisterStatement("GET_APPLICATION", GET_APPLICATION);
		DatabaseConnector.RegisterStatement("CREATE_APPLICATION", CREATE_APPLICATION);
		DatabaseConnector.RegisterStatement("UPDATE_APPLICATION", UPDATE_APPLICATION);
		DatabaseConnector.RegisterStatement("GET_APPLICATION_LIST", GET_APPLICATION_LIST);
		DatabaseConnector.RegisterStatement("DELETE_APPLICATION", DELETE_APPLICATION);
	}
	
	public ApplicationDTO getApplication(String applierID, int taskID) throws DALException {
		PreparedStatement statement;
		try {
			statement = DatabaseConnector.getPreparedStatement("GET_APPLICATION");
			statement.setInt(1, taskID);
			statement.setString(2, applierID);

			ResultSet rs = statement.executeQuery();

			if (!rs.first()) 
				return null;
			
			return generate(rs);
			
		}
		catch (SQLException e) {throw new DALException(e); }
	}

	public List<String> getApplicationList(int taskID) throws DALException {
		List<String> list = new ArrayList<String>();
		PreparedStatement statement;
		try
		{
			statement = DatabaseConnector.getPreparedStatement("GET_APPLICATION_LIST");
			statement.setInt(1,  taskID);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) 
			{
				list.add(generate(rs).getApplierid());
			}
		} 
		catch (SQLException e) { throw new DALException(e.getMessage()); }
		return list;
	}

	public int createApplication(ApplicationDTO app) throws DALException {
		try {
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("CREATE_APPLICATION");
			statement.setInt(1, app.taskid);
			statement.setString(2, app.applierID);
			statement.setString(3, app.applicationMessage);
			
			int res = statement.executeUpdate();
			return res;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int updateApplication(ApplicationDTO app) throws DALException {
		try{
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("UPDATE_APPLICATION");
			statement.setInt(1, app.taskid);
			statement.setString(2, app.applierID);
			statement.setString(3, app.applicationMessage);
			return statement.executeUpdate();
		
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

	public int deleteApplication(String applierID, int taskID) throws DALException {
		try{
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("DELETE_APPLICATION");
			statement.setInt(1, taskID);
			statement.setString(2, applierID);
			return statement.executeUpdate();
		
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public ApplicationDTO generate(ResultSet rs) throws SQLException
	{
		ApplicationDTO app = new ApplicationDTO();
		app.setTaskid(rs.getInt("TaskID")).setApplierid(rs.getString("ApplierID")).setApplicationMessage(rs.getString("appliermessage"));
		return app;
	}

}
