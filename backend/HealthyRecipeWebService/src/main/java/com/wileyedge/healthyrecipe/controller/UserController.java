package com.wileyedge.healthyrecipe.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wileyedge.healthyrecipe.model.LoginRequest;
import com.wileyedge.healthyrecipe.model.User;
import com.wileyedge.healthyrecipe.service.IUserService;

@RestController
@RequestMapping("/users")
public class UserController {


	private IUserService userService;

	@Autowired
	public UserController(IUserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/signup")
	public User createUser(@RequestBody User user) {
		User createdUser = userService.createUser(user);
		return createdUser;
	}

	@PutMapping("/{userId}")
	public User updateUserDetailsById(@PathVariable Integer userId, @RequestBody User userDetails) {
		userDetails.setId(userId);
		User updatedUser = userService.updateUserDetailsById(userDetails);
		return updatedUser;
	}

	
	@PostMapping("/login")
	public String loginUser(@RequestBody LoginRequest loginRequest) {
	    String identifier = loginRequest.getIdentifier();
	    String password = loginRequest.getPassword();
	    
	    String token = userService.loginUser(identifier, password);
	    
	    return token;
	}
	
	@PostMapping("/logout")
	public String logoutUser(@RequestHeader("Authorization") String token) {
	    userService.logoutUser(token);
	    return "User logged out successfully.";
	}


}
