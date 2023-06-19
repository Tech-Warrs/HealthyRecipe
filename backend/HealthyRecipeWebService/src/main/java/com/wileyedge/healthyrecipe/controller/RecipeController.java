package com.wileyedge.healthyrecipe.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.wileyedge.healthyrecipe.model.HealthCategory;
import com.wileyedge.healthyrecipe.model.Recipe;
import com.wileyedge.healthyrecipe.service.IRecipeService;


@RestController
@RequestMapping("/recipes")
public class RecipeController {

	private IRecipeService recipeService;


	@Autowired
	public RecipeController(IRecipeService recipeService) {
		this.recipeService = recipeService;
	}
	
	@PostMapping("")
	public Recipe createRecipe(@RequestBody Recipe recipe, @RequestHeader("Authorization") String token) {
	    return recipeService.createRecipe(recipe, token);
	}
		
	@GetMapping
	public List<Recipe> getAllRecipes() {
		return recipeService.getAllRecipes();
	}
	
	@GetMapping("/{recipeId}")
	public Recipe getRecipesById(@PathVariable long recipeId) {
		return recipeService.getRecipeById(recipeId);
	}

	@GetMapping("/users/{userId}")
	public List<Recipe> getRecipesByUserId(@PathVariable long userId) {
		return recipeService.getRecipesByUserId(userId);
	}

	@GetMapping("/health")
	public List<Recipe> getRecipesByHealthType(@RequestBody List<String> healthTypes) {
	    List<HealthCategory> healthCategories = new ArrayList<>();
	    for (String healthType : healthTypes) {
	        healthCategories.add(HealthCategory.valueOf(healthType.toUpperCase()));
	    }
	    return recipeService.getRecipesByHealthCategories(healthCategories);
	}
	
	@DeleteMapping("/{recipeId}")
	public String deleteRecipe(@PathVariable long recipeId, @RequestHeader("Authorization") String token) {
	    recipeService.deleteRecipe(recipeId, token);
	    return "SUCCESS: The Recipe has been deleted successfully";	
	}

	@PutMapping("/{recipeId}")
	public Recipe updateRecipe(@PathVariable long recipeId, @RequestBody Recipe updatedRecipe, @RequestHeader("Authorization") String token) {
	    return recipeService.updateRecipe(recipeId, updatedRecipe, token);
	}
	
}

