package com.ritchey.timesheet.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



// Generated Apr 6, 2009 2:53:47 PM by Hibernate Tools 3.2.2.GA


/**
 * FulltimeAgreements generated by hbm2java
 */
public class FulltimeAgreements implements java.io.Serializable {
	private static Log log = LogFactory.getLog(FulltimeAgreements.class);
	
	private Integer id;
	private People people;
	private Job job;
	private boolean active;
	private int rate;
	private int overtime;
	private boolean parttime;
	private String positionId;
	
	

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	@Override
	public String toString() {
		return "FulltimeAgreements [id=" + id  + ", active=" + active + ", rate=" + rate + ", overtime="
				+ overtime + ", parttime=" + parttime + ", positionId=" + positionId + "]";
	}

	public FulltimeAgreements() {
	}

	public FulltimeAgreements(Integer id, People people, Job job,
			boolean active, int rate, int overtime) {
		this.id = id;
		this.people = people;
		this.job = job;
		this.active = active;
		this.rate = rate;
		this.overtime = overtime;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public People getPeople() {
		return this.people;
	}

	public void setPeople(People people) {
		this.people = people;
	}

	public Job getJob() {
		return this.job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public boolean isActive() {
		return this.active;
	}

	public boolean getActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public int getRate() {
		return this.rate;
	}
	
	public String getRateFormatted() {
		return Constants.formatPennies(this.rate);
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getOvertime() {
		return this.overtime;
	}
	
	public String getOvertimeFormatted() {
		return Constants.formatPennies(this.getOvertime());
	}

	public void setOvertime(int overtime) {
		this.overtime = overtime;
	}

	public boolean isParttime() {
		return parttime;
	}

	public void setParttime(boolean parttime) {
		this.parttime = parttime;
		log.debug("set parttime to " + parttime);
	}

}
