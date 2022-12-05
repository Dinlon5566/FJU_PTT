<?php
	//非資料庫密碼
    $con = mysqli_connect('127.0.0.1:3306','root','password','fju_ptt');
    mysqli_query($con, 'SET NAMES utf8');
    if($con === false){
        die("ERROR: Could not connect. " . mysqli_connect_error());
    }
    else{
        return $con;
    }
    
?>