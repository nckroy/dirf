package net.nicoleroy.dirf.controller;

import java.util.List;
import net.nicoleroy.directory.DirectoryPerson;
import net.nicoleroy.directory.LDAPFactory;
import net.nicoleroy.directory.LDAPInfo;
import net.nicoleroy.directory.LDAPUtils;
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
 * @author nckroy
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
	public String ldst(@RequestParam(value="userid", required=false) String userid, Model model) {
		if((userid != null) && (!(userid.isEmpty()))) {
			StringBuilder sb = new StringBuilder("(userid=");
			sb.append(userid + ")");
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
				sResultUid = searchResult.getSearchResults().get(0).getUserid();
				redirectPath = (((sResultUid == null) || sResultUid.isEmpty()) ? "redirect:/ldst" : "redirect:/ldst?userid=" + sResultUid);
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
