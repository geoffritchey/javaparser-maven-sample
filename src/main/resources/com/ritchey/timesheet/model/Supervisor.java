package com.ritchey.timesheet.model;


public class Supervisor implements java.io.Serializable {
	private Integer id = null;
	private Job job = null;
	private Integer fulltime = 0;
	private PeopleProperties people = null;
	
	public Supervisor() {
		
	}
	
	public Supervisor(PeopleProperties people) {
		this.people = people;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public Integer getFulltime() {
		return fulltime;
	}
	public void setFulltime(Integer fulltime) {
		this.fulltime = fulltime;
	}
	
	public String getFulltimeString() {
		switch (fulltime) {
		case Constants.FULLTIME: return "Employees";
		case Constants.STUDENT: return "Students";
		case Constants.BOTH: return "All";
		}
		return "Neither";
	}
	
	public int toggle() {
		switch (fulltime) {
		case Constants.FULLTIME: return Constants.STUDENT;
		case Constants.STUDENT: return Constants.BOTH;
		case Constants.BOTH: return Constants.FULLTIME;
		}
		return Constants.STUDENT;
	}
	
	public boolean equals(Object s) {
		if (this == s)
			return true;
		if (s == null)
			return false;
		if (!(s instanceof Supervisor))
			return false;
		Supervisor supervisor = (Supervisor) s;
		if (people == null) {
			return supervisor.getPeople() == null;
		}
		if (job == null) {
			return people.getPeopleId().equals(supervisor.getPeople().getPeopleId());
		}
		return people.equals(supervisor.getPeople()) &&
				job.equals(supervisor.getJob());
	}
	
	public int hashCode() {
		int hash = 7;
		int var_code = (null == people ? 0 : people.getPeopleId().hashCode()); 
		hash = 31 * hash + var_code; 
		var_code = (null == job ? 0 : job.hashCode()); 
		hash = 31 * hash + var_code; 
		return hash;
	}

	public PeopleProperties getPeople() {
		return people;
	}

	public void setPeople(PeopleProperties people) {
		this.people = people;
	}
}
