package net.nicoleroy.directory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nckroy
 *
 */
public class LDAPInfo {
	
	private Map<String, List<String>> attributeInfoMap = new HashMap<String, List<String>>();
	
	private List<LDAPInfoElement> elements = new ArrayList<LDAPInfoElement>();
	
	public List<LDAPInfoElement> getAttributeInfoElementList() {
		elements.clear();
		for (String key : attributeInfoMap.keySet()) {
			elements.add(new LDAPInfoElement(key, attributeInfoMap.get(key)));
		}
		return this.elements;
	}
	
	public Map<String, List<String>> addNameValuePair(String name, String value) {
		if(attributeInfoMap.containsKey(name)) {
			List<String> values = attributeInfoMap.get(name);
			if(!values.contains(value)) {
				values.add(value);
				attributeInfoMap.put(name, values);
			}
		}
		else {
			List<String> values = new ArrayList<String>();
			values.add(value);
			attributeInfoMap.put(name, values);
		}
		return attributeInfoMap;
	}
	
	public Map<String, List<String>> deleteNameValuePair(String name, String value) {
		if(attributeInfoMap.containsKey(name)) {
			List<String> values = attributeInfoMap.get(name);
			if(values.contains(value)) {
				values.remove(value);
				if(values.isEmpty()) {
					attributeInfoMap.remove(name);
				}
				else {
					attributeInfoMap.put(name, values);
				}
			}
		}
		return attributeInfoMap;
	}
}
