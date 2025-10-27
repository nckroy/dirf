package net.nicoleroy.directory;

import java.util.ArrayList;
import java.util.List;

/**
 * Test fixtures for LDAP directory objects
 * @author nckroy
 */
public class TestFixtures {

    /**
     * Creates a sample DirectoryPerson with complete data
     */
    public static DirectoryPerson createSampleDirectoryPerson() {
        DirectoryPerson person = new DirectoryPerson();
        person.setUserid("jsmith");
        person.setGivenName("John");
        person.setSn("Smith");
        person.setDisplayName("Smith, John");
        person.setCn("John Smith");
        person.setMail("jsmith@example.com");
        person.setTelephoneNumber("555-1234");
        person.setMobile("555-5678");
        person.setTitle("Software Engineer");
        person.setPostalAddress("123 Main St, Building 5, Room 101");
        person.setEduPersonPrincipalName("jsmith@example.com");
        person.setEduPersonAffiliation("employee, member");
        person.setEduPersonPrimaryAffiliation("employee");
        person.setUidNumber("1001");
        person.setGidNumber("100");
        person.setHomeDirectory("/home/jsmith");
        person.setLoginShell("/bin/bash");
        person.setObjectClass("inetOrgPerson, eduPerson, posixAccount");
        return person;
    }

    /**
     * Creates a DirectoryPerson with minimal data
     */
    public static DirectoryPerson createMinimalDirectoryPerson() {
        DirectoryPerson person = new DirectoryPerson();
        person.setUserid("adoe");
        person.setGivenName("Alice");
        person.setSn("Doe");
        person.setMail("adoe@example.com");
        return person;
    }

    /**
     * Creates a DirectoryPerson representing a student
     */
    public static DirectoryPerson createStudentDirectoryPerson() {
        DirectoryPerson person = new DirectoryPerson();
        person.setUserid("bwilliams");
        person.setGivenName("Bob");
        person.setSn("Williams");
        person.setDisplayName("Williams, Bob");
        person.setMail("bwilliams@example.com");
        person.setTelephoneNumber("555-9999");
        person.setEduPersonAffiliation("student, member");
        person.setEduPersonPrimaryAffiliation("student");
        person.setEduPersonNickname("Bobby");
        return person;
    }

    /**
     * Creates a sample LDAPInfo object with typical person attributes
     */
    public static LDAPInfo createSampleLDAPInfo() {
        LDAPInfo info = new LDAPInfo();
        info.addNameValuePair("userid", "jsmith");
        info.addNameValuePair("givenName", "John");
        info.addNameValuePair("sn", "Smith");
        info.addNameValuePair("displayName", "Smith, John");
        info.addNameValuePair("cn", "John Smith");
        info.addNameValuePair("mail", "jsmith@example.com");
        info.addNameValuePair("telephoneNumber", "555-1234");
        info.addNameValuePair("mobile", "555-5678");
        info.addNameValuePair("title", "Software Engineer");
        info.addNameValuePair("postalAddress", "123 Main St");
        info.addNameValuePair("postalAddress", "Building 5");
        info.addNameValuePair("postalAddress", "Room 101");
        info.addNameValuePair("eduPersonPrincipalName", "jsmith@example.com");
        info.addNameValuePair("eduPersonAffiliation", "employee");
        info.addNameValuePair("eduPersonAffiliation", "member");
        info.addNameValuePair("eduPersonPrimaryAffiliation", "employee");
        info.addNameValuePair("uidNumber", "1001");
        info.addNameValuePair("gidNumber", "100");
        info.addNameValuePair("homeDirectory", "/home/jsmith");
        info.addNameValuePair("loginShell", "/bin/bash");
        info.addNameValuePair("objectClass", "inetOrgPerson");
        info.addNameValuePair("objectClass", "eduPerson");
        info.addNameValuePair("objectClass", "posixAccount");
        return info;
    }

    /**
     * Creates an LDAPInfo object with minimal attributes
     */
    public static LDAPInfo createMinimalLDAPInfo() {
        LDAPInfo info = new LDAPInfo();
        info.addNameValuePair("userid", "adoe");
        info.addNameValuePair("givenName", "Alice");
        info.addNameValuePair("sn", "Doe");
        info.addNameValuePair("mail", "adoe@example.com");
        return info;
    }

    /**
     * Creates a list of DirectoryPerson objects for testing search results
     */
    public static List<DirectoryPerson> createMultipleDirectoryPersons() {
        List<DirectoryPerson> persons = new ArrayList<>();

        DirectoryPerson person1 = new DirectoryPerson();
        person1.setUserid("jsmith");
        person1.setGivenName("John");
        person1.setSn("Smith");
        person1.setDisplayName("Smith, John");
        person1.setMail("jsmith@example.com");
        persons.add(person1);

        DirectoryPerson person2 = new DirectoryPerson();
        person2.setUserid("jsmith2");
        person2.setGivenName("Jane");
        person2.setSn("Smith");
        person2.setDisplayName("Smith, Jane");
        person2.setMail("jsmith2@example.com");
        persons.add(person2);

        DirectoryPerson person3 = new DirectoryPerson();
        person3.setUserid("jsmith3");
        person3.setGivenName("James");
        person3.setSn("Smith");
        person3.setDisplayName("Smith, James");
        person3.setMail("jsmith3@example.com");
        persons.add(person3);

        return persons;
    }

    /**
     * Creates a list of LDAPInfo objects for testing
     */
    public static List<LDAPInfo> createMultipleLDAPInfos() {
        List<LDAPInfo> infos = new ArrayList<>();

        LDAPInfo info1 = new LDAPInfo();
        info1.addNameValuePair("userid", "jsmith");
        info1.addNameValuePair("givenName", "John");
        info1.addNameValuePair("sn", "Smith");
        info1.addNameValuePair("mail", "jsmith@example.com");
        infos.add(info1);

        LDAPInfo info2 = new LDAPInfo();
        info2.addNameValuePair("userid", "jsmith2");
        info2.addNameValuePair("givenName", "Jane");
        info2.addNameValuePair("sn", "Smith");
        info2.addNameValuePair("mail", "jsmith2@example.com");
        infos.add(info2);

        LDAPInfo info3 = new LDAPInfo();
        info3.addNameValuePair("userid", "jsmith3");
        info3.addNameValuePair("givenName", "James");
        info3.addNameValuePair("sn", "Smith");
        info3.addNameValuePair("mail", "jsmith3@example.com");
        infos.add(info3);

        return infos;
    }

    /**
     * Creates a DirectoryPerson with special characters in names
     */
    public static DirectoryPerson createPersonWithSpecialCharacters() {
        DirectoryPerson person = new DirectoryPerson();
        person.setUserid("omullen");
        person.setGivenName("Seán");
        person.setSn("O'Mullen");
        person.setDisplayName("O'Mullen, Seán");
        person.setMail("omullen@example.com");
        return person;
    }

    /**
     * Creates an LDAPInfo object for a person with multiple phone numbers
     */
    public static LDAPInfo createLDAPInfoWithMultiplePhones() {
        LDAPInfo info = new LDAPInfo();
        info.addNameValuePair("userid", "mjones");
        info.addNameValuePair("givenName", "Mary");
        info.addNameValuePair("sn", "Jones");
        info.addNameValuePair("telephoneNumber", "555-1111");
        info.addNameValuePair("telephoneNumber", "555-2222");
        info.addNameValuePair("mobile", "555-3333");
        info.addNameValuePair("homePhone", "555-4444");
        info.addNameValuePair("fax", "555-5555");
        return info;
    }
}
