$(document).ready(function () {
  // Check if user is logged in
  const user = getUserFromLocalStorage(); // Function to get user info from local storage
  if (user) {
    // User is logged in
    console.log("user logged in")
    showLoggedInUser(user);
  } else {
    // User is not logged in
    console.log("user not logged in");
    showLoggedOutUser();
  }

  $("#user-nav").on("click", "#logout-button", function (e) {
    e.preventDefault();
    // Clear user data from local storage
    localStorage.removeItem("user");

    // Restore login/register links and remove welcome message
    $("#welcome-message").text("");
    $("#login-button").show();
    $("#signup-button").show();
    $("#logout-button").hide();

    $("#success-banner").text("Logout successful").show().delay(2000).fadeOut();
  });
});

function showLoggedInUser(user) {
  const userNav = $("#user-nav");
  const welcomeMessage = $("<span>")
    .addClass("nav-link")
    .text(`Welcome, ${user.user.firstName}`)
    .attr("id", "welcome-message");
  userNav.append(welcomeMessage);

  $("#login-button").hide();
  $("#signup-button").hide();

  const logoutNav = $("<li>").addClass("nav-item").attr("id", "logout-button");
  userNav.append(logoutNav);
  logoutNav.append(
    $("<a>").addClass("nav-link").attr("href", "index.html").text("Logout")
  );
}

function showLoggedOutUser() {
  // No action needed, as the initial HTML has the Login and Signup buttons
}

function getUserFromLocalStorage() {
  // Implement this function to retrieve user info from local storage
  // If user is logged in, return the user object, otherwise return null
  // Example implementation:
  const userJson = localStorage.getItem("user");
  return JSON.parse(userJson);
}
