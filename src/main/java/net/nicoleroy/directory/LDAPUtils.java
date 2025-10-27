package net.nicoleroy.directory;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nckroy
 *
 */
public class LDAPUtils {

	private static final Logger logger = LoggerFactory.getLogger(LDAPUtils.class);

	public static List<DirectoryPerson> LDAPInfoListToDirectoryPersonList(List<LDAPInfo> convertList) {
		List<DirectoryPerson> results = new ArrayList<DirectoryPerson>();
		
		for (LDAPInfo info : convertList) {
			DirectoryPerson dp = new DirectoryPerson();
			List<LDAPInfoElement> elements = info.getAttributeInfoElementList();
			for (LDAPInfoElement element : elements) {
				StringBuilder eNameBuilder = new StringBuilder();
				String eName = element.name;
				Character c = eName.charAt(0);
				Character C = Character.toUpperCase(c);
				eName = eName.replaceFirst(c.toString(), C.toString());
				eNameBuilder.append("set");
				eNameBuilder.append(eName);
				try {
					Class[] params = new Class[1];
					params[0] = String.class;
					Method setter = dp.getClass().getMethod(eNameBuilder.toString(), params);
					setter.invoke(dp, getConcatAttrValueString(element.values));
				} catch (NoSuchMethodException e) {
					logger.debug("No setter method found for LDAP attribute: {}", element.name);
				} catch (SecurityException e) {
					logger.error("Security exception accessing setter for attribute: {}", element.name, e);
				} catch (IllegalAccessException e) {
					logger.error("Illegal access exception setting attribute: {}", element.name, e);
				} catch (IllegalArgumentException e) {
					logger.error("Illegal argument exception setting attribute: {}", element.name, e);
				} catch (InvocationTargetException e) {
					logger.error("Invocation target exception setting attribute: {}", element.name, e);
				}
			}
			results.add(dp);
		}
		
		return results;
	}
	
	public static String getConcatAttrValueString(List<String> values) {
		StringBuilder sb = new StringBuilder();
		for (String val : values) {
			sb.append(val + ", ");
		}
		if(sb.length() > 2) {
			sb.replace(sb.length() - 2, sb.length(), "");
		}
		return sb.toString();
	}
}
