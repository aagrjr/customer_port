package br.com.portfolio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Customer not found")
public class CustomerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7825066852624562956L;
	
}