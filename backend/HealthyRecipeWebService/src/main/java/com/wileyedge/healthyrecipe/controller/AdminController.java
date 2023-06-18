package com.wileyedge.healthyrecipe.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.wileyedge.healthyrecipe.model.HealthCategory;
import com.wileyedge.healthyrecipe.model.Recipe;
import com.wileyedge.healthyrecipe.model.User;
import com.wileyedge.healthyrecipe.service.IRecipeService;
import com.wileyedge.healthyrecipe.service.IUserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private IUserService userService;
	private IRecipeService recipeService;

	public AdminController(IUserService userService, IRecipeService recipeService) {
		this.userService = userService;
		this.recipeService = recipeService;
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


		User user = new User();
		user.setUsername("exampleUser");
		user.setEmail("user@example.com");
		user.setPassword("Password@123");
		user.setFirstName("Rafael");
		user.setLastName("Dawson");
		user.setRole("MEMBER"); 
		userService.createUser(user);

		Recipe recipe1 = new Recipe();
		recipe1.setTitle("Recipe 1");
		recipe1.setShortDesc("This is recipe 1");
		recipe1.setIngredients("Ingredient 1, Ingredient 2");
		recipe1.setInstructions("Step 1, Step 2, Step 3");
		recipe1.setCookingDurationInMinutes(30);


		Recipe recipe2 = new Recipe();
		recipe2.setTitle("Recipe 2");
		recipe2.setShortDesc("This is recipe 2");
		recipe2.setIngredients("Ingredient 1, Ingredient 2");
		recipe2.setInstructions("Step 1, Step 2, Step 3");
		recipe2.setCookingDurationInMinutes(45);


		Set<HealthCategory> suitableForSet = new HashSet<>();
		suitableForSet.add(HealthCategory.WEIGHT_LOSS);
		suitableForSet.add(HealthCategory.HIGH_BLOOD_PRESSURE);
		suitableForSet.add(HealthCategory.GENERAL);

		recipe1.setSuitableFor(suitableForSet);
		recipe2.setSuitableFor(suitableForSet);

		Set<HealthCategory> notSuitableForSet1 = new HashSet<>();
		notSuitableForSet1.add(HealthCategory.DIABETES_MANAGEMENT);

		Set<HealthCategory> notSuitableForSet2 = new HashSet<>();
		notSuitableForSet2.add(HealthCategory.HEART_HEALTH);

		recipe1.setNotSuitableFor(notSuitableForSet1);
		recipe2.setNotSuitableFor(notSuitableForSet2);

		recipe1.setUser(user);
		recipe2.setUser(user);

		recipeService.createRecipe(recipe1);
		recipeService.createRecipe(recipe2);

	}

	@GetMapping("/users")
	public List<User> getAllUsers(@RequestHeader("Authorization") String token) {
		List<User> users = null;

		if(token == null || token.isBlank() || token.isEmpty()) {
			throw new InvalidTokenException("no token found");
		}
		// remove the first part of the string which is "Bearer " 
		String jwtToken = token.replace("Bearer ", "").trim();
		users = userService.findAllUsers(jwtToken);			

		return users;
	}


	@GetMapping("/users/{userId}")
	public User getUserById(@PathVariable Integer userId,  @RequestHeader("Authorization") String token) {
		Optional<User> userOptional = userService.findUserById(userId, token);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			return user;
		} else {
			throw new UserNotFoundException("User not found");
		}
	}

	@GetMapping("/users/email/{email}")
	public User getUserByEmail(@PathVariable String email,  @RequestHeader("Authorization") String token) {
		User user = userService.findUserByEmail(email,token);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("Email: " + email);
		}
	}

	@GetMapping("/users/username/{username}")
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
