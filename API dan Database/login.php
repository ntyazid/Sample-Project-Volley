<?php
require_once 'DB_Functions.php';
$db = new DB_Functions();

header('Content-Type: application/json');
// json response array
$response = array("error" => FALSE);
$json = file_get_contents('php://input');
$obj = json_decode($json);
$email = $obj->{'email'};
$password = $obj->{'password'};

 
if ($email !== null && $password !== null) {
 
 
    // get the user by email and password
    $user = $db->getUserByEmailAndPassword($email, $password);
 
    if ($user != false) {
        // use is found
        $response["error"] = FALSE;
        $response["kode"] = $user["kode"];
        $response["user"]["name"] = $user["name"];
        $response["user"]["email"] = $user["email"];
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters email or password is missing!";
    echo json_encode($response);
}
?>