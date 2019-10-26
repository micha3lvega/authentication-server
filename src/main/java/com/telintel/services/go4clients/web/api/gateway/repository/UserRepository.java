package com.telintel.services.go4clients.web.api.gateway.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationUser;
import com.telintel.services.go4clients.web.api.gateway.repository.base.UserRepositoryBase;

@Repository
public interface UserRepository extends MongoRepository<AuthenticationUser, String>, UserRepositoryBase {

	@Query("{'username':?0, 'appId':?1 }")
	AuthenticationUser findByLogin(String username, String appid);

	AuthenticationUser findByUsername(String username);

	List<AuthenticationUser> findByAppId(String appId);

	void deleteByUsername(String username);

	AuthenticationUser findByAppIdAndUsername(String appId, String username);

	AuthenticationUser findByApiKeyAndApiSecretAndAppId(String apiKey, String apiSecret, String appId);
	
	AuthenticationUser findByApiKey(String apiKey);

}
