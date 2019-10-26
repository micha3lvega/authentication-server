package com.telintel.services.go4clients.web.api.gateway.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import com.telintel.telintelutils.security.AuthenticationClientDetailsDTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Document(collection = "client_details")
public class AuthenticationClientDetails implements ClientDetails {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String clientId;
	private String clientSecret;
	private Set<String> scope;
	private Set<String> resourceIds;
	private Set<String> authorizedGrantTypes;
	private Set<String> registeredRedirectUris;
	private List<GrantedAuthority> authorities;
	private Integer refreshTokenValiditySeconds;
	private Integer accessTokenValiditySeconds;
	private Map<String, Object> additionalInformation;
	private Set<String> autoApproveScopes;

	public AuthenticationClientDetails(final String clientId, final String clientSecret, final Set<String> scope,
			final Set<String> resourceIds, final Set<String> authorizedGrantTypes,
			final Set<String> registeredRedirectUris, final List<GrantedAuthority> authorities,
			final Integer accessTokenValiditySeconds, final Integer refreshTokenValiditySeconds,
			final Map<String, Object> additionalInformation, final Set<String> autoApproveScopes) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.scope = scope;
		this.resourceIds = resourceIds;
		this.authorizedGrantTypes = authorizedGrantTypes;
		this.registeredRedirectUris = registeredRedirectUris;
		this.authorities = authorities;
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
		this.additionalInformation = additionalInformation;
		this.autoApproveScopes = autoApproveScopes;
	}

	public AuthenticationClientDetailsDTO cast() {

		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(this, AuthenticationClientDetailsDTO.class);

	}

	@Override
	public boolean isScoped() {
		return this.scope != null && !this.scope.isEmpty();
	}

	@Override
	public boolean isSecretRequired() {
		return this.clientSecret != null;
	}

	@Override
	public Set<String> getRegisteredRedirectUri() {
		return registeredRedirectUris;
	}

	@Override
	public boolean isAutoApprove(final String scope) {
		if (autoApproveScopes == null) {
			return false;
		}
		for (String auto : autoApproveScopes) {
			if (auto.equals("true") || scope.matches(auto)) {
				return true;
			}
		}
		return false;
	}

}
