const imgUrl = "https://foodimagesbucket.s3.ap-southeast-2.amazonaws.com/";

$(document).ready(async function () {
  const urlParams = new URLSearchParams(window.location.search);
  const recipeId = urlParams.get("recipeId");

  try {
    const response = await fetch(`http://localhost:8181/recipes/${recipeId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${getToken()}`,
      },
    });

    if (!response.ok) {
      throw new Error("HTTP error " + response.status);
    }

    const recipe = await response.json();
    console.log(recipe.imageUrl)

    // Create jQuery objects for each section of the recipe
    const title = $("<h2>").text(recipe.title);
    const image = $("<img>", {
      class: "img-fluid mb-3",
      src: `${imgUrl}${recipe.imageUrl}`,
      alt: recipe.title,
    });
    const shortDesc = $("<p>").text(recipe.shortDesc);
    const ingredients = $("<div>").append(
      $("<h4>").text("Ingredients"),
      $("<p>").text(recipe.ingredients)
    );
    const instructions = $("<div>").append(
      $("<h4>").text("Instructions"),
      $("<p>").text(recipe.instructions)
    );
    const suitableFor = $("<div>").append(
      $("<h4>").text("Suitable For"),
      $("<ul>").append(
        recipe.suitableFor.map((category) => $("<li>").text(category))
      )
    );
    const notSuitableFor = $("<div>").append(
      $("<h4>").text("Not Suitable For"),
      $("<ul>").append(
        recipe.notSuitableFor.map((category) => $("<li>").text(category))
      )
    );
    const cookingDuration = $("<p>").text(
      `Cooking duration: ${recipe.cookingDurationInMinutes} minutes`
    );

    // Append each section to the recipe container
    const recipeContainer = $("#recipe-container");
    recipeContainer.empty();
    recipeContainer.append(
      title,
      image,
      shortDesc,
      ingredients,
      instructions,
      suitableFor,
      notSuitableFor,
      cookingDuration
    );
  } catch (error) {
    console.log(error);
  }
});

function getToken() {
  const user = JSON.parse(localStorage.getItem("user"));
  return user.token;
}
