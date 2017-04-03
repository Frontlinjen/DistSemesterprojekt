package exceptions;

public class BadRequestException extends Exception {
	
	public BadRequestException(String s){
		super("BadRequest " + s);
		}
}