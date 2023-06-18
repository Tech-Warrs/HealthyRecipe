package com.wileyedge.healthyrecipe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/health/{healthType}")
	public List<Recipe> getRecipesByHealthType(@PathVariable String healthType) {
		return recipeService.getRecipesByHealthType(healthType);
	}

}

