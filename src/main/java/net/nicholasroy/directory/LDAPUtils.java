package net.nicholasroy.directory;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author nick
 *
 */
public class LDAPUtils {

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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
