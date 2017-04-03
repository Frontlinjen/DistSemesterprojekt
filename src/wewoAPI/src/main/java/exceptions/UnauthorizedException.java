package exceptions;

public class UnauthorizedException extends Exception {
	
	private static final long serialVersionUID = -8710384907608499138L;
	
	private String message;
	
	public UnauthorizedException(String s){
		message = "Unauthorized " + s;
		}
	
	@Override
	public String getMessage() {
		return "Unauthorized";
	}

}
