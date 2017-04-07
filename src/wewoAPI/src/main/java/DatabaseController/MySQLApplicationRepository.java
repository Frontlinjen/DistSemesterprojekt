package DatabaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLApplicationRepository implements ApplicationRepository{

	private final String GET_APPLICATION = "SELECT * FROM Application WHERE TaskID = ?;";
	private final String CREATE_APPLICATION = "INSERT INTO Tasks(TaskID, ApplierID, appliermessage) VALUES (?, ?, ?);";
	private final String UPDATE_APPLICATION = "UPDATE Application SET  TaskID = '?', ApplierID =  '?', appliermessage = '?';";
	
	public MySQLApplicationRepository(){
		DatabaseConnector.RegisterStatement("GET_APPLICATION", GET_APPLICATION);
		DatabaseConnector.RegisterStatement("CREATE_APPLICATION", CREATE_APPLICATION);
		DatabaseConnector.RegisterStatement("UPDATE_APPLICATION", UPDATE_APPLICATION);
		
	}
	
	public ApplicationDTO getApplication(int id) throws DALException {
		PreparedStatement statement;
		try {
			statement = DatabaseConnector.getPreparedStatement("GET_APPLICATION");
			statement.setInt(1, id);

			ResultSet rs = statement.executeQuery();

			if (!rs.first()) 
				throw new DALException("Application med applierid " + id + " findes ikke");
			
			return generate(rs);
			
		}
		catch (SQLException e) {throw new DALException(e); }
	}

	public List<String> getApplicationList(int i) throws DALException {
		List<String> list = new ArrayList<String>();
		ResultSet rs = DatabaseConnector.doQuery("SELECT * FROM Appliers;");
		try
		{
			ApplicationDTO dto = new ApplicationDTO();
			String stringList = dto.applierid;
			while (rs.next()) 
			{
					list.add(stringList);
			}
		} 
		catch (SQLException e) { throw new DALException(e.getMessage()); }
		return list;
	}

	public int createApplication(ApplicationDTO app) throws DALException {
		try {
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("CREATE_APPLICATION");
			statement.setString(1, app.taskid);
			statement.setString(2, app.applierid);
			statement.setString(3, app.applicationMessage);
			
			int res = statement.executeUpdate();

			ResultSet rs = DatabaseConnector.doQuery("SELECT LAST_INSERT_ID();");
			rs.first();
			String ID = rs.getString("last_insert_id()");
			app.taskid = ID;
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
			statement.setString(1, app.taskid);
			statement.setString(2, app.applierid);
			statement.setString(3, app.applicationMessage);
			return statement.executeUpdate();
		
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

	public int deleteApplication(int id) throws DALException {
		return DatabaseConnector.doUpdate("DELETE FROM Application WHERE ApplierID = " + id + ";");
	}
	
	public ApplicationDTO generate(ResultSet rs) throws SQLException
	{
		ApplicationDTO app = new ApplicationDTO();
		app.setTaskid(rs.getString("TaskID")).setApplierid(rs.getString("ApplierID")).setApplicationMessage(rs.getString("appliermessage"));
		return app;
	}

}
