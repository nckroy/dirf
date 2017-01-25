package net.nicholasroy.whitepages.controller;

import java.util.List;
import net.nicholasroy.directory.DirectoryPerson;
import net.nicholasroy.directory.LDAPFactory;
import net.nicholasroy.directory.LDAPInfo;
import net.nicholasroy.directory.LDAPUtils;
import org.ldaptive.LdapException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @author nick
 *
 * TODO: Add input validation via regex/JSR 303 validators, add complex search logic, 
 * secure with Spring Security
 * 
 * Add missing attributes to DirectoryPerson class, to prevent throwing stack traces in stdout during reflection in LDAPUtils
 * 
 * Add logging and put catches in log instead of stdout
 * 
 * Figure out how to change logging from DEBUG
 * 
 * Set focus on search field
 * 
 * Clear out search results (session-scoped bean) when an empty search is performed or /ldst is GETed without a uid param
 * 
 * Integrate with Tomcat in Eclipse and figure out how to deploy for debugging
 * 
 * Fix "appears to have started a thread but has failed to stop it" warnings from Tomcat on shutdown of the app
 * 
 * Fix SearchResult java.io.NotSerializableException
 * 
 * Add tests/mocks
 * 
 * Add common UI elements like the search box to template fragments
 * 
 * Create a navigation interface and include as a template fragment
 * 
 * Fix printing of null for null values in interfaces
 * 
 * Clear out session-scoped search result if no results found
 * 
 * Put &nbsp; between admin area and department in ldsr
 * 
 * Use user principal from session and groups from session (via Shibboleth) for custom security controller annotations:
 * @Self - self-service basic level of access (can operate on self only)
 * @Create("create-granting-ldap-group-name")
 * @Read("read-granting-ldap-group-name")
 * @Update("update-granting-ldap-group-name")
 * @Delete("delete-granting-ldap-group-name")
 * 
 * Database connection pooling
 * 
 * SSL/TLS security
 * 
 * log4net logging
 * 
 * Authorities Mapper implementation that maps LDAP groups to roles
 * 
 * Replace authN with Shibboleth SP
 * 
 * Cache UserDetails to prevent repeated LDAP lookups on authentication (for web services authN)
 * 
 * Separate basic authN for web services, separate from forms authN for interfaces
 * 
 * Explore converting from LDAPtive to Spring LDAP
 * 
 * Make UMGs into links which show membership.  On membership view, allow viewing of admins.
 * 
 **/ 
@Controller
@PropertySources(value = {@PropertySource("classpath:/controller.properties")})
public class LdapSearchController {

	private LDAPFactory factory = new LDAPFactory();

	private LDAPInfo ldapInfo = new LDAPInfo();

	@Autowired
	private SearchResult searchResult;

	@ModelAttribute("searchResults")
	private List<DirectoryPerson> getSearchResults() {
		return searchResult.getSearchResults();
	}

	@RequestMapping(method=RequestMethod.GET, value="/")
	public String home(Model model) {
		return "redirect:/ldst";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/ldst")
	public String ldst(@RequestParam(value="uid", required=false) String uid, Model model) {
		if((uid != null) && (!(uid.isEmpty()))) {
			StringBuilder sb = new StringBuilder("(uid=");
			sb.append(uid + ")");
			try {
				ldapInfo=factory.findSingleEntry(sb.toString());

			} catch (LdapException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		model.addAttribute("ldapInfo", ldapInfo.getAttributeInfoElementList());
		return "/ldst";
	}

	@RequestMapping(method=RequestMethod.POST, value="/ldst")
	public String ldstPost(@RequestParam(value="personToSearch", required=true) String searchString) {
		String redirectPath = "redirect:/ldst";
		if((searchString != null) && (!(searchString.isEmpty()))) {
			try {
				searchResult.setSearchResults(LDAPUtils.LDAPInfoListToDirectoryPersonList(factory.findEntriesByStringSearch(searchString)));
			} catch (LdapException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(searchResult.getSearchResults().size() == 1)
			{
				String sResultUid = null;
				sResultUid = searchResult.getSearchResults().get(0).getUid();
				redirectPath = (((sResultUid == null) || sResultUid.isEmpty()) ? "redirect:/ldst" : "redirect:/ldst?uid=" + sResultUid);
			}
			else if (searchResult.getSearchResults().size() > 1 ) {
				redirectPath = "redirect:/ldsr";
			}
			else {
				redirectPath = "redirect:/ldst";
			}
		}
		else {
			redirectPath = "redirect:/ldst";
		}
		return redirectPath;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/ldsr")
	public String ldsr(Model model) {
		if(searchResult.getSearchResults().size() > 1) {
			return "/ldsr";
		}
		else {
			return "redirect:/ldst";
		}
	}
}