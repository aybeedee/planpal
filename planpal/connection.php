<?php
    $con = mysqli_connect("localhost","root","","planpal");

    if(!$con){
        die("could not connect to db". mysqli_connect_error());
    }
    
?>