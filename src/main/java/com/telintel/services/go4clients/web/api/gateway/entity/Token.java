package com.telintel.services.go4clients.web.api.gateway.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.*;

public class Token implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long exp;
	private String userName;
	private String[] authorities;
	private String jti;
	private String clientID;
	private String[] scope;

	@JsonProperty("exp")
	public long getExp() {
		return exp;
	}

	@JsonProperty("exp")
	public void setExp(long value) {
		this.exp = value;
	}

	@JsonProperty("user_name")
	public String getUserName() {
		return userName;
	}

	@JsonProperty("user_name")
	public void setUserName(String value) {
		this.userName = value;
	}

	@JsonProperty("authorities")
	public String[] getAuthorities() {
		return authorities;
	}

	@JsonProperty("authorities")
	public void setAuthorities(String[] value) {
		this.authorities = value;
	}

	@JsonProperty("jti")
	public String getJti() {
		return jti;
	}

	@JsonProperty("jti")
	public void setJti(String value) {
		this.jti = value;
	}

	@JsonProperty("client_id")
	public String getClientID() {
		return clientID;
	}

	@JsonProperty("client_id")
	public void setClientID(String value) {
		this.clientID = value;
	}

	@JsonProperty("scope")
	public String[] getScope() {
		return scope;
	}

	@JsonProperty("scope")
	public void setScope(String[] value) {
		this.scope = value;
	}
}
