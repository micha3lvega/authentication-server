package com.telintel.services.go4clients.web.api.gateway.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationUser;
import com.telintel.telintelutils.security.AuthenticationUserDTO;
import com.telintel.telintelutils.security.ResetPasswordDTO;
import com.telintel.utils.exceptionmanagementutils.Go4ClientsServiceException;

@Qualifier("com.telintel.services.go4clients.web.api.gateway.services.IUserServices")
public interface IUserServices {

	List<AuthenticationUser> findAll() throws Go4ClientsServiceException;

	void delete(String id) throws Go4ClientsServiceException;

	AuthenticationUserDTO insert(AuthenticationUser user) throws Go4ClientsServiceException;

	AuthenticationUser update(AuthenticationUser user) throws Go4ClientsServiceException;

	List<AuthenticationUser> findByAppid(String appid) throws Go4ClientsServiceException;

	AuthenticationUser findByUsername(String userName) throws Go4ClientsServiceException;

	AuthenticationUser findByapiKey(String apikey) throws Go4ClientsServiceException;

	void delete(String appid, String username) throws Go4ClientsServiceException;

	AuthenticationUser findByUsername(String appid, String username) throws Go4ClientsServiceException;

	ResetPasswordDTO resetPassword(String appid, String username) throws Go4ClientsServiceException;

	AuthenticationUser findByLogin(String user, String password, String appid) throws Go4ClientsServiceException;

	AuthenticationUser findByAccesToken(String apiKey, String apiSecret, String appId)
			throws Go4ClientsServiceException;

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
