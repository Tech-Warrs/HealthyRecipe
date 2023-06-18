package com.wileyedge.healthyrecipe.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
