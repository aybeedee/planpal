<?php
    include "connection.php";
    $response=array();
    if(isset($_POST["name"],$_POST["email"],$_POST["password"],$_POST["pictureUrl"])){
        $name=$_POST["name"];
        $email=$_POST["email"];
        $password=$_POST["password"];
        $pictureUrl=$_POST["pictureUrl"];
        $sql = "INSERT INTO `users`(`fullname`, `email`,`password`, `picUrl`) VALUES ('$name', '$email','$password', '$pictureUrl')";
        echo $sql;
        if(mysqli_query($con, $sql)){
            $response["status"]= 1;
            $response["message"]= "DATA INSERTED SUCCESSFULLY";
        }
        else{
            $response["status"]= 0;
            $response["message"]= "DATA FAILED";
        }
    }
    else{
        $response["status"]= 0;
        $response["message"]= "REQUIRED FIELD MISSING";
    }
    echo json_encode($response);

