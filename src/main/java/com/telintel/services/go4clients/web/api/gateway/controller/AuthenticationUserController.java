package com.telintel.services.go4clients.web.api.gateway.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationUser;
import com.telintel.services.go4clients.web.api.gateway.services.IUserServices;
import com.telintel.services.go4clients.web.api.gateway.util.AuthenticationServerErrorCodes;
import com.telintel.services.go4clients.web.api.gateway.util.Util;
import com.telintel.telintelutils.security.AuthenticationUserDTO;
import com.telintel.utils.exceptionmanagementutils.Go4ClientsServiceException;

@RestController
@RequestMapping("/user/auth/v1.0")
public class AuthenticationUserController {

	@Autowired
	private IUserServices iUserServices;

	@GetMapping
	public ResponseEntity<?> findAll(@RequestHeader(value = "locale", required = false) String locale)
			throws Go4ClientsServiceException {

		List<AuthenticationUserDTO> users = iUserServices.findAll().stream().map(Util::castUserToUserDTO)
				.collect(Collectors.toList());

		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/{appid}")
	public ResponseEntity<?> findAll(@RequestHeader(value = "locale", required = false) String locale,
			@PathVariable(value = "appid") String appid) throws Go4ClientsServiceException {

		List<AuthenticationUserDTO> users = iUserServices.findByAppid(appid).stream().map(Util::castUserToUserDTO)
				.collect(Collectors.toList());

		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/login/{appid}/{user}/{password}")
	public ResponseEntity<?> login(@RequestHeader(value = "locale", required = false) String locale,
			@PathVariable(value = "appid") String appid, @PathVariable(value = "user") String user,
			@PathVariable(value = "password") String password) throws Go4ClientsServiceException {

		AuthenticationUser usuario = iUserServices.findByLogin(user, password, appid);

		if (usuario == null || usuario.getId() == null) {
			throw new Go4ClientsServiceException(AuthenticationServerErrorCodes.USER_NOT_FOUND.getHttpStatus(),
					AuthenticationServerErrorCodes.USER_NOT_FOUND.getMessage());
		} else {
			return new ResponseEntity<>(Util.castUserToUserDTO(usuario), HttpStatus.OK);
		}

	}

	@DeleteMapping("/{appid}/{email}")
	public ResponseEntity<Void> delete(@PathVariable("appid") String appid, @PathVariable("username") String username)
			throws Go4ClientsServiceException {

		iUserServices.delete(appid, username);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping("/login/{appid}/{username}")
	public ResponseEntity<?> searchByEmail(@PathVariable(value = "appid") String appid,
			@PathVariable(value = "username") String username) throws Go4ClientsServiceException {

		return new ResponseEntity<>(iUserServices.findByUsername(appid, username), HttpStatus.OK);

	}

	@PostMapping
	public ResponseEntity<AuthenticationUserDTO> insert(
			@RequestHeader(value = "locale", required = false) String locale, @RequestBody AuthenticationUserDTO user)
			throws Go4ClientsServiceException {

		return new ResponseEntity<AuthenticationUserDTO>(iUserServices.insert(AuthenticationUser.cast(user)),
				HttpStatus.OK);

	}

	@PutMapping
	public ResponseEntity<?> update(@RequestHeader(value = "locale", required = false) String locale,
			@RequestBody AuthenticationUserDTO user) throws Go4ClientsServiceException {

		return new ResponseEntity<>(iUserServices.update(AuthenticationUser.cast(user)), HttpStatus.OK);

	}

	@PutMapping("/{appid}/{email}")
	public ResponseEntity<?> resetPassword(@PathVariable(value = "appid") String appid,
			@PathVariable(value = "username") String username) throws Go4ClientsServiceException {

		return new ResponseEntity<>(iUserServices.resetPassword(appid, username), HttpStatus.OK);

	}

}
