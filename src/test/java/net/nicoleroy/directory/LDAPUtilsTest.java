package net.nicoleroy.directory;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for LDAPUtils class
 * @author nckroy
 */
public class LDAPUtilsTest {

    private List<LDAPInfo> sampleLDAPInfoList;

    @Before
    public void setUp() {
        sampleLDAPInfoList = new ArrayList<>();
        sampleLDAPInfoList.add(TestFixtures.createSampleLDAPInfo());
        sampleLDAPInfoList.add(TestFixtures.createMinimalLDAPInfo());
    }

    @Test
    public void testLDAPInfoListToDirectoryPersonList_WithValidData() {
        List<DirectoryPerson> result = LDAPUtils.LDAPInfoListToDirectoryPersonList(sampleLDAPInfoList);

        assertNotNull("Result should not be null", result);
        assertEquals("Should have 2 persons", 2, result.size());

        // Test first person (sample)
        DirectoryPerson person1 = result.get(0);
        assertEquals("jsmith", person1.getUserid());
        assertEquals("John", person1.getGivenName());
        assertEquals("Smith", person1.getSn());
        assertEquals("jsmith@example.com", person1.getMail());

        // Test second person (minimal)
        DirectoryPerson person2 = result.get(1);
        assertEquals("adoe", person2.getUserid());
        assertEquals("Alice", person2.getGivenName());
        assertEquals("Doe", person2.getSn());
        assertEquals("adoe@example.com", person2.getMail());
    }

    @Test
    public void testLDAPInfoListToDirectoryPersonList_WithEmptyList() {
        List<LDAPInfo> emptyList = new ArrayList<>();
        List<DirectoryPerson> result = LDAPUtils.LDAPInfoListToDirectoryPersonList(emptyList);

        assertNotNull("Result should not be null", result);
        assertEquals("Result should be empty", 0, result.size());
    }

    @Test
    public void testGetConcatAttrValueString_WithSingleValue() {
        List<String> values = new ArrayList<>();
        values.add("value1");

        String result = LDAPUtils.getConcatAttrValueString(values);

        assertEquals("value1", result);
    }

    @Test
    public void testGetConcatAttrValueString_WithMultipleValues() {
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        values.add("value3");

        String result = LDAPUtils.getConcatAttrValueString(values);

        assertEquals("value1, value2, value3", result);
    }

    @Test
    public void testGetConcatAttrValueString_WithEmptyList() {
        List<String> values = new ArrayList<>();

        String result = LDAPUtils.getConcatAttrValueString(values);

        assertEquals("", result);
    }

    @Test
    public void testLDAPInfoListToDirectoryPersonList_WithMultiValueAttribute() {
        LDAPInfo info = TestFixtures.createLDAPInfoWithMultiplePhones();
        List<LDAPInfo> list = new ArrayList<>();
        list.add(info);

        List<DirectoryPerson> result = LDAPUtils.LDAPInfoListToDirectoryPersonList(list);

        assertNotNull("Result should not be null", result);
        assertEquals("Should have 1 person", 1, result.size());

        DirectoryPerson person = result.get(0);
        assertEquals("mjones", person.getUserid());

        // Multi-valued telephoneNumber should be concatenated
        String phones = person.getTelephoneNumber();
        assertNotNull("Phone numbers should not be null", phones);
        assertTrue("Should contain first number", phones.contains("555-1111"));
        assertTrue("Should contain second number", phones.contains("555-2222"));
    }

    @Test
    public void testLDAPInfoListToDirectoryPersonList_WithSpecialCharacters() {
        LDAPInfo info = new LDAPInfo();
        info.addNameValuePair("userid", "omullen");
        info.addNameValuePair("givenName", "Seán");
        info.addNameValuePair("sn", "O'Mullen");

        List<LDAPInfo> list = new ArrayList<>();
        list.add(info);

        List<DirectoryPerson> result = LDAPUtils.LDAPInfoListToDirectoryPersonList(list);

        assertNotNull("Result should not be null", result);
        assertEquals("Should have 1 person", 1, result.size());

        DirectoryPerson person = result.get(0);
        assertEquals("Seán", person.getGivenName());
        assertEquals("O'Mullen", person.getSn());
    }
}
