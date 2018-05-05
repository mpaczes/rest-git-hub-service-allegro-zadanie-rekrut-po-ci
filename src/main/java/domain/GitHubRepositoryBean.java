package domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.json.JSONObject;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepositoryBean {
	private static final String EMPTY_STRING_GIT_HUB_ENTRY = "empty Git Hub entry";
	private static final Integer EMPTY_INTEGER_GIT_HUB_ENTRY = -1;
	
	private String fullName = EMPTY_STRING_GIT_HUB_ENTRY;
	private String description = EMPTY_STRING_GIT_HUB_ENTRY;
	private String cloneUrl = EMPTY_STRING_GIT_HUB_ENTRY;
	private Integer stargazersCount = EMPTY_INTEGER_GIT_HUB_ENTRY;
	private String createdAt = EMPTY_STRING_GIT_HUB_ENTRY;
	
	public String getFull_name() {
		return fullName;
	}
	public void setFull_name(String full_name) {
		this.fullName = full_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getClone_url() {
		return cloneUrl;
	}
	public void setClone_url(String clone_url) {
		this.cloneUrl = clone_url;
	}
	public Integer getStargazers_count() {
		return stargazersCount;
	}
	public void setStargazers_count(Integer stargazers_count) {
		this.stargazersCount = stargazers_count;
	}
	public String getCreated_at() {
		return createdAt;
	}
	public void setCreated_at(String created_at) {
		this.createdAt = created_at;
	}
	
	public void changeCreatedAtToLocale(JSONObject jsonObject) {
		String createdAtTemp = jsonObject.getString("created_at");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		
		try {
			date = simpleDateFormat.parse(createdAtTemp);
		} catch (ParseException e) {
			date = new Date();
		}
		
		Locale locale = new Locale("pl");
		DateFormat formatterLocale = DateFormat.getDateInstance(DateFormat.FULL, locale);
		String localeDateString = formatterLocale.format(date);
		
		this.createdAt = localeDateString;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.fullName == null) ? 0 : this.fullName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GitHubRepositoryBean other = (GitHubRepositoryBean) obj;
		if (this.fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!this.fullName.equals(other.fullName))
			return false;
		return true;
	}

}
