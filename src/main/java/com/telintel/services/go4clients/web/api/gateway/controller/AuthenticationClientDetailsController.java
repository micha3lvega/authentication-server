package com.telintel.services.go4clients.web.api.gateway.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telintel.services.go4clients.web.api.gateway.services.IMongoClientDetailsService;
import com.telintel.services.go4clients.web.api.gateway.util.Util;
import com.telintel.telintelutils.security.AuthenticationClientDetailsDTO;
import com.telintel.utils.exceptionmanagementutils.Go4ClientsServiceException;

@RestController
@RequestMapping("/client/detail/v1.0")
public class AuthenticationClientDetailsController {

	@Autowired
	private IMongoClientDetailsService clientDetailsService;

	@GetMapping
	public ResponseEntity<?> findAll(@RequestHeader(value = "locale", required = false) String locale)
			throws Go4ClientsServiceException {

		return new ResponseEntity<>(clientDetailsService.findAll().stream().map(clientDetail -> {
			return clientDetail.cast();
		}).collect(Collectors.toList()), HttpStatus.OK);

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@RequestHeader(value = "locale", required = false) String locale,
			@PathVariable("id") String id) throws Go4ClientsServiceException {
		return new ResponseEntity<>(clientDetailsService.findById(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> insert(@RequestHeader(value = "locale", required = false) String locale,
			@RequestBody AuthenticationClientDetailsDTO clientDetails) throws Go4ClientsServiceException {
		return new ResponseEntity<>(clientDetailsService.insert(Util.castClientDetailDTO(clientDetails)).cast(),
				HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestHeader(value = "locale", required = false) String locale,
			@RequestBody AuthenticationClientDetailsDTO clientDetails) throws Go4ClientsServiceException {
		return new ResponseEntity<>(clientDetailsService.update(Util.castClientDetailDTO(clientDetails)).cast(),
				HttpStatus.OK);
	}

}
