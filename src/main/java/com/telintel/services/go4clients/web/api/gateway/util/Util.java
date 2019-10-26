package com.telintel.services.go4clients.web.api.gateway.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationClientDetails;
import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationUrlAccess;
import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationUser;
import com.telintel.telintelutils.security.AuthenticationClientDetailsDTO;
import com.telintel.telintelutils.security.AuthenticationUrlAccessDTO;
import com.telintel.telintelutils.security.AuthenticationUserDTO;

@SuppressWarnings("unchecked")
public class Util {

	private Util() {
	}
	
	public static AuthenticationUrlAccessDTO castAuthenticationUrl(AuthenticationUrlAccess authentication) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(authentication, AuthenticationUrlAccessDTO.class);
	}


	public static AuthenticationUserDTO castUserToUserDTO(AuthenticationUser user) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(user, AuthenticationUserDTO.class);
	}

	public static AuthenticationClientDetails castClientDetailDTO(AuthenticationClientDetailsDTO dto) {
		ModelMapper modelMapper = new ModelMapper();

		Converter<List<String>, List<GrantedAuthority>> convertRols = ctx -> {

			List<GrantedAuthority> auths = new ArrayList<>();

			if (ctx.getSource() != null) {
				for (String rol : ctx.getSource()) {
					SimpleGrantedAuthority authority = new SimpleGrantedAuthority(rol);
					auths.add(authority);
				}
			}

			return auths;
		};

		TypeMap<AuthenticationClientDetailsDTO, AuthenticationClientDetails> typeMap = modelMapper
				.createTypeMap(AuthenticationClientDetailsDTO.class, AuthenticationClientDetails.class);
		typeMap.addMappings(mapper -> mapper.using(convertRols).map(AuthenticationClientDetailsDTO::getAuthorities,
				AuthenticationClientDetails::setAuthorities));

		return modelMapper.map(dto, AuthenticationClientDetails.class);

	}

	public static boolean compareGrantType(Authentication authentication, String granType) {

		if (authentication.getDetails() != null) {

			LinkedHashMap<String, String> details = (LinkedHashMap<String, String>) authentication.getDetails();

			return details != null && details.get("grant_type") != null && details.get("grant_type") != null
					&& details.get("grant_type").equals(granType);

		}

		return false;
	}

	public static String getDetailValue(Authentication authentication, String value) {

		if (authentication.getDetails() != null) {

			LinkedHashMap<String, String> details = (LinkedHashMap<String, String>) authentication.getDetails();

			return details != null ? details.get(value) : null;
		}

		return null;
	}

}
