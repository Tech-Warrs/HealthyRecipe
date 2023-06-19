package com.wileyedge.healthyrecipe.service;

import java.util.List;

import com.wileyedge.healthyrecipe.model.HealthCategory;
import com.wileyedge.healthyrecipe.model.Recipe;

public interface IRecipeService {

	List<Recipe> getAllRecipes();

	List<Recipe> getRecipesByUserId(long userId);

	Recipe getRecipeById(long recipeId);

	List<Recipe> getRecipesByHealthCategories(List<HealthCategory> healthCategories);

	Recipe createRecipe(Recipe recipe, String token);

	void deleteRecipe(long recipeId, String token);

	Recipe updateRecipe(long recipeId, Recipe updatedRecipe, String token);

//	List<Recipe> getRecipesByUserId(Long userId);
	   

}
