package com.wileyedge.healthyrecipe.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.wileyedge.healthyrecipe.dao.RecipeRepository;
import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.exception.RecipeNotFoundException;
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






}
