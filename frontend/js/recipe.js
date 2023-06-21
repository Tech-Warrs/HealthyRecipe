const APP_ID = "4262aadd";
const APP_KEY = "4b7086bb3c6f33c3cd914e1948631c5f";

$(document).ready(function () {
  // Get the recipe URI from the query parameters
  const urlParams = new URLSearchParams(window.location.search);
  const encodedRecipeURI = urlParams.get("uri");
  const recipeURI = decodeURIComponent(encodedRecipeURI);
  console.log(recipeURI)
  // Call the function to fetch and display the recipe details
  fetchRecipeDetails(recipeURI);
});

async function fetchRecipeDetails(recipeURI) {
  try {
    const encodedURI = encodeURIComponent(recipeURI);
    const url = `https://api.edamam.com/api/recipes/v2/${encodedURI}?app_id=${APP_ID}&app_key=${APP_KEY}`;
    const response = await fetch(url);
    const recipe = await response.json();

    // Call the displayRecipe function to render the recipe details
    displayRecipe(recipe);
  } catch (error) {
    console.error("There was a problem fetching the recipe details: ", error);
  }
}

function displayRecipe(recipe) {
  const recipeContainer = $("#recipe-container");
  recipeContainer.empty(); // Clear any previous content

  // Create the elements to display the recipe details
  const recipeTitle = $("<h1>").text(recipe.recipe.label);
  const recipeImage = $("<img>")
    .attr("src", recipe.recipe.image)
    .attr("alt", recipe.recipe.label);
  const recipeIngredients = $("<ul>").addClass("recipe-ingredients");
  recipe.recipe.ingredientLines.forEach((ingredient) => {
    const ingredientItem = $("<li>").text(ingredient);
    recipeIngredients.append(ingredientItem);
  });

  // Append the elements to the recipe container
  recipeContainer.append(recipeTitle, recipeImage, recipeIngredients);
}
