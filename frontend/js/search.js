const APP_ID = "4262aadd";
const API_KEY = "4b7086bb3c6f33c3cd914e1948631c5f";


$(document).ready(function () {
  $("#searchButton").click(async function () {
    const recipeName = $("#recipeInput").val().trim();

    // Get the values of the checked health and diet filters
    const healthFilters = $("input[name=health]:checked")
      .map(function () {
        return this.value;
      })
      .get()
      .join("&");

    const dietFilters = $("input[name=diet]:checked")
      .map(function () {
        return this.value;
      })
      .get()
      .join("&");

    // Validate if at least one search option is filled
    if (!recipeName && !healthFilters && !dietFilters) {
      $("#recipeInput").attr(
        "placeholder",
        "Please fill in at least 1 search option"
      );
      return;
    }

    const recipes = await fetchRecipes(recipeName, healthFilters, dietFilters);
    displayRecipes(recipes);
  });
});

async function fetchRecipes(name, healthFilters, dietFilters) {
  let baseURL = `https://api.edamam.com/search?q=${name}&app_id=${APP_ID}&app_key=${API_KEY}&from=0&to=10`;

  if (healthFilters) {
    baseURL += `&health=${healthFilters}`;
  }

  if (dietFilters) {
    baseURL += `&diet=${dietFilters}`;
  }

  try {
    const response = await fetch(baseURL);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    return data.hits;
  } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
  }
}


function displayRecipes(recipes) {
  $("#recipeContainer").empty();

  recipes.forEach((recipeData) => {
    const recipe = recipeData.recipe;

    // Create card components
    const cardDiv = $("<div>").addClass("col-sm-6 col-md-4 col-lg-3 mb-4");
    const card = $("<div>").addClass("card");
    const img = $("<img>")
      .addClass("card-img-top")
      .attr("src", recipe.image)
      .attr("alt", recipe.label);
    const cardBody = $("<div>").addClass("card-body");
    const cardTitle = $("<h5>").addClass("card-title").text(recipe.label);
    const protein = $("<p>")
      .addClass("card-text")
      .text(`Protein: ${recipe.totalNutrients.PROCNT.quantity.toFixed(2)}`);
    const fat = $("<p>")
      .addClass("card-text")
      .text(`Fat: ${recipe.totalNutrients.FAT.quantity.toFixed(2)}`);
    const carbs = $("<p>")
      .addClass("card-text")
      .text(`Carbs: ${recipe.totalNutrients.CHOCDF.quantity.toFixed(2)}`);

    // Assemble card
    cardBody.append(cardTitle, protein, fat, carbs);
    card.append(img, cardBody);
    cardDiv.append(card);

    // Add card to container
    $("#recipeContainer").append(cardDiv);
  });
}
