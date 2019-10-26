package com.telintel.services.go4clients.web.api.gateway.config.client.detail;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationClientDetails;

public interface MongoClientDetailsRepository
		extends MongoRepository<AuthenticationClientDetails, String>, MongoClientDetailsRepositoryBase {

}
