/**
 * 
 */
package com.telintel.services.go4clients.web.api.gateway.configurers;

import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationUser;
import com.telintel.services.go4clients.web.api.gateway.services.IUserServices;
import com.telintel.utils.exceptionmanagementutils.Go4ClientsServiceException;

/**
 * A custom {@link TokenGranter} that always grants a token, and does not
 * authenticate users (hence the client has to be trusted to only send
 * authenticated client details).
 * 
 * @author Michael vega
 *
 */
public class ApiKeyTokenGranter extends AbstractTokenGranter {

	private IUserServices iUserServices;

	private BCryptPasswordEncoder passwordEnconder;

	public ApiKeyTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
			OAuth2RequestFactory requestFactory, String grantType, IUserServices iUserServices,
			BCryptPasswordEncoder passwordEnconder) {
		super(tokenServices, clientDetailsService, requestFactory, grantType);
		this.iUserServices = iUserServices;
		this.passwordEnconder = passwordEnconder;
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

		Map<String, String> params = tokenRequest.getRequestParameters();

		if (!params.containsKey("apikey") && !params.containsKey("apisecret")) {
			throw new InvalidRequestException("Invalid Credentials.");
		}

		String apikey = params.get("apikey");
		String apisecret = params.get("apisecret");

		try {
			if (iUserServices != null) {
				// search by api Credentials
				AuthenticationUser user = iUserServices.findByapiKey(apikey);

				if (user != null) {

					if (user.getApiSecret() != null && apisecret != null
							&& passwordEnconder.matches(user.getApiSecret(), apisecret)) {
						throw new UsernameNotFoundException("Invalid Credentials.");
					}

					Authentication userAuthentication = new UsernamePasswordAuthenticationToken(user.getUsername(),
							user.getPassword(), user.getAuthorities());
					OAuth2Authentication authentication = new OAuth2Authentication(
							tokenRequest.createOAuth2Request(client), userAuthentication);
					return authentication;
				}
			}
		} catch (Go4ClientsServiceException e) {
			throw new UsernameNotFoundException("Invalid Credentials.");
		}

		throw new UsernameNotFoundException("Invalid Credentials.");

	}

}
