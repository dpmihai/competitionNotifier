package com.example.competitionnotifier;

import java.io.Serializable;

public class User implements Serializable {
	
	private String username;
	private String team;
	private String email;
	private String phone;
	private String userTeamInitials;
	
	public User() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserTeamInitials() {
		return userTeamInitials;
	}

	public void setUserTeamInitials(String userTeamInitials) {
		this.userTeamInitials = userTeamInitials;
	}
	
	@Override
	public String toString() {
		return "User [username=" + username + ", team=" + team + ", email="
				+ email + ", phone=" + phone + ", userTeamInitials="
				+ userTeamInitials + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((team == null) ? 0 : team.hashCode());
		result = prime
				* result
				+ ((userTeamInitials == null) ? 0 : userTeamInitials.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		if (userTeamInitials == null) {
			if (other.userTeamInitials != null)
				return false;
		} else if (!userTeamInitials.equals(other.userTeamInitials))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	

}
