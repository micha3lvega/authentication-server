package com.telintel.services.go4clients.web.api.gateway.repository.impl;

import com.mongodb.client.result.UpdateResult;
import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationUser;
import com.telintel.services.go4clients.web.api.gateway.repository.base.UserRepositoryBase;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Update.update;

@Component
public class UserRepositoryImpl implements UserRepositoryBase {

    private final MongoTemplate mongoTemplate;

    public UserRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean changePassword(final String oldPassword,
                                  final String newPassword,
                                  final String username) {
        final Query searchUserQuery = new Query(where("username").is(username).andOperator(where("password").is(oldPassword)));
        final UpdateResult updateResult = mongoTemplate.updateFirst(searchUserQuery, update("password", newPassword), AuthenticationUser.class);
        return updateResult.wasAcknowledged();
    }
}