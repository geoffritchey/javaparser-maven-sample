package com.ritchey.timesheet.model;

import java.util.Date;

// Generated Feb 6, 2009 11:36:48 AM by Hibernate Tools 3.2.2.GA


/**
 * CodeTerm generated by hbm2java
 */
public class Holiday implements java.io.Serializable {

	private Integer id;
	private Date date;
	private String description;
	private boolean applied;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDateFormatted() {
		return Constants.displayDate.format(date);
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isApplied() {
		return applied;
	}
	public void setApplied(boolean applied) {
		this.applied = applied;
	}

}