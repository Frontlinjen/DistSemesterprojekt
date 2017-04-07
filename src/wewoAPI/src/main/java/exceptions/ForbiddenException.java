package exceptions;

public class ForbiddenException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public ForbiddenException(String s){
		super("Forbidden " + s);
		}
}