package domain;

public class HttpProxyBean {

	private String httpProxyUrl;
	private String httpProxyPort;
	
	public HttpProxyBean(String httpProxyUrl, String httpProxyPort) {
		this.httpProxyUrl = httpProxyUrl;
		this.httpProxyPort = httpProxyPort;
	}
	
	public String getHttpProxyUrl() {
		return httpProxyUrl;
	}
	
	public String getHttpProxyPort() {
		return httpProxyPort;
	}
	
}
