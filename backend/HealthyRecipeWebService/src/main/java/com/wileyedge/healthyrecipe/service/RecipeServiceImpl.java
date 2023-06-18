package com.wileyedge.healthyrecipe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wileyedge.healthyrecipe.dao.RecipeRepository;
import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.exception.RecipeNotFoundException;
import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.model.Recipe;
import com.wileyedge.healthyrecipe.model.User;

@Service
public class RecipeServiceImpl implements IRecipeService {

	private RecipeRepository recipeRepository;
	private UserRepository userRepository;

	@Autowired
	public RecipeServiceImpl(RecipeRepository recipeRepository,UserRepository userRepository) {
		this.recipeRepository = recipeRepository;
		this.userRepository = userRepository;
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
	public Recipe createRecipe(Recipe recipe) {
		return recipeRepository.save(recipe);
	}
	
	@Override
    public List<Recipe> getRecipesByHealthType(String healthType) {
        return recipeRepository.findBySuitableFor(healthType);
    }

}
