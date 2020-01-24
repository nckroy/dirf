package net.nicholasroy.directory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.ldaptive.Connection;
import org.ldaptive.DefaultConnectionFactory;
import org.ldaptive.LdapAttribute;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;
import org.ldaptive.SearchOperation;
import org.ldaptive.SearchRequest;
import org.ldaptive.SearchResult;
import org.ldaptive.pool.PooledConnectionFactory;
import org.ldaptive.pool.SoftLimitConnectionPool;

/**
 * @author nick
 *
 */

public class LDAPFactory {

	private SoftLimitConnectionPool pool;
	private PooledConnectionFactory connFactory = null;
	private Config conf = ConfigFactory.load();
	private String searchBase;

	public LDAPFactory() {

		String directoryURL = conf.getString("directoryURL");
		searchBase = conf.getString("searchBase");
		pool = new SoftLimitConnectionPool(new DefaultConnectionFactory(directoryURL));
		pool.initialize();
		connFactory = new PooledConnectionFactory(pool);
	}

	public LDAPInfo findSingleEntry(String lDAPSearchFilterString) throws LdapException,Exception {

		LDAPInfo result = new LDAPInfo();

		Connection conn = connFactory.getConnection();

		try {
			// connection is already open, perform an operation
			SearchOperation search = new SearchOperation(conn);
			SearchResult sResult = search.execute(new SearchRequest(searchBase, lDAPSearchFilterString)).getResult();
			if(sResult.size() > 1) {
				throw new Exception("Search result contains more than one entry.");
			}
			else if(sResult.size() == 1) {
				LdapEntry lde = sResult.getEntry();
				result = lDAPInfoFromLDAPEntry(lde);
			}
		} finally {
			// closing a connection returns it to the pool
			conn.close();
		}

		return result;
	}

	public List<LDAPInfo> findEntriesByStringSearch(String searchString) throws LdapException {
		List<LDAPInfo> results = new ArrayList<LDAPInfo>();

		Boolean wildcards = searchString.contains("*");

		// Remove all '(' and ')' and all escape sequence char '\'  from filter data.

		String cleanName = "";
		for (int i = 0; i < searchString.length(); i++)
		{
			if ((searchString.charAt(i) != ')') && (searchString.charAt(i) != '(') && (searchString.charAt(i) != '\\'))
			{
				cleanName += searchString.charAt(i);
			}
		}

		String name1 = cleanName;
		String name2 = "";
		String name3 = "";
		String firstName = "";
		String lastName = "";
		String originalName = name1;
		String[] nameSplit = cleanName.split(" ", 3);

		// Name is specified as "first last"
		// Rewrite as "last, first";

		if (nameSplit.length == 3)
		{
			if (originalName.contains(","))
			{
				lastName = nameSplit[0].replace(",", "");
				firstName = nameSplit[1];
				name1 = originalName;
			}
			else
			{
				lastName = nameSplit[2];
				firstName = nameSplit[0];
				name1 = lastName + ", " + nameSplit[0] + " " + nameSplit[1];
				name2 = nameSplit[1] + " " + nameSplit[2];
				name3 = nameSplit[2] + ' ' + nameSplit[1];
			}
		}
		else if (nameSplit.length == 2)
		{
			if (originalName.contains(","))
			{
				lastName = nameSplit[0].replace(",", "");
				firstName = nameSplit[1];
				name1 = originalName;
			}
			else
			{
				firstName = nameSplit[0];
				lastName = nameSplit[1];
				name1 = lastName + ", " + firstName;
			}
		}
		else if (nameSplit.length == 1)
		{
			lastName = nameSplit[0];
			name1 = lastName;
		}

		name1 = name1.trim();
		name2 = name2.trim();
		name3 = name3.trim();
		firstName = firstName.trim();
		lastName = lastName.trim();
		originalName = originalName.trim();

		List<String> filters = new ArrayList<String>();

		if (name1.contains(" "))
		{
			// Build simple search filters    

			if ((name2.length() > 0) && (name3.length() > 0) && !wildcards)
			{
				// Filter for a more complex search using the displayName attribute.

				filters.add("(|(displayName=" + name2 + "*)(displayName=" + name3 + "*))");
			}

			// Filters that are combinations of the name attributes.

			if ((originalName.length() > 0) && (name1.length() > 0))
			{
				filters.add("(|(sn=" + originalName + ")(displayName=" + name1 + "))");
			}
			if ((lastName.length() > 0) && (firstName.length() > 0))
			{
				filters.add("(&(sn=" + lastName + ")(givenName=" + firstName + "))");

				// Filter to include a last name and first name search.

				if (!wildcards)
				{
					filters.add("(&(sn=*" + lastName + "*)(givenName=*" + firstName + "*))");
				}
			}
			else if (lastName.length() > 0)
			{
				// Filter to include a last name search.

				filters.add("(sn=" + lastName + ")");
			}
			if ((!wildcards) && (originalName.length() > 0))
			{
				// Filter to catch displayNames that contain blanks

				filters.add("(displayName=*" + originalName + "*)");
			}
		}
		else if (name1.length() > 0)
		{
			// For a single token assume we either have an
			// Account ID or a last name.

			// We only have a single value with which to generate searches.
			// Make some assumptions about what the value is.


			// userid attribute is included in this search filter.
			filters.add("(|(userid=" + name1 + ")(sn=" + name1 + "))");

			// Filter to include a mail alias search.
			filters.add("(mail=" + name1 + ")");

			if (!wildcards)
			{
				filters.add("(sn=*" + name1 + "*)");
				filters.add("(givenName=*" + name1 + "*)");
			}
		}
		
		Connection conn = connFactory.getConnection();

		try {
			// connection is already open, perform an operation
			// TODO!!!! Un-hard-wire search base! Replace with searchBase
			SearchOperation search = new SearchOperation(conn);
			SearchResult sResult = search.execute(
					new SearchRequest(
							"dc=example,dc=com", orFiltersTogether(filters))).getResult();
			Collection<LdapEntry> entries = sResult.getEntries();
			for (LdapEntry entry : entries) {
				results.add(lDAPInfoFromLDAPEntry(entry));
			}
		} finally {
			// closing a connection returns it to the pool
			conn.close();
		}

		return results;
	}

	private LDAPInfo lDAPInfoFromLDAPEntry(LdapEntry entry) {
		LDAPInfo result = new LDAPInfo();

		List<String> attributeNames = new ArrayList<String>();

		attributeNames = Arrays.asList(entry.getAttributeNames());

		for (String name : attributeNames) {
			LdapAttribute attr = entry.getAttribute(name);
			if(!attr.isBinary()) {
				Collection<String> vals = attr.getStringValues();
				for (String val : vals) {
					result.addNameValuePair(name, val);
				}
			}
		}

		return result;
	}
	
	private String orFiltersTogether(List<String> filters) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("(|");
		for (String filter : filters) {
			sb.append(filter);
		}
		sb.append(")");
		
		return sb.toString();
	}
}
