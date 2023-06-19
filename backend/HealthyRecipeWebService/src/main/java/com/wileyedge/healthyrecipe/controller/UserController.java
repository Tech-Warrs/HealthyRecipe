package com.wileyedge.healthyrecipe.controller;


import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
//import com.wileyedge.healthyrecipe.model.HealthCategory;
//import com.wileyedge.healthyrecipe.model.Recipe;
import com.wileyedge.healthyrecipe.model.User;
import com.wileyedge.healthyrecipe.service.IMemberService;
//import com.wileyedge.healthyrecipe.service.IRecipeService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

	private IMemberService memberService;
//	private IRecipeService recipeService;

	@Autowired
	public UserController(IMemberService memberService /*IRecipeService recipeService*/ ) {
		this.memberService = memberService;
//		this.recipeService = recipeService;
	}
	
	@PostConstruct
	public void createDefaultAdmin() {
		User defaultAdmin = new User();
		defaultAdmin.setUsername("admin");
		defaultAdmin.setEmail("admin@example.com");
		defaultAdmin.setFirstName("Admin");
		defaultAdmin.setPassword("Admin@123");
		defaultAdmin.setRole("ADMIN");
		memberService.createUser(defaultAdmin);


		User user = new User();
		user.setUsername("exampleUser");
		user.setEmail("user@example.com");
		user.setPassword("Password@123");
		user.setFirstName("Rafael");
		user.setLastName("Dawson");
		user.setRole("MEMBER"); 
		memberService.createUser(user);

//		Recipe recipe1 = new Recipe();
//		recipe1.setTitle("Recipe 1");
//		recipe1.setShortDesc("This is recipe 1");
//		recipe1.setIngredients("Ingredient 1, Ingredient 2");
//		recipe1.setInstructions("Step 1, Step 2, Step 3");
//		recipe1.setCookingDurationInMinutes(30);
//
//
//		Recipe recipe2 = new Recipe();
//		recipe2.setTitle("Recipe 2");
//		recipe2.setShortDesc("This is recipe 2");
//		recipe2.setIngredients("Ingredient 1, Ingredient 2");
//		recipe2.setInstructions("Step 1, Step 2, Step 3");
//		recipe2.setCookingDurationInMinutes(45);
//
//
//		Set<HealthCategory> suitableForSet = new HashSet<>();
//		suitableForSet.add(HealthCategory.WEIGHT_LOSS);
//		suitableForSet.add(HealthCategory.HIGH_BLOOD_PRESSURE);
//		suitableForSet.add(HealthCategory.GENERAL);
//		
//		Set<HealthCategory> suitableForSet2 = new HashSet<>();
//		suitableForSet2.add(HealthCategory.IMMUNE_SUPPORT);
//		suitableForSet2.add(HealthCategory.DIGESTIVE_HEALTH);
//		suitableForSet2.add(HealthCategory.GENERAL);
//
//		recipe1.setSuitableFor(suitableForSet);
//		recipe2.setSuitableFor(suitableForSet2);
//
//		Set<HealthCategory> notSuitableForSet1 = new HashSet<>();
//		notSuitableForSet1.add(HealthCategory.DIABETES_MANAGEMENT);
//
//		Set<HealthCategory> notSuitableForSet2 = new HashSet<>();
//		notSuitableForSet2.add(HealthCategory.HEART_HEALTH);
//
//		recipe1.setNotSuitableFor(notSuitableForSet1);
//		recipe2.setNotSuitableFor(notSuitableForSet2);
//
//		recipe1.setUser(user);
//		recipe2.setUser(user);
//
//		recipeService.createRecipe(recipe1);
//		recipeService.createRecipe(recipe2);

	}

	
	@PostMapping("/signup")
	public User createUser(@RequestBody User user) {
		User createdUser = memberService.createUser(user);
		return createdUser;
	}

	
	@GetMapping("/{userId}")
	public User getUserById(@PathVariable long userId, @RequestHeader("Authorization") String token) {
		User updatedUser = memberService.findUserById(userId, token);
		return updatedUser;
	}
	

	@GetMapping("/email/{email}")
	public User getUserByEmail(@PathVariable String email,  @RequestHeader("Authorization") String token) {
		User user = memberService.findUserByEmail(email,token);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("Email: " + email);
		}
	}

	@GetMapping("/username/{username}")
	public User getUserByUsername(@PathVariable String username,  @RequestHeader("Authorization") String token) {
		User user = memberService.findUserByUsername(username, token);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("Username: " + username);
		}
	}
	
	@PutMapping("/{userId}")
	public User updateUserDetailsById(@PathVariable long userId, @RequestBody User userDetails, @RequestHeader("Authorization") String token) {
		userDetails.setId(userId);
		User updatedUser = memberService.updateUserDetailsById(userDetails,token);
		return updatedUser;
	}

	@DeleteMapping("/{userIdToDelete}")
	public String deleteUser(@PathVariable long userIdToDelete, @RequestHeader("Authorization") String token) {
		// remove the first part of the string which is "Bearer " 
		String jwtToken = token.replace("Bearer ", "").trim();

		try {
			memberService.deleteUser(userIdToDelete, jwtToken);
			return "SUCCESS: User has been deleted successfully";
		}catch (UserNotFoundException ex) {
			return ex.getMessage();
		}catch (Exception ex) {
			return ex.getMessage();
		}

	}
	

}
