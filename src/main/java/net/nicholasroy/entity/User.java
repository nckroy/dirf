package net.nicholasroy.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_test_users", schema="public")
public class User {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long person_id;
	private String uid;
    private String fname;
    private String mnames;
    private String lname;

    protected User() {}

    public User(String uid, String fname, String mnames, String lname) {
        this.uid = uid;
        this.fname = fname;
        this.mnames = mnames;
        this.lname = lname;
    }

    /**
	 * @return the person_id
	 */
	public long getPerson_id() {
		return person_id;
	}

	/**
	 * @param person_id the person_id to set
	 */
	public void setPerson_id(long person_id) {
		this.person_id = person_id;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the fname
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * @param fname the fname to set
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}

	/**
	 * @return the mnames
	 */
	public String getMnames() {
		return mnames;
	}

	/**
	 * @param mnames the mnames to set
	 */
	public void setMnames(String mnames) {
		this.mnames = mnames;
	}

	/**
	 * @return the lname
	 */
	public String getLname() {
		return lname;
	}

	/**
	 * @param lname the lname to set
	 */
	public void setLname(String lname) {
		this.lname = lname;
	}

	@Override
    public String toString() {
        return String.format(
                "User[person_id=%d, uid='%s', fname='%s', mnames='%s', lname='%s']",
                person_id, uid, fname, mnames, lname);
    }
    
}