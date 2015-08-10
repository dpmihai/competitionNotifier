package com.example.competitionnotifier;

import java.io.Serializable;
import java.io.StringWriter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MailData implements Serializable {
	
	private static final long serialVersionUID = -2020202922932937787L;
	
	private String competitionName;
	private String stageName;
	private User user;
	private int daysToStage;
	
	public MailData() {		
	}
	
	public String getCompetitionName() {
		return competitionName;
	}
	
	public void setCompetitionName(String competitionName) {
		this.competitionName = competitionName;
	}
	
	public String getStageName() {
		return stageName;
	}
	
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}	
	
	public int getDaysToStage() {
		return daysToStage;
	}

	public void setDaysToStage(int daysToStage) {
		this.daysToStage = daysToStage;
	}
	
	@Override
	public String toString() {
		return "MailData [competitionName=" + competitionName + ", stageName="
				+ stageName + ", user=" + user + ", daysToStage=" + daysToStage
				+ "]";
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((competitionName == null) ? 0 : competitionName.hashCode());
		result = prime * result + daysToStage;
		result = prime * result
				+ ((stageName == null) ? 0 : stageName.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MailData other = (MailData) obj;
		if (competitionName == null) {
			if (other.competitionName != null)
				return false;
		} else if (!competitionName.equals(other.competitionName))
			return false;
		if (daysToStage != other.daysToStage)
			return false;
		if (stageName == null) {
			if (other.stageName != null)
				return false;
		} else if (!stageName.equals(other.stageName))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();		
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, this);
			return writer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error : " + ex.getMessage();
		}
	}		

}
