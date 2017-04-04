package exceptions;

public class InternalServerErrorException extends Exception {
	
	public InternalServerErrorException(String s){
		super("InternalServerError " + s);
		}
}
