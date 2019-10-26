package com.telintel.services.go4clients.web.api.gateway.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationUrlAccess;
import com.telintel.services.go4clients.web.api.gateway.repository.IUrlAccessRepository;
import com.telintel.services.go4clients.web.api.gateway.services.IURLAccessServices;
import com.telintel.services.go4clients.web.api.gateway.util.Util;
import com.telintel.telintelutils.security.AuthenticationUrlAccessDTO;

@Service
public class URLAccessServices implements IURLAccessServices {

	@Autowired
	private IUrlAccessRepository iUrlAccessRepository;

	@Override
	public List<AuthenticationUrlAccessDTO> findAll() {
		return iUrlAccessRepository.findAll().stream().map(Util::castAuthenticationUrl).collect(Collectors.toList());
	}

	@Override
	public AuthenticationUrlAccessDTO insert(AuthenticationUrlAccess access) throws Exception {

		if (access != null) {

			if (access.getUrl() == null || access.getUrl().isEmpty()) {
				throw new Exception("Invalid url");
			}

			if (access.getMethod() == null || access.getMethod().isEmpty()
					|| HttpMethod.resolve(access.getMethod().toUpperCase()) == null) {
				throw new Exception("Invalid http method");
			}

			if (access.getAppid() == null || access.getAppid().isEmpty()) {
				throw new Exception("Invalid appid");
			}

			access.setMethod(access.getMethod().toUpperCase());

			return Util.castAuthenticationUrl(iUrlAccessRepository.insert(access));
		}

		throw new Exception("Invalid parameters");
	}


	@Override
	public List<AuthenticationUrlAccessDTO> findByAppid(String appid) {
		return iUrlAccessRepository.findByAppid(appid).stream().map(Util::castAuthenticationUrl)
				.collect(Collectors.toList());
	}

	@Override
	public List<AuthenticationUrlAccessDTO> findByRolsAndAppid(List<String> rols, String appid) {
		return iUrlAccessRepository.findByRolsInAndAppid(rols, appid).stream().map(Util::castAuthenticationUrl)
				.collect(Collectors.toList());
	}

}
