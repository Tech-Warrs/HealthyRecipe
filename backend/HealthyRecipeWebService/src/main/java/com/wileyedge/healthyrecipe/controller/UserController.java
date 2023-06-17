package com.wileyedge.healthyrecipe.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.model.LoginRequest;
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
			throw new UserNotFoundException("Email: " + email);
		}
	}

	@GetMapping("/username/{username}")
	public User getUserByUsername(@PathVariable String username) {
		User user = userService.findUserByUsername(username);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("Username: " + username);
		}
	}

	@PostMapping("/login")
	public String loginUser(@RequestBody LoginRequest loginRequest) {
	    String username = loginRequest.getUsername();
	    String password = loginRequest.getPassword();
	    
	    String token = userService.loginUser(username, password);
	    
	    return token;
	}
	
	@PostMapping("/logout")
	public String logoutUser(@RequestHeader("Authorization") String token) {
	    userService.logoutUser(token);
	    return "User logged out successfully.";
	}
	
//	@DeleteMapping("/{userId}")
//	public String deleteUser(@PathVariable Long userIdToDelete, @RequestHeader("Authorization") String token) {
//	    String jwtToken = token.split(" ")[1].trim(); // the first part of the string is "Bearer" and we want to remove it.
//		try {
//			userService.deleteUser(userIdToDelete, jwtToken);
//			return "SUCCESS: User has been deleted successfully";
//		}catch (UserNotFoundException ex) {
//			return ex.getMessage();
//		}catch (Exception ex) {
//			return ex.getMessage();
//		}
//	    
//	}

}
