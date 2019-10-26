package com.telintel.services.go4clients.web.api.gateway.services.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.filter;
import static com.google.common.collect.Sets.newHashSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.telintel.services.go4clients.web.api.gateway.config.client.detail.MongoClientDetailsRepository;
import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationClientDetails;
import com.telintel.services.go4clients.web.api.gateway.services.IMongoClientDetailsService;
import com.telintel.services.go4clients.web.api.gateway.util.AuthenticationServerErrorCodes;
import com.telintel.utils.exceptionmanagementutils.Go4ClientsServiceException;

@Component
public class MongoClientDetailsService
		implements ClientDetailsService, ClientRegistrationService, IMongoClientDetailsService {

	private final PasswordEncoder passwordEncoder;
	private final MongoClientDetailsRepository mongoClientDetailsRepository;

	@Autowired
	public MongoClientDetailsService(final MongoClientDetailsRepository mongoClientDetailsRepository,
			final BCryptPasswordEncoder passwordEncoder) {
		this.mongoClientDetailsRepository = mongoClientDetailsRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

		try {

			AuthenticationClientDetails mongoClientDetails = mongoClientDetailsRepository.findByClientId(clientId);

			BaseClientDetails client = new BaseClientDetails(mongoClientDetails.getClientId(),
					Joiner.on(",").join(mongoClientDetails.getResourceIds()),
					Joiner.on(",").join(mongoClientDetails.getScope()),
					Joiner.on(",").join(mongoClientDetails.getAuthorizedGrantTypes()),
					Joiner.on(",").join(mongoClientDetails.getAuthorities()),
					Joiner.on(",").join(mongoClientDetails.getRegisteredRedirectUri()));

			client.setAccessTokenValiditySeconds(mongoClientDetails.getAccessTokenValiditySeconds());
			client.setRefreshTokenValiditySeconds(mongoClientDetails.getRefreshTokenValiditySeconds());
			client.setClientSecret(mongoClientDetails.getClientSecret());

			return client;
		} catch (IllegalArgumentException e) {
			throw new ClientRegistrationException("No Client Details for client id", e);
		}
	}

	@Override
	public void addClientDetails(final ClientDetails clientDetails) throws ClientAlreadyExistsException {

		final AuthenticationClientDetails mongoClientDetails = new AuthenticationClientDetails(clientDetails.getClientId(),
				passwordEncoder.encode(clientDetails.getClientSecret()), clientDetails.getScope(),
				clientDetails.getResourceIds(), clientDetails.getAuthorizedGrantTypes(),
				clientDetails.getRegisteredRedirectUri(), newArrayList(clientDetails.getAuthorities()),
				clientDetails.getAccessTokenValiditySeconds(), clientDetails.getRefreshTokenValiditySeconds(),
				clientDetails.getAdditionalInformation(), null);

		mongoClientDetailsRepository.save(mongoClientDetails);
	}

	@Override
	public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {

		final AuthenticationClientDetails mongoClientDetails = new AuthenticationClientDetails(clientDetails.getClientId(),
				clientDetails.getClientSecret(), clientDetails.getScope(), clientDetails.getResourceIds(),
				clientDetails.getAuthorizedGrantTypes(), clientDetails.getRegisteredRedirectUri(),
				newArrayList(clientDetails.getAuthorities()), clientDetails.getAccessTokenValiditySeconds(),
				clientDetails.getRefreshTokenValiditySeconds(), clientDetails.getAdditionalInformation(),
				getAutoApproveScopes(clientDetails));
		final boolean result = mongoClientDetailsRepository.update(mongoClientDetails);

		if (!result) {
			throw new NoSuchClientException("No such Client Id");
		}
	}

	@Override
	public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {

		final boolean result = mongoClientDetailsRepository.updateClientSecret(clientId,
				passwordEncoder.encode(secret));

		if (!result) {
			throw new NoSuchClientException("No such client id");
		}

	}

	@Override
	public void removeClientDetails(String clientId) throws NoSuchClientException {

		final boolean result = mongoClientDetailsRepository.deleteByClientId(clientId);

		if (!result) {
			throw new NoSuchClientException("No such client id");
		}
	}

	@Override
	public List<ClientDetails> listClientDetails() {

		return mongoClientDetailsRepository.findAll().stream()
				.map(mongoClientDetails -> new BaseClientDetails(mongoClientDetails.getClientId(),
						Joiner.on(",").join(mongoClientDetails.getResourceIds()),
						Joiner.on(",").join(mongoClientDetails.getScope()),
						Joiner.on(",").join(mongoClientDetails.getAuthorizedGrantTypes()),
						Joiner.on(",").join(mongoClientDetails.getAuthorities()),
						Joiner.on(",").join(mongoClientDetails.getRegisteredRedirectUri())))
				.collect(Collectors.toList());

	}

	private Set<String> getAutoApproveScopes(final ClientDetails clientDetails) {

		if (clientDetails.isAutoApprove("true")) {
			return newHashSet("true"); // all scopes autoapproved
		}

		return filter(clientDetails.getScope(), ByAutoApproveOfScope(clientDetails));

	}

	private Predicate<String> ByAutoApproveOfScope(final ClientDetails clientDetails) {
		return new Predicate<String>() {

			@Override
			public boolean apply(final String scope) {
				return clientDetails.isAutoApprove(scope);
			}

		};
	}


	@Override
	public List<AuthenticationClientDetails> findAll() throws Go4ClientsServiceException {
		return mongoClientDetailsRepository.findAll();
	}

	@Override
	public AuthenticationClientDetails findById(String id) throws Go4ClientsServiceException {

		Optional<AuthenticationClientDetails> clientDetail = mongoClientDetailsRepository.findById(id);
		if (!clientDetail.isPresent()) {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.CLIENT_DETAIL_NOT_FOUND.getHttpStatus(),
					AuthenticationServerErrorCodes.CLIENT_DETAIL_NOT_FOUND.getMessage());
		}
		return clientDetail.get();
	}

	@Override
	public AuthenticationClientDetails insert(AuthenticationClientDetails clientDetails) throws Go4ClientsServiceException {

		if (clientDetails == null) {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.INVALID_DATA.getHttpStatus(),
					AuthenticationServerErrorCodes.INVALID_DATA.getMessage());
		}

		if (clientDetails.getClientSecret() == null || clientDetails.getClientSecret().isEmpty()) {
			throw new Go4ClientsServiceException(
					AuthenticationServerErrorCodes.INVALID_DATA_CLIENT_SECRET.getHttpStatus(),
					AuthenticationServerErrorCodes.INVALID_DATA_CLIENT_SECRET.getMessage());
		}

		if (clientDetails.getClientId() == null || clientDetails.getClientId().isEmpty()) {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.INVALID_DATA_CLIENT_ID.getHttpStatus(),
					AuthenticationServerErrorCodes.INVALID_DATA_CLIENT_ID.getMessage());
		}

		clientDetails.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()));
		return mongoClientDetailsRepository.insert(clientDetails);
	}

	@Override
	public AuthenticationClientDetails update(AuthenticationClientDetails clientDetails) throws Go4ClientsServiceException {

		if (clientDetails == null || clientDetails.getId() == null) {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.INVALID_DATA_ID.getHttpStatus(),
					AuthenticationServerErrorCodes.INVALID_DATA_ID.getMessage());
		}

		AuthenticationClientDetails currentClientDetails = findById(clientDetails.getClientId());

		// Validar si existe el cliente
		if (currentClientDetails == null) {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.CLIENT_DETAIL_NOT_FOUND.getHttpStatus(),
					AuthenticationServerErrorCodes.CLIENT_DETAIL_NOT_FOUND.getMessage());
		}

		if (clientDetails.getClientSecret() == null || clientDetails.getClientSecret().isEmpty()) {
			throw new Go4ClientsServiceException(
					AuthenticationServerErrorCodes.INVALID_DATA_CLIENT_SECRET.getHttpStatus(),
					AuthenticationServerErrorCodes.INVALID_DATA_CLIENT_SECRET.getMessage());
		}

		if (clientDetails.getClientId() == null || clientDetails.getClientId().isEmpty()) {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.INVALID_DATA_CLIENT_ID.getHttpStatus(),
					AuthenticationServerErrorCodes.INVALID_DATA_CLIENT_ID.getMessage());
		}

		// validar si se esta actualizando la contraseña
		if (currentClientDetails.getClientSecret().equals(clientDetails.getClientSecret())) {
			clientDetails.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()));
		}

		clientDetails.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()));
		return mongoClientDetailsRepository.save(clientDetails);
	}
}
