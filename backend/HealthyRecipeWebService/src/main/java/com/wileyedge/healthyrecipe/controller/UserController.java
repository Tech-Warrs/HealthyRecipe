package com.wileyedge.healthyrecipe.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.model.User;
import com.wileyedge.healthyrecipe.service.UserServiceInterface;

@RestController
@RequestMapping("/users")
public class UserController {


	private UserServiceInterface userService;

	@Autowired
	public UserController(UserServiceInterface userService) {
		this.userService = userService;
	}

	@PostMapping
	public User createUser(@RequestBody User user) {
		User createdUser = userService.createUser(user);
		return createdUser;
	}

	@PutMapping("/{userId}")
	public User updateUser(@PathVariable Integer userId, @RequestBody User user) {
		user.setId(userId);
		User updatedUser = userService.updateUser(user);
		return updatedUser;
	}

	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable Integer userId) {
		userService.deleteUser(userId);
	}

	@GetMapping("/{userId}")
	public User getUserById(@PathVariable Integer userId) {
		 Optional<User> userOptional = userService.findUserById(userId);
		    if (userOptional.isPresent()) {
		        User user = userOptional.get();
		        return user;
		    } else {
		        throw new UserNotFoundException("User not found");
		    }
	}

	@GetMapping("/email/{email}")
	public User getUserByEmail(@PathVariable String email) {
		User user = userService.findUserByEmail(email);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("User not found");
		}
	}

	@GetMapping("/username/{username}")
	public User getUserByUsername(@PathVariable String username) {
		User user = userService.findUserByUsername(username);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("User not found");
		}
	}

	@GetMapping
	public List<User> getAllUsers() {
		List<User> users = userService.findAllUsers();
		return users;
	}




}
