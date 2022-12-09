<?php
$debug_mode = 1;
header("Content-Type: application/json; charset=utf-8");
if (!isset($_POST) || !isset($_POST['IP'])) {
    //alert("請輸入欲搜尋之IP");
    echo "{'error':'no IP'}";
    exit();
}
$IP = $_POST['IP'];
$con = require_once "connect.php";

function searchDB($IP)
{
    //to reSearchIP.php
    require_once "reSearchIP.php";
}

function showCache($result)
{

    $data = array();
    while ($row = $result->fetch_assoc()) {
        if (array_key_exists('IP', $row)) {
            $data['IP'] = $row['IP'];
        }
        if (array_key_exists('userID', $row)) {
            $data['userID'] = $row['userID'];
        }
        if (array_key_exists('times', $row)) {
            $data['times'] = $row['times'];
        }
        $json_array[] = $data;
    }
    echo json_encode($json_array, JSON_UNESCAPED_UNICODE);
}


$sql = "SELECT * FROM fju_ptt.cache_ip WHERE target = '$IP' or IP='$IP' ORDER BY times DESC";
$result = mysqli_query($con, $sql);
if (!$result) {
    die($con->error);
}

if (mysqli_num_rows($result) > 0) {
    // output data of each row as a table
    showCache($result);
} else {
    searchDB($IP);
    $sql = "SELECT * FROM fju_ptt.cache_ip WHERE target = '$IP' or IP like '$IP' ORDER BY times DESC";
    $result = mysqli_query($con, $sql);
    if (!$result) {
        die($con->error);
    }

    if (mysqli_num_rows($result) > 0) {
        showCache($result);
    } else {
        echo "{'error':'no result'}'}";
    }
}

mysqli_close($con);
