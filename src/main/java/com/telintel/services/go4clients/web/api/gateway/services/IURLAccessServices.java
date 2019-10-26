package com.telintel.services.go4clients.web.api.gateway.services;

import java.util.List;

import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationUrlAccess;
import com.telintel.telintelutils.security.AuthenticationUrlAccessDTO;

public interface IURLAccessServices {

	public List<AuthenticationUrlAccessDTO> findAll();
	
	public List<AuthenticationUrlAccessDTO> findByAppid(String appid);
	
	public AuthenticationUrlAccessDTO insert(AuthenticationUrlAccess access) throws Exception;
	
	public List<AuthenticationUrlAccessDTO> findByRolsAndAppid(List<String> rols, String appid);

}
