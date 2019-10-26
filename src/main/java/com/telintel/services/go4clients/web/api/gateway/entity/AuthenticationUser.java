package com.telintel.services.go4clients.web.api.gateway.entity;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.telintel.telintelutils.security.AuthenticationUserDTO;

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
public class AuthenticationUser implements UserDetails, CredentialsContainer {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String appId;
	
	private String apiKey;
	private String apiSecret;

	private String password;
	private String username;
	private boolean enabled;

	private boolean accountNonLocked;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;

	private Date createDate;
	private Date lastUpdateDate;

	private Set<GrantedAuthority> authorities;

	public static AuthenticationUser cast(AuthenticationUserDTO dto) {

		ModelMapper modelMapper = new ModelMapper();

		Converter<List<String>, Set<GrantedAuthority>> convertRols = ctx -> {

			Set<GrantedAuthority> auths = new HashSet<>();

			if (ctx.getSource() != null) {
				for (String rol : ctx.getSource()) {
					SimpleGrantedAuthority authority = new SimpleGrantedAuthority(rol);
					auths.add(authority);
				}
			}

			return auths;
		};

		TypeMap<AuthenticationUserDTO, AuthenticationUser> typeMap = modelMapper.createTypeMap(AuthenticationUserDTO.class,
				AuthenticationUser.class);
		typeMap.addMappings(
				mapper -> mapper.using(convertRols).map(AuthenticationUserDTO::getAuthorities, AuthenticationUser::setAuthorities));

		return modelMapper.map(dto, AuthenticationUser.class);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void eraseCredentials() {
		password = null;
	}

	public UUID getUserUUID() {
		return UUID.randomUUID();
	}
	
	

}
