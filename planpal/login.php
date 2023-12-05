<?php
// Assuming your user credentials are stored in a database
// Establish database connection
include "connection.php";
// Check if the username and password are received via POST request
if (isset($_POST['email']) && isset($_POST['password'])) {
    $email = $_POST['email'];
    $password = $_POST['password'];

    // Perform a query to check if the credentials are valid
    $query = "SELECT * FROM `users` WHERE `email` = '$email' AND `password` = '$password'";
    $result = mysqli_query($con, $query);

    // Check if any rows were returned
    if (mysqli_num_rows($result) == 1) {
        // Fetch the user's name
        $user = mysqli_fetch_assoc($result);
        $name = $user['name'];

        // Login successful
        $response["success"] = 1;
        $response["message"] = "Login successful";
    } else {
        // Login failed
        $response["success"] = 0;
        $response["message"] = "Invalid credentials";
    }
} else {
    // Parameters not provided
    $response["success"] = 0;
    $response["message"] = "Required parameters missing";
}

// Return the response as JSON
echo json_encode($response);

// Close database connection
mysqli_close($con);
?>