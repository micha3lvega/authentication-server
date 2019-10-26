package com.telintel.services.go4clients.web.api.gateway.services.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationUser;
import com.telintel.services.go4clients.web.api.gateway.repository.UserRepository;
import com.telintel.services.go4clients.web.api.gateway.services.IUserServices;
import com.telintel.services.go4clients.web.api.gateway.util.AuthenticationServerErrorCodes;
import com.telintel.services.go4clients.web.api.gateway.util.Util;
import com.telintel.telintelutils.security.AuthenticationUserDTO;
import com.telintel.telintelutils.security.ResetPasswordDTO;
import com.telintel.telintelutils.security.UtilSecurity;
import com.telintel.utils.exceptionmanagementutils.Go4ClientsServiceException;

@Service
public class UserAuthenticationServices implements IUserServices, UserDetailsService {

	@Value("${customgenerator.apikeymaxlength:16}")
	private int apiKeyMaxLength;

	@Value("${customgenerator.apikeyminlength:16}")
	private int apiKeyMinLength;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository iUserRepository;

	@Override
	@Transactional(readOnly = true)
	public AuthenticationUser findByLogin(String username, String password, String appid)
			throws Go4ClientsServiceException {

		AuthenticationUser user = iUserRepository.findByLogin(username, appid);

		// validate password
		if (user != null && user.getPassword() != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
			return user;
		} else {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.USER_NOT_FOUND.getHttpStatus(),
					AuthenticationServerErrorCodes.USER_NOT_FOUND.getMessage());
		}

	}

	@Override
	@Transactional
	public AuthenticationUserDTO insert(AuthenticationUser user) throws Go4ClientsServiceException {

		if (user != null) {

			user.setEnabled(true);
			user.setCreateDate(new Date());
			user.setAccountNonExpired(true);
			user.setAccountNonLocked(true);
			user.setLastUpdateDate(new Date());
			user.setCredentialsNonExpired(true);

			String password = user.getPassword() != null ? user.getPassword() : UtilSecurity.generatedPassword();
			user.setPassword(bCryptPasswordEncoder.encode(password));

			String apisecret = RandomStringUtils.randomNumeric(generateRandomStringLength());
			user.setApiKey(UUID.randomUUID().toString().replace("-", ""));
			user.setApiSecret(bCryptPasswordEncoder.encode(apisecret));

			user = iUserRepository.insert(user);

			// plain secrets
			user.setPassword(password);
			user.setApiSecret(apisecret);

			AuthenticationUserDTO dto = Util.castUserToUserDTO(user);

			return dto;

		} else {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.USER_EMTPY.getHttpStatus(),
					AuthenticationServerErrorCodes.USER_EMTPY.getMessage());
		}

	}

	@Override
	@Transactional
	public AuthenticationUser update(AuthenticationUser user) throws Go4ClientsServiceException {

		try {

			String password = user.getPassword() != null ? user.getPassword() : UtilSecurity.generatedPassword();

			user.setLastUpdateDate(new Date());
			user.setPassword(bCryptPasswordEncoder.encode(password));

			return iUserRepository.save(user);
		} catch (Exception e) {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.GENERAL_EXCEPTION.getHttpStatus(),
					AuthenticationServerErrorCodes.GENERAL_EXCEPTION.getMessage());
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<AuthenticationUser> findAll() throws Go4ClientsServiceException {
		return iUserRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public AuthenticationUser findByUsername(String username) throws Go4ClientsServiceException {
		return iUserRepository.findByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		return iUserRepository.findByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public AuthenticationUser findByAccesToken(String apiKey, String apiSecret, String appId)
			throws Go4ClientsServiceException {
		return iUserRepository.findByApiKeyAndApiSecretAndAppId(apiKey, apiSecret, appId);
	}

	@Override
	@Transactional(readOnly = true)
	public AuthenticationUser findByUsername(String appid, String email) throws Go4ClientsServiceException {
		return iUserRepository.findByAppIdAndUsername(appid, email);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AuthenticationUser> findByAppid(String appid) throws Go4ClientsServiceException {
		return iUserRepository.findByAppId(appid);
	}

	@Override
	@Transactional(readOnly = true)
	public void delete(String id) throws Go4ClientsServiceException {

		AuthenticationUser entity = iUserRepository.findById(id).orElse(null);

		if (entity == null) {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.USER_NOT_FOUND.getHttpStatus(),
					AuthenticationServerErrorCodes.USER_NOT_FOUND.getMessage());
		}

		iUserRepository.delete(entity);

	}

	@Override
	@Transactional
	public void delete(String appid, String username) throws Go4ClientsServiceException {

		AuthenticationUser entity = iUserRepository.findByAppIdAndUsername(appid, username);

		if (entity == null) {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.USER_NOT_FOUND.getHttpStatus(),
					AuthenticationServerErrorCodes.USER_NOT_FOUND.getMessage());
		}

		iUserRepository.delete(entity);

	}

	@Override
	@Transactional
	public ResetPasswordDTO resetPassword(String appid, String username) throws Go4ClientsServiceException {

		// Buscar usuario
		AuthenticationUser user = findByUsername(appid, username);

		if (user == null || user.getId() == null) {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.USER_NOT_FOUND.getHttpStatus(),
					AuthenticationServerErrorCodes.USER_NOT_FOUND.getMessage());
		}

		// Generar password
		String password = UtilSecurity.generatedPassword();

		// actualizar usuario
		user.setPassword(password);
		user.setPassword(bCryptPasswordEncoder.encode(password));
		update(user);

		ResetPasswordDTO dto = new ResetPasswordDTO();
		dto.setAppid(appid);
		dto.setUsername(username);
		dto.setPassword(password);

		return dto;

	}

	private int generateRandomStringLength() {
		return (Long.valueOf(Math.round(Math.random() * (apiKeyMaxLength - apiKeyMinLength) + apiKeyMinLength)))
				.intValue();
	}

	@Override
	public AuthenticationUser findByapiKey(String apikey) throws Go4ClientsServiceException {
		return iUserRepository.findByApiKey(apikey);
	}

}
