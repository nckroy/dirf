package net.nicoleroy.directory;

import java.util.List;

/**
 * @author nckroy
 *
 */
public class LDAPInfoElement {

	public String name;
	
	public List<String> values;
	
	public LDAPInfoElement(String key, List<String> vals) {
		this.name = key;
		this.values = vals;
	}
}
