package controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("test-DispatcherServlet-context.xml")
@WebAppConfiguration
public class GitHubRestControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void checkDetailsOfGivenGithubRepository() throws Exception {		
		this.mockMvc.perform(get("/repositories/mpaczes/hello-world"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$fullName", is("mpaczes/hello-world")))
			.andExpect(jsonPath("$stargazersCount", is(0)))
			.andExpect(jsonPath("$cloneUrl", is("https://github.com/mpaczes/hello-world.git")))
			.andExpect(status().is(200));
	}
	
	@Test
	public void checkHandleError() throws Exception  {
		this.mockMvc.perform(get("/repositories/mpaczeserror/hello-world"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$className", is("service.GitHubRestService")))
			.andExpect(jsonPath("$methodName", is("parseGithubRepsonse()")))
			.andExpect(jsonPath("$exceptionMessage", containsString("JSONObject[\"items\"] not found")))
			.andExpect(jsonPath("$requestedUrl", containsString("repositories/mpaczeserror/hello-world")))
			.andExpect(status().is(200))
			;
	}

}
