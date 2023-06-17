package com.wileyedge.healthyrecipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
	 private String searchCriteria;

	    public UserNotFoundException(String searchCriteria) {
	        super("User not found " + searchCriteria);
	        this.searchCriteria = searchCriteria;
	    }

	    public String getSearchCriteria() {
	        return searchCriteria;
	    }
}
