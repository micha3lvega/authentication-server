package com.telintel.services.go4clients.web.api.gateway.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telintel.services.go4clients.web.api.gateway.entity.AuthenticationUrlAccess;
import com.telintel.services.go4clients.web.api.gateway.services.IURLAccessServices;
import com.telintel.telintelutils.security.AuthenticationUrlAccessDTO;

@RestController
@RequestMapping("/url/access/v1.0")
public class AuthenticationURLAccessController {
	private static final Logger log = LoggerFactory.getLogger(AuthenticationURLAccessController.class);

	@Autowired
	private IURLAccessServices iurlAccessServices;

	@GetMapping
	public List<AuthenticationUrlAccessDTO> findAll() {
		return iurlAccessServices.findAll();
	}

	@GetMapping("/{appid}")
	public List<AuthenticationUrlAccessDTO> findByAppid(@PathVariable String appid) {
		return iurlAccessServices.findByAppid(appid);
	}

	@GetMapping("/{appid}/roles")
	public List<AuthenticationUrlAccessDTO> findByAppidAndRols(@PathVariable String appid,
			@RequestParam(name = "rol", required = false) String[] roles) {

		if (roles == null) {

			return findByAppid(appid);

		} else {

			List<String> rolesList = new ArrayList<>(Arrays.asList(roles));

			return iurlAccessServices.findByRolsAndAppid(rolesList, appid);
		}

	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody AuthenticationUrlAccess urlAccess) {

		try {

			return new ResponseEntity<>(iurlAccessServices.insert(urlAccess), HttpStatus.OK);

		} catch (Exception e) {
			log.error("(save) Exception: " + e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

}
