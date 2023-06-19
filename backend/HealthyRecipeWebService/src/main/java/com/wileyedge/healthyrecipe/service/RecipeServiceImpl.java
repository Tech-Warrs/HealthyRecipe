package com.wileyedge.healthyrecipe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wileyedge.healthyrecipe.dao.RecipeRepository;
import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.exception.RecipeNotFoundException;
import com.wileyedge.healthyrecipe.exception.UnauthorizedAccessException;
import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.model.HealthCategory;
import com.wileyedge.healthyrecipe.model.Recipe;
import com.wileyedge.healthyrecipe.model.User;

@Service
public class RecipeServiceImpl implements IRecipeService {

	private RecipeRepository recipeRepository;
	private UserRepository userRepository;
	private AuthService authService;

	@Autowired
	public RecipeServiceImpl(RecipeRepository recipeRepository,UserRepository userRepository, AuthService authService) {
		this.recipeRepository = recipeRepository;
		this.userRepository = userRepository;
		this.authService = authService;
	}

	@Override
	public List<Recipe> getAllRecipes(){
		return recipeRepository.findAll();
	}

	@Override
	public Recipe getRecipeById(long recipeId) {
		return recipeRepository.findById(recipeId)
				.orElseThrow(() -> new RecipeNotFoundException("ID : " + recipeId));
	}


	@Override
	public List<Recipe> getRecipesByUserId(long userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {
			return recipeRepository.findByUserId(userId);
		}
		throw new UserNotFoundException("User ID : " + userId);
	}


	@Override
	public List<Recipe> getRecipesByHealthCategories(List<HealthCategory> healthCategories) {
		return recipeRepository.findBySuitableForIn(healthCategories);
	}

	@Override
	public Recipe createRecipe(Recipe recipe, String token) {

		//Validate token
		User loggedInUser = authService.isTokenValid(token);

		//Associate user with recipe
		recipe.setUser(loggedInUser);

		return recipeRepository.save(recipe);
	}
		
	@Override
	public Recipe updateRecipe(long recipeId, Recipe updatedRecipe, String token) {
		// Validate token
		User loggedInUser = authService.isTokenValid(token);

		// Get the recipe by ID
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new RecipeNotFoundException("ID : " + recipeId));

		// Check if the logged-in user is the owner of the recipe
		if (!recipe.getUser().equals(loggedInUser)) {
			throw new UnauthorizedAccessException("You are not authorized to update this recipe");
		}

		// Update the recipe details if provided and not empty
	    if (updatedRecipe.getTitle() != null && !updatedRecipe.getTitle().isEmpty()) {
	        recipe.setTitle(updatedRecipe.getTitle());
	    }
	    if (updatedRecipe.getShortDesc() != null && !updatedRecipe.getShortDesc().isEmpty()) {
	        recipe.setShortDesc(updatedRecipe.getShortDesc());
	    }
	    if (updatedRecipe.getIngredients() != null && !updatedRecipe.getIngredients().isEmpty()) {
	        recipe.setIngredients(updatedRecipe.getIngredients());
	    }
	    if (updatedRecipe.getInstructions() != null && !updatedRecipe.getInstructions().isEmpty()) {
	        recipe.setInstructions(updatedRecipe.getInstructions());
	    }
	    if (updatedRecipe.getSuitableFor() != null && !updatedRecipe.getSuitableFor().isEmpty()) {
	        recipe.setSuitableFor(updatedRecipe.getSuitableFor());
	    }
	    if (updatedRecipe.getNotSuitableFor() != null && !updatedRecipe.getNotSuitableFor().isEmpty()) {
	        recipe.setNotSuitableFor(updatedRecipe.getNotSuitableFor());
	    }
	    if (updatedRecipe.getCookingDurationInMinutes() != 0) {
	        recipe.setCookingDurationInMinutes(updatedRecipe.getCookingDurationInMinutes());
	    }

		return recipeRepository.save(recipe);
	}
	
	@Override
	public void deleteRecipe(long recipeId, String token) {
		// Validate token
		User loggedInUser = authService.isTokenValid(token);

		// Get the recipe by ID
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new RecipeNotFoundException("ID : " + recipeId));

		 // Check if the logged-in user is the owner of the recipe
		if (!recipe.getUser().equals(loggedInUser)) {
			throw new UnauthorizedAccessException("You are not authorized to delete this recipe");
		}

		recipeRepository.delete(recipe);
	}





}
