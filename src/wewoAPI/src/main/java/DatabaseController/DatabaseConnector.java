package DatabaseController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import java.sql.PreparedStatement;

class PreparedStatementWrapper{
	private PreparedStatement statement;
	private String query;
	
	public PreparedStatementWrapper(String query)
	{
		this.query = query;
	}
	
	public void generateStatement(Connection db) throws SQLException
	{
		statement = db.prepareStatement(query);
	}
	public PreparedStatement get(){
		return statement;
	}
	
}

public class DatabaseConnector {
	/**
	 * To connect to a MySQL-server
	 * 
	 * @param url must have the form
	 * "jdbc:mysql://<server>/<database>" for default port (3306)
	 * OR
	 * "jdbc:mysql://<server>:<port>/<database>" for specific port
	 * more formally "jdbc:subprotocol:subname"
	 * 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 */
	public static Connection connectToDatabase(String url, String username, String password)
			throws InstantiationException, IllegalAccessException,
					ClassNotFoundException, SQLException
	{
		// call the driver class' no argument constructor
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		// get Connection-object via DriverManager
		return DriverManager.getConnection(url, username, password);
	}
	
	private static Connection conn;
	private static Statement stm;
	private static HashMap<String, PreparedStatementWrapper> statements;
	
	public static void RegisterStatement(String identifier, String query) throws DALException
	{
		connector();
		if(!statements.containsKey(identifier))
		{
			PreparedStatementWrapper statement = new PreparedStatementWrapper(query);
			statements.put(identifier, statement);
		}
	}
	
	public DatabaseConnector(String server, int port, String database,
			String username, String password)
				throws InstantiationException, IllegalAccessException,
					ClassNotFoundException, SQLException
	{
		conn	= connectToDatabase("jdbc:mysql://"+server+":"+port+"/"+database,
					username, password);
		stm		= conn.createStatement();
		statements = new HashMap<String, PreparedStatementWrapper>();
	}
	
	public DatabaseConnector() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException
	{
		this(Constant.server, Constant.port, Constant.database,
				Constant.username, Constant.password);
	}
	
	public static PreparedStatement getPreparedStatement(String identifier) throws SQLException
	{
		PreparedStatementWrapper ret = statements.get(identifier);
		if(ret != null)
		{
			if(ret.get() != null){
				return ret.get();
			}
			else{
				ret.generateStatement(conn);
				return ret.get();
			}
		}
		return null;
	}
	
	public static ResultSet doQuery(String cmd) throws DALException
	{
		connector();
		try { return stm.executeQuery(cmd); }
		catch (SQLException e) { throw new DALException(e); }
	}
	
	public static int doUpdate(String cmd) throws DALException
	{
		connector();
		try { return stm.executeUpdate(cmd); }
		catch (SQLException e) { throw new DALException(e); }
	}

	private static void connector() throws DALException {
		try
		{
			if(conn==null)
			{
				new DatabaseConnector();
			}
		}
		catch(Exception e)
		{
			throw new DALException(e.getMessage());
		}
	}
}
