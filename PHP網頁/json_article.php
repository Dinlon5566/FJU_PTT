<?php
header("Content-Type: application/json; charset=utf-8");

$debug_mode = 1;
if (!isset($_POST) || !isset($_POST['ID'])) {
    echo "{'error':'no ID'}";
    exit();
}
$ID = $_POST['ID'];
$con = require_once "connect.php";
function showTable($result)
{
    $data=array();
    while ($row = $result->fetch_assoc()) {
        
        if (array_key_exists('board', $row)) {
            $data['board'] = $row['board'];
        }
        if (array_key_exists('idArticles', $row)) {
            $data['idArticles'] = $row['idArticles'];
        }
        if (array_key_exists('writer', $row)) {
            $data['writer'] = $row['writer'];
        }
        if (array_key_exists('title', $row)) {
            $data['title'] = $row['title'];
        }
        if (array_key_exists('time', $row)) {
            $data['time'] = $row['time'];
        }
        if (array_key_exists('IP', $row)) {
            $data['IP'] = $row['IP'];
        }
        if (array_key_exists('body', $row)) {
            $data['body'] = $row['body'];
        }
        //$data=$row;
        $json_array[] = $data;
    }
    echo json_encode($json_array, JSON_UNESCAPED_UNICODE);
}
/*
        SELECT * FROM fju_ptt.articles where idArticles='M.1670224062.A.B62' ;
        */
$sql = "SELECT * FROM fju_ptt.articles where idArticles='$ID'";
$result = mysqli_query($con, $sql);
if (!$result) {
    echo "Error: " . $sql . "<br>" . mysqli_error($con);
    exit();
}
if (mysqli_num_rows($result) > 0) {
    showTable($result);
} else {
    echo "{'error':'no result'}";    
}
