package com.telintel.services.go4clients.web.api.gateway.config.client.detail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationClientDetails;

@Component
public class MongoClientDetailsRepositoryImpl implements MongoClientDetailsRepositoryBase {

	private final MongoTemplate mongoTemplate;

	@Autowired
	public MongoClientDetailsRepositoryImpl(final MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public boolean deleteByClientId(String clientId) {
		final Query query = Query.query(Criteria.where("clientId").is(clientId));
		final DeleteResult writeResult = mongoTemplate.remove(query, AuthenticationClientDetails.class);
		return writeResult.getDeletedCount() == 1;
	}

	@Override
	public boolean update(final AuthenticationClientDetails mongoClientDetails) {
		final Query query = Query.query(Criteria.where("clientId").is(mongoClientDetails.getClientId()));

		final Update update = Update.update("scope", mongoClientDetails.getScope())
				.set("accessTokenValiditySeconds", mongoClientDetails.getAccessTokenValiditySeconds())
				.set("refreshTokenValiditySeconds", mongoClientDetails.getRefreshTokenValiditySeconds())
				.set("additionalInformation", mongoClientDetails.getAdditionalInformation())
				.set("resourceIds", mongoClientDetails.getResourceIds())
				.set("authorizedGrantTypes", mongoClientDetails.getAuthorizedGrantTypes())
				.set("authorities", mongoClientDetails.getAuthorities())
				.set("autoApproveScopes", mongoClientDetails.getAutoApproveScopes())
				.set("registeredRedirectUris", mongoClientDetails.getRegisteredRedirectUri());

		final UpdateResult writeResult = mongoTemplate.updateFirst(query, update, AuthenticationClientDetails.class);

		return writeResult.getModifiedCount() == 1;
	}

	@Override
	public boolean updateClientSecret(final String clientId, final String newSecret) {
		
		final Query query = Query.query(Criteria.where("clientId").is(clientId));
		final Update update = Update.update("clientSecret", newSecret);
		final UpdateResult writeResult = mongoTemplate.updateFirst(query, update, AuthenticationClientDetails.class);
		return writeResult.getModifiedCount() == 1;
	}

	@Override
	public AuthenticationClientDetails findByClientId(final String clientId) throws IllegalArgumentException {

		final Query query = Query.query(Criteria.where("clientId").is(clientId));
		final AuthenticationClientDetails mongoClientDetails = mongoTemplate.findOne(query, AuthenticationClientDetails.class);
		if (mongoClientDetails == null) {
			throw new IllegalArgumentException("No valid client id");
		}
		return mongoClientDetails;

	}

}
