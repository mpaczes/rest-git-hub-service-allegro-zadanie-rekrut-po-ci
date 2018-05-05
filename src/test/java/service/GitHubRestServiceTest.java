package service;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import domain.GitHubRepositoryBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("test-DispatcherServlet-context.xml")
@WebAppConfiguration
public class GitHubRestServiceTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	private GitHubRestService gitHubRestService;
	private String owner;
	private String repositoryName;
	private String gitHubApiCallResults;
	
	@Before
	public void setUp() {
		this.gitHubRestService = ((GitHubRestService) this.wac.getBean("gitHubRestService"));
		this.owner = "mpaczes";
		this.repositoryName = "hello-world";
		
		this.gitHubApiCallResults = gitHubRestService.callURL("https://api.github.com/search/repositories?q=" + this.repositoryName + "+user:" + this.owner);
	}
	
	@Test
	public void checkParseGithubRepsonse() {
		GitHubRepositoryBean gitHubRepositoryBean = this.gitHubRestService.parseGithubRepsonse(gitHubApiCallResults);
		
		assertThat("mpaczes/hello-world", is(gitHubRepositoryBean.getFull_name()));
		
		assertThat("https://github.com/mpaczes/hello-world.git", is(gitHubRepositoryBean.getClone_url()));
	}
	
	@Test
	public void checkFillClassFieldsFromJsonRepository() {
		// (1)
		JSONObject obj = new JSONObject(this.gitHubApiCallResults);
		
		JSONArray items = obj.getJSONArray("items");
		
		int numbersOfRepositories = items.length();
		
		assertThat(1, is(numbersOfRepositories));
		
		// (2)
		JSONObject repository = items.getJSONObject(0);
		
		GitHubRepositoryBean localGitHubRepositoryBean = this.gitHubRestService.fillClassFieldsFromJsonRepository(repository);
		
		assertThat("mpaczes/hello-world", anyOf(notNullValue(), is(localGitHubRepositoryBean.getFull_name())));
	}
	
	@Test
	public void checkBuildResponseToShow() {
		GitHubRepositoryBean gitHubRepositoryBean = new GitHubRepositoryBean();
		gitHubRepositoryBean.setFull_name("mpaczes/hello-world");
		
		String responseToShow = this.gitHubRestService.buildResponseToShow(gitHubRepositoryBean);
		
		JSONObject obj = new JSONObject(responseToShow);
		
		assertThat("mpaczes/hello-world", is(obj.getString("fullName")));
	}

}
