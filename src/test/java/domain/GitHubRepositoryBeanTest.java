package domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.codehaus.jackson.map.ObjectMapper;
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

import service.GitHubRestService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("test-DispatcherServlet-context.xml")
@WebAppConfiguration
public class GitHubRepositoryBeanTest {
	
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
	public void checkChangeCreatedAtToLocaleSuccess() throws Exception {
		GitHubRepositoryBean gitHubRepositoryBean = new GitHubRepositoryBean();
		
		JSONObject obj = new JSONObject(this.gitHubApiCallResults);
		
		JSONArray items = obj.getJSONArray("items");
		
		JSONObject repository = items.getJSONObject(0);
		
		ObjectMapper mapper = new ObjectMapper();
		gitHubRepositoryBean = mapper.readValue(repository.toString(), GitHubRepositoryBean.class);
		
		gitHubRepositoryBean.changeCreatedAtToLocale(repository);
		
		assertThat(true, is(gitHubRepositoryBean.getCreated_at().contains("sobota")));
		System.out.println("Bean's field 'createdAt' contains string 'sobota'");
	}
	
	@Test
	public void checkChangeCreatedAtToLocaleFailure() {
		GitHubRepositoryBean gitHubRepositoryBean = new GitHubRepositoryBean();

		try {
			JSONObject obj = new JSONObject(this.gitHubApiCallResults);
			
			JSONArray items = obj.getJSONArray("items_err");
			
			JSONObject repository = items.getJSONObject(0);
			
			ObjectMapper mapper = new ObjectMapper();
			gitHubRepositoryBean = mapper.readValue(repository.toString(), GitHubRepositoryBean.class);
			
			gitHubRepositoryBean.changeCreatedAtToLocale(repository);
		} catch (Exception e) {
			assertThat(true, not(gitHubRepositoryBean.getCreated_at().contains("sobota")));
			System.out.println("Bean's field 'createdAt' does not contain string 'sobota', because : " + e.getMessage());
		}
	}

}
