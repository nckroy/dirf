package net.nicoleroy.dirf.controller;

import java.util.ArrayList;
import java.util.List;
import net.nicoleroy.directory.DirectoryPerson;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author nckroy
 *
 */
@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SearchResult {
	
private List<DirectoryPerson> searchResults = new ArrayList<DirectoryPerson>();
	
	public void setSearchResults(List<DirectoryPerson> results) {
		this.searchResults = results;
	}
	
	public List<DirectoryPerson> getSearchResults() {
		return this.searchResults;
	}
}
