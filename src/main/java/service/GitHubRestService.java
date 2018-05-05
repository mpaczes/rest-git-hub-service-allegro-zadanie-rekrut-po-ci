package service;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import domain.GitHubRepositoryBean;
import domain.HttpProxyBean;
import exception.RestGitHubException;


@JsonIgnoreProperties(ignoreUnknown = true)
@Service("gitHubRestService")
public class GitHubRestService {
	
	private HttpProxyBean httpProxyBean;
	private OkHttpClient okHttpClient;
	private ObjectMapper objectMapper;
	
	public GitHubRestService() {
		// do nothing ..
	}
	
	public GitHubRestService(HttpProxyBean httpProxyBean, OkHttpClient okHttpClient, ObjectMapper objectMapper) {
		this.httpProxyBean = httpProxyBean;
		this.okHttpClient = okHttpClient;
		this.objectMapper = objectMapper;
	}
	
	// Using OkHttp for efficient network access :
	public String callURL(String myURL) {
		String stringResponse = "";
		try {
			if (!"empty".equals(this.httpProxyBean.getHttpProxyUrl()) && !"empty".equals(this.httpProxyBean.getHttpProxyPort())) {
				Proxy proxyTest = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.httpProxyBean.getHttpProxyUrl(), Integer.parseInt(this.httpProxyBean.getHttpProxyPort())));
				this.okHttpClient.setProxy(proxyTest);
			}
			
	        Request request = new Request.Builder().url(myURL).build();
	        Response response = this.okHttpClient.newCall(request).execute();
			stringResponse = response.body().string();
		} catch (Exception e) {
			RestGitHubException restGitHubException = new RestGitHubException("service.GitHubRestService", "callURL()");
			restGitHubException.setMessage("Original message from exception : " + e.getMessage() + ", and cause : " + e.getCause());
			throw restGitHubException;
		}
		
		return stringResponse;
	}
	
	public GitHubRepositoryBean parseGithubRepsonse(String gitHubApiCallResults) {
		GitHubRepositoryBean gitHubRepositoryBean = new GitHubRepositoryBean();
		try {
			JSONObject obj = new JSONObject(gitHubApiCallResults);
			
			JSONArray items = obj.getJSONArray("items");
			int numbersOfRepositories = items.length();
			
			// we expect only one repository to get
			if (numbersOfRepositories == 1) {
				JSONObject repository = items.getJSONObject(0);
				
				gitHubRepositoryBean = fillClassFieldsFromJsonRepository(repository);
				return gitHubRepositoryBean;
			}
		} catch(Exception e) {
			RestGitHubException restGitHubException = new RestGitHubException("service.GitHubRestService", "parseGithubRepsonse()");
			restGitHubException.setMessage("Original message from exception -- " + e.getMessage() + ", and cause : " + e.getCause());
			throw restGitHubException;
		}
		
		return gitHubRepositoryBean;
	}
	
	public GitHubRepositoryBean fillClassFieldsFromJsonRepository(JSONObject repository) {
		GitHubRepositoryBean gitHubRepositoryBean = new GitHubRepositoryBean();
		try {
			gitHubRepositoryBean = this.objectMapper.readValue(repository.toString(), GitHubRepositoryBean.class);
			gitHubRepositoryBean.changeCreatedAtToLocale(repository);
		} catch (Exception e) {
			RestGitHubException restGitHubException = new RestGitHubException("service.GitHubRestService", "fillClassFieldsFromJsonRepository()");
			restGitHubException.setMessage("Original message from exception : " + e.getMessage() + ", and cause  : " + e.getCause());
			throw restGitHubException;
		}
		return gitHubRepositoryBean;
	}
	
	public String buildResponseToShow(GitHubRepositoryBean gitHubRepositoryBean) {
		String jsonString = "";
		
		try {
			this.objectMapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
		
			jsonString = this.objectMapper.writeValueAsString(gitHubRepositoryBean);
			
			jsonString = replaceNamesOFBeanFields(jsonString);
		} catch (Exception e) {
			RestGitHubException restGitHubException = new RestGitHubException("service.GitHubRestService", "buildResponseToShow()");
			restGitHubException.setMessage("Original message from exception : " + e.getMessage() + ", and cause : " + e.getCause());
			throw restGitHubException;
		}
		
		return jsonString;
	}
	
	private String replaceNamesOFBeanFields(String jsonString) {
		JSONObject obj = new JSONObject(jsonString);
		
		obj.put("fullName", obj.getString("full_name"))
			.put("cloneUrl", obj.getString("clone_url"))
			.put("stargazersCount", obj.getInt("stargazers_count"))
			.put("createdAt", obj.getString("created_at"));
		
		obj.remove("full_name");
		obj.remove("stargazers_count");
		obj.remove("clone_url");
		obj.remove("created_at");
		
		return obj.toString();
	}

}
