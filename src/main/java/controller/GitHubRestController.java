package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import service.GitHubRestService;
import domain.GitHubRepositoryBean;
import domain.GitHubRepositoryBeanForException;
import exception.RestGitHubException;


@RestController("gitHubRestController")
@RequestMapping("/repositories")
public class GitHubRestController {
	
	@Autowired
	@Qualifier("gitHubRestService")
	private GitHubRestService gitHubRestService;
	
	private String gitHubLink;
	
	public GitHubRestController() {
		// do nothing ..
	}
	
	public GitHubRestController(String gitHubLink) {
		this.gitHubLink = gitHubLink;
	}
	
	/**
	 * Here is example how to invoke this method :
	 * <br />
	 * http://localhost:8080/github-repo/restservice/repositories/mpaczesn/test
	 */
	@RequestMapping(value = "/{owner}/{repositoryName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String detailsOfGivenGithubRepository(@PathVariable(value = "owner") String owner, 
			@PathVariable(value = "repositoryName") String repositoryName) {
		
		String gitHubApiCallResults = gitHubRestService.callURL(this.gitHubLink + "?q=" + repositoryName + "+user:" + owner);
		
		GitHubRepositoryBean gitHubRepositoryBean = gitHubRestService.parseGithubRepsonse(gitHubApiCallResults);
		
		return gitHubRestService.buildResponseToShow(gitHubRepositoryBean);
	}
	
	@ExceptionHandler(RestGitHubException.class)
	public GitHubRepositoryBeanForException handleError(HttpServletRequest req, RestGitHubException exception) {
		GitHubRepositoryBeanForException gitHubRepositoryBeanForException = new GitHubRepositoryBeanForException();
		
		gitHubRepositoryBeanForException.setRequestedUrl(req.getRequestURL() + "?" + req.getQueryString());
		gitHubRepositoryBeanForException.setClassName(exception.getClassName());
		gitHubRepositoryBeanForException.setMethodName(exception.getMethodName());
		gitHubRepositoryBeanForException.setExceptionMessage(exception.getMessage());
		
		return gitHubRepositoryBeanForException;
	}

}
