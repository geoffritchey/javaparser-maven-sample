package com.ritchey.timesheet.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class PeopleProperties implements People, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4903369615844939988L;
	/**
	 * 
	 */
	
	private Integer id;
	private String peopleId;
	private String lastName;
	private String firstName;
	private String middleName;
	private String governmentId;
	private Boolean active = true;
	private Set fulltimeAgreements = new HashSet(0);
	private String dynamicId;
	private String passcode;
	private Long workRegion;

	@Override
	public String toString() {
		return "PeopleProperties [id=" + id + "\\n, peopleId=" + peopleId + "\\n, lastName=" + lastName
				+ "\\n, firstName=" + firstName + "\\n, middleName=" + middleName + "\\n, governmentId=" + governmentId
				+ "\\n, fulltimeAgreements=" + fulltimeAgreements + "\\n, dynamicId=" + dynamicId + "\\n, passcode=" + passcode + "]";
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public int getAgreementSize() {
		if (fulltimeAgreements == null)
			return 0;
		return fulltimeAgreements.size();
	}

	public PeopleProperties() {
		
	}
	
	public PeopleProperties(PeopleBasic p) {
		if (p == null) 
			return;
		this.peopleId = p.getPeopleId();
		this.lastName = p.getLastName();
		this.firstName = p.getFirstName();
		this.middleName = p.getMiddleName();
		this.governmentId = p.getGovernmentId();
	}
	
	public String getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getGovernmentId() {
		return governmentId;
	}

	public void setGovernmentId(String governmentId) {
		this.governmentId = governmentId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set getFulltimeAgreements() {
		return fulltimeAgreements;
	}

	public void setFulltimeAgreements(Set fulltimeAgreements) {
		this.fulltimeAgreements = fulltimeAgreements;
	}
	
	public String getDynamicId() {
		return dynamicId;
	}
	
	public void setDynamicId(String dynamicId) {
		this.dynamicId = dynamicId;
	}
	

	public String getPasscode() {
		return passcode;
	}

	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}
	
	public Long getWorkRegion() {
		return workRegion;
	}

	public void setWorkRegion(Long workRegion) {
		this.workRegion = workRegion;
	}

	public boolean equals(Object s) {
		if (this == s)
			return true;
		if (s == null)
			return false;
		if (!(s instanceof PeopleProperties))
			return false;
		PeopleProperties people = (PeopleProperties) s;
		if (peopleId == null) {
			return people.getPeopleId() == null;
		}
		return peopleId.equals(people.getPeopleId());
	}
	
	public int hashCode() {
		int hash = 7;
		int var_code = (null == peopleId ? 0 : peopleId.hashCode()); 
		hash = 31 * hash + var_code; 
		return hash;
	}
}
