package DatabaseController;

import java.sql.SQLException;

public class DALException extends Exception{
	
	public static class EntryNullException extends DALException{ //TODO Fix infinite loops
		public EntryNullException(SQLException exception) {
			super("Unspecified value", exception);
		}
	}
	
	public static class ForeignKeyException extends DALException{
		public ForeignKeyException(SQLException exception) {
			super("Parent does not exist.", exception);
		}
	}
	
	private static final long serialVersionUID = 1L;
	public static final int FOREIGN_KEY_CONSTRAINT = 1452;
	public static final int CANNOT_BE_NULL = 1048;
	public static String message;
	
	public DALException(String message, SQLException cause)
	{
		super(message, cause);
	}
	
	public DALException(String message){
		super(message);
	}
	public DALException(SQLException exception) throws EntryNullException, ForeignKeyException{
		switch(exception.getErrorCode()){
		case CANNOT_BE_NULL:
				throw new EntryNullException(exception);
		case FOREIGN_KEY_CONSTRAINT:
				throw new ForeignKeyException(exception);
		}
	}
}