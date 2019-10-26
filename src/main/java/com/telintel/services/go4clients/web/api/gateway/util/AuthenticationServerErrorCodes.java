package com.telintel.services.go4clients.web.api.gateway.util;

import org.springframework.http.HttpStatus;

import com.telintel.telintelutils.integrations.ITelintelServiceErrorMsg;

public enum AuthenticationServerErrorCodes implements ITelintelServiceErrorMsg {
	
	GENERAL_EXCEPTION("authentication.server.general.exception", HttpStatus.INTERNAL_SERVER_ERROR.value() + ""),
	SERVICE_UNAVAILABLE("authentication.server.service.unavailable", HttpStatus.INTERNAL_SERVER_ERROR.value() + ""),
	PAGINATION_EXCEPTION("authentication.server.pagination.exception", HttpStatus.BAD_REQUEST.value() + ""),
	SENDER_EXCEPTION("authentication.server.sender.exception", HttpStatus.BAD_REQUEST.value() + ""),
	DATA_NOT_FOUND("authentication.server.service.data.not.found", HttpStatus.INTERNAL_SERVER_ERROR.value() + ""),
	INVALID_DATA("authentication.server.invalid.data", HttpStatus.BAD_REQUEST.value() + ""),
	INVALID_DATA_ID("authentication.server.invalid.data.id", HttpStatus.BAD_REQUEST.value() + ""),
	INVALID_DATA_CLIENT_SECRET("authentication.server.invalid.data.cliente.secret", HttpStatus.BAD_REQUEST.value() + ""),
	DUPLICATE_CLIENT_DETAIL("authentication.server.duplicate.client.detail", HttpStatus.BAD_REQUEST.value() + ""),
	INVALID_DATA_CLIENT_ID("authentication.server.invalid.data.cliente.id", HttpStatus.BAD_REQUEST.value() + ""),
	USER_NOT_FOUND("authentication.server.service.user.not.found", HttpStatus.NOT_FOUND.value() + ""),
	CLIENT_DETAIL_NOT_FOUND("authentication.server.service.client.detail.not.found", HttpStatus.NOT_FOUND.value() + ""),
	COUNTRY_NOT_FOUND("authentication.server.signup.country.not.found.exception", HttpStatus.BAD_REQUEST.value() + ""),
	TIMEZONE_NOT_FOUND("authentication.server.signup.timezone.not.found.exception", HttpStatus.BAD_REQUEST.value() + ""),
	USER_EMTPY("authentication.server.signup.user.empty.data.exception", HttpStatus.BAD_REQUEST.value() + ""),
	INVALID_AUTH("authentication.server.signup.invalid.auth.exception", HttpStatus.BAD_REQUEST.value() + "");

	private String message;
	private String httpStatus;
	private String errorDetail;

	private AuthenticationServerErrorCodes(String message, String httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}

	private AuthenticationServerErrorCodes(String message, String errorDetail, String httpStatus) {
		this.message = message;
		this.errorDetail = errorDetail;
		this.httpStatus = httpStatus;
	}

	private AuthenticationServerErrorCodes(String message) {
		this.message = message;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String getHttpStatus() {
		return httpStatus;
	}
}
