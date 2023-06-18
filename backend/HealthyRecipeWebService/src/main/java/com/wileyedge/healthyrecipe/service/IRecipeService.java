package com.wileyedge.healthyrecipe.service;

import java.util.List;
import java.util.Optional;

import com.wileyedge.healthyrecipe.model.Recipe;

public interface IRecipeService {

	Recipe createRecipe(Recipe recipe);

	List<Recipe> getAllRecipes();

	List<Recipe> getRecipesByUserId(long userId);

	Recipe getRecipeById(long recipeId);

	List<Recipe> getRecipesByHealthType(String healthType);

}
