package com.wileyedge.healthyrecipe.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wileyedge.healthyrecipe.exception.InvalidTokenException;
import com.wileyedge.healthyrecipe.exception.UnauthorizedAccessException;
import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.model.User;
import com.wileyedge.healthyrecipe.service.UserServiceInterface;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	private UserServiceInterface userService;
	
	public AdminController(UserServiceInterface userService) {
		this.userService = userService;
	}
	
	@PostConstruct
    public void createDefaultAdmin() {
        User defaultAdmin = new User();
        defaultAdmin.setUsername("admin");
        defaultAdmin.setEmail("admin@example.com");
        defaultAdmin.setFirstName("Admin");
        defaultAdmin.setPassword("Admin@123");
        defaultAdmin.setRole("ADMIN");
        userService.createUser(defaultAdmin);
    }
	
	@GetMapping("/users")
	public List<User> getAllUsers(@RequestHeader("Authorization") String token) {
		List<User> users = null;
		try {
			if(token == null || token.isBlank() || token.isEmpty()) {
				throw new InvalidTokenException("no token found");
			}
			// remove the first part of the string which is "Bearer " 
			String jwtToken = token.replace("Bearer ", "").trim();
			users = userService.findAllUsers(jwtToken);			

		}catch (UnauthorizedAccessException ex) {
			throw new UnauthorizedAccessException(ex.getMessage());
		}catch(InvalidTokenException ex){
			throw new InvalidTokenException(ex.getMessage());
		}
		
		return users;
	}
	
	
	@GetMapping("/{userId}")
	public User getUserById(@PathVariable Integer userId,  @RequestHeader("Authorization") String token) {
		 Optional<User> userOptional = userService.findUserById(userId, token);
		    if (userOptional.isPresent()) {
		        User user = userOptional.get();
		        return user;
		    } else {
		        throw new UserNotFoundException("User not found");
		    }
	}

	@GetMapping("/email/{email}")
	public User getUserByEmail(@PathVariable String email,  @RequestHeader("Authorization") String token) {
		User user = userService.findUserByEmail(email,token);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("Email: " + email);
		}
	}

	@GetMapping("/username/{username}")
	public User getUserByUsername(@PathVariable String username,  @RequestHeader("Authorization") String token) {
		User user = userService.findUserByUsername(username, token);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("Username: " + username);
		}
	}


	@DeleteMapping("/users/{userIdToDelete}")
	public String deleteUser(@PathVariable long userIdToDelete, @RequestHeader("Authorization") String token) {
		 // remove the first part of the string which is "Bearer " 
		String jwtToken = token.replace("Bearer ", "").trim();

		try {
			userService.deleteUser(userIdToDelete, jwtToken);
			return "SUCCESS: User has been deleted successfully";
		}catch (UserNotFoundException ex) {
			return ex.getMessage();
		}catch (Exception ex) {
			return ex.getMessage();
		}
	    
	}

}
