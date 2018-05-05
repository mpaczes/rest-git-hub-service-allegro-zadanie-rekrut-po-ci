package service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import domain.GitHubRepositoryBean;

public class GitHubRestServiceTestWithoutConnection {
	
	private ObjectMapper objectMapper;
	private GitHubRestService gitHubRestService;
	private JSONObject outputFromGitHubApi;

	@Before
	public void setUp() {
		this.objectMapper = new ObjectMapper();
		this.gitHubRestService = new GitHubRestService(null, null, this.objectMapper);
		
		this.outputFromGitHubApi = new JSONObject();
		
		this.outputFromGitHubApi.put("total_count", 1);
		this.outputFromGitHubApi.put("incomplete_results", false);
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.put((new JSONObject())
				.put("full_name", "mpaczes/hello-world")
				.put("created_at", "2016-09-24T21:01:57Z")
				.put("stargazers_count", 0)
				.put("clone_url", "https://github.com/mpaczes/hello-world.git")
				.put("description", "Repozytroium utworzone na podstawie tutoriala ze strony https://guides.github.com/activities/hello-world/."));
		
		this.outputFromGitHubApi.put("items", jsonArray);
	}
	
	@Test
	public void checkParseGithubRepsonseWithoutConnection() {
		GitHubRepositoryBean gitHubRepositoryBean = gitHubRestService.parseGithubRepsonse(this.outputFromGitHubApi.toString());
		
		assertThat(gitHubRepositoryBean.getFull_name(), is("mpaczes/hello-world"));
		assertThat(gitHubRepositoryBean.getClone_url(), is("https://github.com/mpaczes/hello-world.git"));
	}
	
	@Test
	public void checkBuildResponseToShow() {
		GitHubRepositoryBean gitHubRepositoryBean = new GitHubRepositoryBean();
		gitHubRepositoryBean.setFull_name("mpaczes/hello-world");
		gitHubRepositoryBean.setClone_url("https://github.com/mpaczes/hello-world.git");
		
		String responseToShow = gitHubRestService.buildResponseToShow(gitHubRepositoryBean);
		
		JSONObject jsonObject = new JSONObject(responseToShow);
		assertThat(jsonObject.getString("fullName"), is("mpaczes/hello-world"));
		assertThat(jsonObject.getString("cloneUrl"), is("https://github.com/mpaczes/hello-world.git"));
	}

}
