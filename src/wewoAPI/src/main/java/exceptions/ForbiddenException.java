package exceptions;

public class ForbiddenException extends Exception {
	
	public ForbiddenException(String s){
		super("Forbidden " + s);
		}
}