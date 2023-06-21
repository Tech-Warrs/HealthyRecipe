const createRecipeEndpoint = "http://localhost:8181/recipes";

$(document).ready(function () {
  $("#create-recipe-form").on("submit", async function (e) {
    e.preventDefault();

    const title = $("#title").val();
    const shortDesc = $("#shortDesc").val();
    const ingredients = $("#ingredients").val();
    const instructions = $("#instructions").val();
    const cookingDurationInMinutes = $("#cookingDurationInMinutes").val();
    const suitableFor = [];
    const notSuitableFor = [];
    $("input[name='suitableFor']:checked").each(function () {
      suitableFor.push($(this).val());
    });
    $("input[name='notSuitableFor']:checked").each(function () {
      notSuitableFor.push($(this).val());
    });

    const imageFile = $("#imageFile").prop("files")[0];

    let formData = new FormData();
    formData.append(
      "recipe",
      new Blob(
        [
          JSON.stringify({
            title,
            shortDesc,
            ingredients,
            instructions,
            cookingDurationInMinutes,
            suitableFor,
            notSuitableFor,
          }),
        ],
        {
          type: "application/json",
        }
      )
    );
    formData.append("image", imageFile);

    try {
      const user = JSON.parse(localStorage.getItem("user"));
      console.log("user", user);
      const token = user.token;
      console.log("token", token);

      const response = await fetch(createRecipeEndpoint, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (!response.ok) {
        throw new Error("Error in response from server");
      }

      const data = await response.json();

      $("#success-banner").text("Recipe successful created").show();
      
      // You can redirect the user to another page here or show a success message
      // window.location.href = "index.html";
    } catch (error) {
      console.error("Error:", error);
      // Show error to user
    }
  });
});
