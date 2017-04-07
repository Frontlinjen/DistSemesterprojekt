package DatabaseController;

import java.sql.SQLException;

public class MySQLException extends Exception{
	class EntryNullException extends MySQLException{
		public EntryNullException(SQLException exception) {
			super(exception);
		}

		super.message = "Value needs to be inserted.";
	}
	
	class ForeignKeyException extends MySQLException{
		public ForeignKeyException(SQLException exception) {
			super(exception);
		}

		super.message = "Parent does not exist.";
	}
	
	private static final long serialVersionUID = 1L;
	public static final int FOREIGN_KEY_CONSTRAINT = 1452;
	public static final int CANNOT_BE_NULL = 1048;
	
	public MySQLException(SQLException exception){
		switch(exception.getErrorCode()){
		
		case CANNOT_BE_NULL:
			try {
				throw new EntryNullException();
			} catch (EntryNullException e) {
				e.printStackTrace();
			}
	
		case FOREIGN_KEY_CONSTRAINT:
			try {
				throw new ForeignKeyException();
			} catch (ForeignKeyException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}