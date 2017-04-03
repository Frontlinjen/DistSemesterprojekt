package exceptions;

public class ForbiddenException extends Exception {
	
	private String message;
	
	public ForbiddenException(String s){
		message = "Forbidden " + s;
		}
	
	public String getMessage() {
		return "Forbidden";
	}
}
