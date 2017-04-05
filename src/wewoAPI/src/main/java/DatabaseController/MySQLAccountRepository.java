package DatabaseController;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLAccountRepository implements AccountRepository{

	private final String GET_ACCOUNT = "SELECT * FROM Account WHERE id = ?;";
	private final String CREATE_ACCOUNT = "INSERT INTO Account(name, email, phone, sex, aboutme, competences)" +
										"VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
	private final String UPDATE_ACCOUNT = "UPDATE account SET  ID = '?', name =  '?', email = '?', phone = '?', sex = '?', aboutme " +
									   "= '?', competences = '?' WHERE ID = '?';";
	
	
	public MySQLAccountRepository(){
		DatabaseConnector.RegisterStatement("GET_ACCOUNT", GET_ACCOUNT);
		DatabaseConnector.RegisterStatement("CREATE_ACOUNT", CREATE_ACCOUNT);
		DatabaseConnector.RegisterStatement("UPDATE_ACCOUNT", UPDATE_ACCOUNT);
	}
		
	public AccountDTO getAccount(int id) throws DALException {
		PreparedStatement statement;
		try {
			statement = DatabaseConnector.getPreparedStatement("GET_ACCOUNT");
			statement.setInt(1, id);

			ResultSet rs = statement.executeQuery();

			if (!rs.first()) 
				throw new DALException("Account med id " + id + " findes ikke");
			
			return generate(rs);
			
		}
		catch (SQLException e) {throw new DALException(e); }
	}
	
	private AccountDTO generate(ResultSet rs) throws SQLException
	{
		AccountDTO acc = new AccountDTO();
		acc.setUserID(rs.getString("ID")).setName(rs.getString("name"))
		.setEmail(rs.getString("email")).setPhone(rs.getString("phone"))
		.setSex(rs.getBoolean("sex")).setAboutMe(rs.getString("aboutme"))
		.setCompetences((List<String>) rs.getArray("competences"));
		return acc;
	}

	public List<AccountDTO> queryAccount(List<Integer> tags) throws DALException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AccountDTO> getAccountList() throws DALException {
		List<AccountDTO> list = new ArrayList<AccountDTO>();
		ResultSet rs = DatabaseConnector.doQuery("SELECT * FROM Account;");
		try
		{
			while (rs.next()) 
			{
				AccountDTO account = generate(rs);
				list.add(account);
			}
		} 
		catch (SQLException e) { throw new DALException(e.getMessage()); }
		return list;
	}

	public int createAccount(AccountDTO acc) throws DALException {
		try {
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("CREATE_ACCOUNT");
			statement.setString(1, acc.name);
			statement.setString(2, acc.email);
			statement.setString(3,  acc.phone);
			statement.setBoolean(4, acc.sex);
			statement.setString(5, acc.aboutMe);
			statement.setArray(6, (Array) acc.competences);


			int res = statement.executeUpdate();


			ResultSet rs = DatabaseConnector.doQuery("SELECT LAST_INSERT_ID();");
			rs.first();
			String ID = rs.getString("last_insert_id()");
			acc.userID = ID;
			return res;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int updateAccount(AccountDTO acc) throws DALException {
		try{
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("CREATE_ACCOUNT");
			statement.setString(1, acc.name);
			statement.setString(2, acc.email);
			statement.setString(3,  acc.phone);
			statement.setBoolean(4, acc.sex);
			statement.setString(5, acc.aboutMe);
			statement.setArray(6, (Array) acc.competences);
			return statement.executeUpdate();
		
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

	public int deleteAccount(int id) throws DALException {
		return DatabaseConnector.doUpdate("DELETE FROM Account WHERE ID = " + id + ";");
	}

}
