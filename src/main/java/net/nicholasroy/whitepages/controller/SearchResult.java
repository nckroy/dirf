package net.nicholasroy.whitepages.controller;

import java.util.ArrayList;
import java.util.List;
import net.nicholasroy.directory.DirectoryPerson;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author nick
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
