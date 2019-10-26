package com.telintel.services.go4clients.web.api.gateway.config.client.detail;

import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationClientDetails;

public interface MongoClientDetailsRepositoryBase {
	
    boolean deleteByClientId(String clientId);

    boolean update(AuthenticationClientDetails mongoClientDetails);

    boolean updateClientSecret(String clientId, String newSecret);

    AuthenticationClientDetails findByClientId(String clientId) throws IllegalArgumentException;
    
}