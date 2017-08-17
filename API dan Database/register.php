<?php
 
require_once 'DB_Functions.php';
$db = new DB_Functions();
 
header('Content-Type: application/json');
// json response array
$response = array("error" => FALSE);


$json = file_get_contents('php://input');
$obj = json_decode($json);
$name = $obj->{'name'};
$email = $obj->{'email'};
$password = $obj->{'password'};
 
if ($name !== null && $email !== null && $password !== null) {
 
    // check if user is already existed with the same email
    if ($db->isUserExisted($email)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $email;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($name, $email, $password);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>