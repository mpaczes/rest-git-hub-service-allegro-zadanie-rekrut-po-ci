package domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class GitHubRepositoryBeanTestWithoutConnection {
	
	private JSONObject outputFromGitHubApi;
	private ObjectMapper mapper;
	
	@Before
	public void setUp() {
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
		
		this.mapper = new ObjectMapper();
	}
	
	@Test
	public void checkChangeCreatedAtToLocaleSuccess() throws Exception {
		GitHubRepositoryBean gitHubRepositoryBean = new GitHubRepositoryBean();
		
		JSONObject obj = new JSONObject(this.outputFromGitHubApi.toString());
		
		JSONArray items = obj.getJSONArray("items");
		
		JSONObject repository = items.getJSONObject(0);
		
		gitHubRepositoryBean = this.mapper.readValue(repository.toString(), GitHubRepositoryBean.class);
		
		gitHubRepositoryBean.changeCreatedAtToLocale(repository);
		
		assertThat(true, is(gitHubRepositoryBean.getCreated_at().contains("sobota")));
		System.out.println("bean's field 'createdAt' contains string 'sobota'");
	}
	
	@Test
	public void checkChangeCreatedAtToLocaleFailure() {
		GitHubRepositoryBean gitHubRepositoryBean = new GitHubRepositoryBean();

		try {
			JSONObject obj = new JSONObject(this.outputFromGitHubApi.toString());
			
			JSONArray items = obj.getJSONArray("items_err");
			
			JSONObject repository = items.getJSONObject(0);
			
			gitHubRepositoryBean = this.mapper.readValue(repository.toString(), GitHubRepositoryBean.class);
			
			gitHubRepositoryBean.changeCreatedAtToLocale(repository);
		} catch (Exception e) {
			assertThat(true, not(gitHubRepositoryBean.getCreated_at().contains("sobota")));
			System.out.println("Bean's field 'createdAt' does not contain string 'sobota', because : " + e.getMessage());
		}
	}

}
