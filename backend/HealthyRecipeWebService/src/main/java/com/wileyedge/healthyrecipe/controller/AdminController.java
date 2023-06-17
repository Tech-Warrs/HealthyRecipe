package com.wileyedge.healthyrecipe.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.model.User;
import com.wileyedge.healthyrecipe.service.UserServiceInterface;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	private UserServiceInterface userService;
	
	@Autowired
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
	public List<User> getAllUsers() {
		List<User> users = userService.findAllUsers();
		
		return users;
	}

	@DeleteMapping("/users/{userIdToDelete}")
	public String deleteUser(@PathVariable long userIdToDelete, @RequestHeader("Authorization") String token) {
	    String jwtToken = token.split(" ")[1].trim(); // the first part of the string is "Bearer" and we want to remove it.
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
