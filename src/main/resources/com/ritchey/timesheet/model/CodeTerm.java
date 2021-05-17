package com.ritchey.timesheet.model;

import java.util.HashSet;
import java.util.Set;

public class CodeTerm implements java.io.Serializable {

	private Integer termId;
	private String term;
	private Set awardeds = new HashSet(0);
	private Set payPers = new HashSet(0);

	public CodeTerm() {
	}

	public CodeTerm(String term, Set awardeds, Set payPers) {
		this.term = term;
		this.awardeds = awardeds;
		this.payPers = payPers;
	}

	public String getTerm() {
		return this.term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Set getAwardeds() {
		return this.awardeds;
	}

	public void setAwardeds(Set awardeds) {
		this.awardeds = awardeds;
	}

	public Set getPayPers() {
		return this.payPers;
	}

	public void setPayPers(Set payPers) {
		this.payPers = payPers;
	}

	public Integer getTermId() {
		return termId;
	}

	public void setTermId(Integer termId) {
		this.termId = termId;
	}

}
