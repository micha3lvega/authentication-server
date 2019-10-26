package com.telintel.services.go4clients.web.api.gateway.services;

import java.util.List;

import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationClientDetails;
import com.telintel.utils.exceptionmanagementutils.Go4ClientsServiceException;

public interface IMongoClientDetailsService {

	/**
	 * Find all client details configurate
	 * 
	 * @return list {@link AuthenticationClientDetails}
	 * @throws Go4ClientsServiceException
	 */
	public List<AuthenticationClientDetails> findAll() throws Go4ClientsServiceException;

	/**
	 * Find a {@link AuthenticationClientDetails} for id
	 * 
	 * @param id
	 * @return {@link AuthenticationClientDetails}
	 * @throws Go4ClientsServiceException
	 */
	public AuthenticationClientDetails findById(String id) throws Go4ClientsServiceException;

	/**
	 * create a new {@link AuthenticationClientDetails}
	 * 
	 * @param clientDetails
	 * @return {@link AuthenticationClientDetails}
	 * @throws Go4ClientsServiceException
	 */
	public AuthenticationClientDetails insert(AuthenticationClientDetails clientDetails) throws Go4ClientsServiceException;

	/**
	 * Update de {@link AuthenticationClientDetails} recevided
	 * 
	 * @param clientDetails
	 * @return {@link AuthenticationClientDetails}
	 * @throws Go4ClientsServiceException
	 */
	public AuthenticationClientDetails update(AuthenticationClientDetails clientDetails) throws Go4ClientsServiceException;

}
