package DatabaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MySQLTagsRepository implements TagsRepository{

	private final String GET_TAGS = "SELECT * FROM Tags;";
	
	public MySQLTagsRepository() throws DALException{
		DatabaseConnector.RegisterStatement("GET_TAGS", GET_TAGS);
	}
	
	public HashMap<String, Integer> getTags() throws DALException {
		HashMap<String, Integer> tagsList = new HashMap<String, Integer>();
		PreparedStatement statement;
		try
		{
			statement = DatabaseConnector.getPreparedStatement("GET_TAGS");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) 
			{
				tagsList.put(rs.getString(2), rs.getInt(1));
			}
		} 
		catch (SQLException e) { throw new DALException(e.getMessage()); }
		return tagsList;
	}
	
	
}
