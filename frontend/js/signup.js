const signupEndpoint = "http://localhost:8181/users/signup";

$(document).ready(function () {
  // Register form submit event
  $("#register-form").on("submit", async function (e) {
    e.preventDefault();

    // Get form values
    const username = $("#username").val();
    const email = $("#email").val();
    const password = $("#password").val();
    const firstname = $("#firstname").val();
    const lastname = $("#lastname").val();

    try {
      // Perform registration request using Fetch API
      const response = await fetch(signupEndpoint, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username,
          email,
          password,
          firstName: firstname,
          lastName: lastname,
        }),
      });
      
      if (response.ok) {
        // Handle registration success
        console.log("Registration successful");
        // Display success banner
        $("#success-banner").text("Sign up successful").show();
        // Redirect to the home page after a delay
        setTimeout(function () {
          window.location.href = "index.html"; // Replace with the actual home page URL
        }, 2000); // Delay in milliseconds (3 seconds in this example)
      } else {
        // Handle registration failure
        const error = await response.text();
        console.error("Registration failed:", error);
        // Display error message to the user
      }
    } catch (error) {
      console.error("Registration failed:", error);
      // Display error message to the user
    }
  });
});
