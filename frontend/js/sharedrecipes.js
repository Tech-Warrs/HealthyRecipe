const getAllRecipesEndpoint = "http://localhost:8181/recipes";

$(document).ready(function () {
  fetchRecipes();
});

const fetchRecipes = async () => {
  try {
    const response = await fetch(getAllRecipesEndpoint, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error("HTTP error " + response.status);
    }

    const recipes = await response.json();

    displayRecipes(recipes);
  } catch (error) {
    console.log(error);
  }
};

const displayRecipes = (recipes) => {
  const recipeContainer = $("#sharedRecipeContainer");

  recipes.forEach((recipe) => {
    // Create the column div
    const colDiv = $("<div>").addClass("col");

    // Create the card div
    const cardDiv = $("<div>").addClass("card h-100");

    // Create the image tag and append to card div
    const imgTag = $("<img>")
      .addClass("card-img-top")
      .attr("src", recipe.imageUrl)
      .attr("alt", "Recipe image")
      .appendTo(cardDiv);

    // Create the card body div and append to card div
    const cardBodyDiv = $("<div>").addClass("card-body");

    // Create the card title and append to card body div
    $("<h5>").addClass("card-title").text(recipe.title).appendTo(cardBodyDiv);

    // Create the card text and append to card body div
    $("<p>").addClass("card-text").text(recipe.shortDesc).appendTo(cardBodyDiv);

    // Create the footer and append to card body div
    $("<footer>")
      .addClass("blockquote-footer")
      .text("Created by ")
      .append(
        $("<cite>").attr("title", "Source Title").text(recipe.user.username)
      )
      .appendTo(cardBodyDiv);

    // Append the card body div to the card div
    cardBodyDiv.appendTo(cardDiv);

    // Create the button and append to the card div
    $("<a>")
      .addClass("btn btn-primary")
      .attr("href", `recipe.html?recipeId=${recipe.recipeId}`)
      .text("Go to Recipe")
      .appendTo(cardDiv);

    // Append the card div to the column div
    cardDiv.appendTo(colDiv);

    // Append the column div to the container
    recipeContainer.append(colDiv);
  });
};

