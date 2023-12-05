<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    require_once('connection.php');

    $encoded_string = $_POST["encoded_string"];
    $imageName = $_POST["image_name"];

    $bin = base64_decode($encoded_string);

    $upload_folder = "pictures/";
    $path = $upload_folder . $imageName . ".jpg";
    $actualpath = "/planpal/$path";

    $file = fopen($path, 'wb');
    $is_written = fwrite($file, $bin);
    fclose($file);

    $response["status"] = 1;
    $response["pictureUrl"] = $actualpath;
} else {
    $response["status"] = 0;
    $response["message"] = "INVALID REQUEST METHOD";
}

echo json_encode($response);
?>
