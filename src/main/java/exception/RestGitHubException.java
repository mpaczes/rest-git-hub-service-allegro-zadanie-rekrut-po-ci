package exception;

public class RestGitHubException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private String className;
	private String methodName;
	private String message = "empty message entry";
	
	public RestGitHubException(String className, String methodName) {
		super();
		this.className = className;
		this.methodName = methodName;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
