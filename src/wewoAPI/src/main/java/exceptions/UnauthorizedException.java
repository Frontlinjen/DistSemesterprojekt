package exceptions;

public class UnauthorizedException extends Exception {
	
	private static final long serialVersionUID = -8710384907608499138L;
		
	public UnauthorizedException(String s){
		super("Unauthorized " + s);
		}
}