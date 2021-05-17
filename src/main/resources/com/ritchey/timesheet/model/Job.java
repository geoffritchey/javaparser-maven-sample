package com.ritchey.timesheet.model;

import java.util.HashSet;
import java.util.Set;

public class Job extends BaseObject implements Comparable<Job>  {
	@Override
	public String toString() {
		return "Job [id=" + id + ", department=" + department
				+ ", description=" + description + ", accountNumber="
				+ accountNumber + ", active=" + active + ", fulltime="
				+ fulltime + ", supervisors=" + supervisors + "]";
	}

	private Long id;
	private String department;
	private String description;
	private String accountNumber;
	private Boolean active;
	private Boolean fulltime;

	public Boolean getFulltime() {
		return fulltime;
	}

	public void setFulltime(Boolean fulltime) {
		this.fulltime = fulltime;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	private Set supervisors = new HashSet(0);
	
	public Job() {
		
	}
	
	public Job(String department) {
		this.department = department;
	}
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Set<Supervisor> getSupervisors() {
		return supervisors;
	}

	public void setSupervisors(Set supervisors) {
		this.supervisors = supervisors;
	}
	
	public boolean equals(Object job) {
		if (this == job)
			return true;
		if (!(job instanceof Job))
			return false;
		return (null == department ? false : department.equals(((Job)job).getDepartment()));
	}
	
	public int hashCode() {
		int hash = 7;
		int var_code = (null == department ? 0 : department.hashCode()); 
		hash = 31 * hash + var_code; 
		return hash;
	}

	@Override
	public int compareTo(Job o) {
		return description.compareTo(o.description);
	}
}
