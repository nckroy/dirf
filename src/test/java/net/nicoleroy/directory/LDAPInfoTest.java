package net.nicoleroy.directory;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Unit tests for LDAPInfo class
 * @author nckroy
 */
public class LDAPInfoTest {

    private LDAPInfo ldapInfo;

    @Before
    public void setUp() {
        ldapInfo = new LDAPInfo();
    }

    @Test
    public void testAddNameValuePair_SingleValue() {
        ldapInfo.addNameValuePair("givenName", "John");

        List<LDAPInfoElement> elements = ldapInfo.getAttributeInfoElementList();
        assertEquals("Should have 1 element", 1, elements.size());

        LDAPInfoElement element = elements.get(0);
        assertEquals("givenName", element.name);
        assertEquals(1, element.values.size());
        assertEquals("John", element.values.get(0));
    }

    @Test
    public void testAddNameValuePair_MultipleValues() {
        ldapInfo.addNameValuePair("objectClass", "inetOrgPerson");
        ldapInfo.addNameValuePair("objectClass", "eduPerson");
        ldapInfo.addNameValuePair("objectClass", "posixAccount");

        List<LDAPInfoElement> elements = ldapInfo.getAttributeInfoElementList();
        assertEquals("Should have 1 element", 1, elements.size());

        LDAPInfoElement element = elements.get(0);
        assertEquals("objectClass", element.name);
        assertEquals(3, element.values.size());
        assertTrue("Should contain inetOrgPerson", element.values.contains("inetOrgPerson"));
        assertTrue("Should contain eduPerson", element.values.contains("eduPerson"));
        assertTrue("Should contain posixAccount", element.values.contains("posixAccount"));
    }

    @Test
    public void testAddNameValuePair_DuplicateValueIgnored() {
        ldapInfo.addNameValuePair("mail", "test@example.com");
        ldapInfo.addNameValuePair("mail", "test@example.com");

        List<LDAPInfoElement> elements = ldapInfo.getAttributeInfoElementList();
        assertEquals("Should have 1 element", 1, elements.size());

        LDAPInfoElement element = elements.get(0);
        assertEquals("mail", element.name);
        assertEquals("Duplicate value should be ignored", 1, element.values.size());
        assertEquals("test@example.com", element.values.get(0));
    }

    @Test
    public void testAddNameValuePair_MultipleAttributes() {
        ldapInfo.addNameValuePair("userid", "jsmith");
        ldapInfo.addNameValuePair("givenName", "John");
        ldapInfo.addNameValuePair("sn", "Smith");
        ldapInfo.addNameValuePair("mail", "jsmith@example.com");

        List<LDAPInfoElement> elements = ldapInfo.getAttributeInfoElementList();
        assertEquals("Should have 4 elements", 4, elements.size());
    }

    @Test
    public void testDeleteNameValuePair_SingleValue() {
        ldapInfo.addNameValuePair("title", "Engineer");
        ldapInfo.deleteNameValuePair("title", "Engineer");

        List<LDAPInfoElement> elements = ldapInfo.getAttributeInfoElementList();
        assertEquals("Should have 0 elements", 0, elements.size());
    }

    @Test
    public void testDeleteNameValuePair_MultipleValues() {
        ldapInfo.addNameValuePair("telephoneNumber", "555-1111");
        ldapInfo.addNameValuePair("telephoneNumber", "555-2222");
        ldapInfo.addNameValuePair("telephoneNumber", "555-3333");

        ldapInfo.deleteNameValuePair("telephoneNumber", "555-2222");

        List<LDAPInfoElement> elements = ldapInfo.getAttributeInfoElementList();
        assertEquals("Should have 1 element", 1, elements.size());

        LDAPInfoElement element = elements.get(0);
        assertEquals("telephoneNumber", element.name);
        assertEquals("Should have 2 values remaining", 2, element.values.size());
        assertTrue("Should contain 555-1111", element.values.contains("555-1111"));
        assertTrue("Should contain 555-3333", element.values.contains("555-3333"));
        assertFalse("Should not contain 555-2222", element.values.contains("555-2222"));
    }

    @Test
    public void testDeleteNameValuePair_NonExistentAttribute() {
        ldapInfo.addNameValuePair("userid", "jsmith");
        ldapInfo.deleteNameValuePair("title", "Engineer");

        List<LDAPInfoElement> elements = ldapInfo.getAttributeInfoElementList();
        assertEquals("Should still have 1 element", 1, elements.size());
    }

    @Test
    public void testDeleteNameValuePair_NonExistentValue() {
        ldapInfo.addNameValuePair("mail", "test@example.com");
        ldapInfo.deleteNameValuePair("mail", "other@example.com");

        List<LDAPInfoElement> elements = ldapInfo.getAttributeInfoElementList();
        assertEquals("Should have 1 element", 1, elements.size());
        assertEquals("Value should be unchanged", "test@example.com", elements.get(0).values.get(0));
    }

    @Test
    public void testGetAttributeInfoElementList_EmptyInfo() {
        List<LDAPInfoElement> elements = ldapInfo.getAttributeInfoElementList();

        assertNotNull("Elements list should not be null", elements);
        assertEquals("Should have 0 elements", 0, elements.size());
    }

    @Test
    public void testGetAttributeInfoElementList_ClearsExistingElements() {
        ldapInfo.addNameValuePair("userid", "jsmith");

        List<LDAPInfoElement> elements1 = ldapInfo.getAttributeInfoElementList();
        List<LDAPInfoElement> elements2 = ldapInfo.getAttributeInfoElementList();

        // Both calls should return fresh lists with the same data
        assertEquals(1, elements1.size());
        assertEquals(1, elements2.size());
    }

    @Test
    public void testComplexScenario() {
        // Build a complex LDAPInfo object
        ldapInfo.addNameValuePair("userid", "jsmith");
        ldapInfo.addNameValuePair("givenName", "John");
        ldapInfo.addNameValuePair("sn", "Smith");
        ldapInfo.addNameValuePair("mail", "jsmith@example.com");
        ldapInfo.addNameValuePair("objectClass", "inetOrgPerson");
        ldapInfo.addNameValuePair("objectClass", "eduPerson");
        ldapInfo.addNameValuePair("telephoneNumber", "555-1111");
        ldapInfo.addNameValuePair("telephoneNumber", "555-2222");

        // Remove one telephone number
        ldapInfo.deleteNameValuePair("telephoneNumber", "555-2222");

        List<LDAPInfoElement> elements = ldapInfo.getAttributeInfoElementList();
        assertEquals("Should have 6 attributes", 6, elements.size());

        // Verify telephoneNumber has only one value
        LDAPInfoElement phoneElement = null;
        for (LDAPInfoElement element : elements) {
            if ("telephoneNumber".equals(element.name)) {
                phoneElement = element;
                break;
            }
        }

        assertNotNull("Should find telephoneNumber element", phoneElement);
        assertEquals("Should have 1 phone number", 1, phoneElement.values.size());
        assertEquals("555-1111", phoneElement.values.get(0));
    }
}
