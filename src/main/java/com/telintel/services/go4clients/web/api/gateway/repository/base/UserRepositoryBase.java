package com.telintel.services.go4clients.web.api.gateway.repository.base;

public interface UserRepositoryBase {

    boolean changePassword(String oldPassword, String newPassword, String username);

}
