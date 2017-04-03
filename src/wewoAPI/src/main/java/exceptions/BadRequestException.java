package exceptions;

public class BadRequestException extends Exception {

	private String message;
	
	public BadRequestException(String s){
		message = "BadRequest " + s;
		}
	
	public String getMessage() {
		return "BadRequest";
	}
}

