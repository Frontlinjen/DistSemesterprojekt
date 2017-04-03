package exceptions;

public class NotFoundException extends Exception {
			
	private String message;
	
	public NotFoundException(String s){
		message = "NotFound " + s;
		}
	
	public String getMessage() {
		return "NotFound";
	}
}
