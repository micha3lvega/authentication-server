package com.telintel.services.go4clients.web.api.gateway.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Document
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationUrlAccess implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String url;
	private String appid;
	private String method;
	private List<String> rols;
	private List<String> scopes;
	private boolean autenticated;

	
}
