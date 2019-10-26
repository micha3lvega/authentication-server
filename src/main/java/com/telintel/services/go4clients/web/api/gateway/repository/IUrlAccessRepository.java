package com.telintel.services.go4clients.web.api.gateway.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationUrlAccess;

public interface IUrlAccessRepository extends MongoRepository<AuthenticationUrlAccess, String> {

	List<AuthenticationUrlAccess> findByAppid(String appid);
	
	List<AuthenticationUrlAccess> findByRolsInAndAppid(List<String> rols, String appid);

}
