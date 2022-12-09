<?php
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
        if (array_key_exists('w1', $row)) {
            $data['w1'] = $row['w1'];
        }
        if (array_key_exists('w2', $row)) {
            $data['w2'] = $row['w2'];
        }
        if (array_key_exists('IP', $row)) {
            $data['IP'] = $row['IP'];
        }
        if (array_key_exists('times', $row)) {
            $data['times'] = $row['times'];
        }
        $json_array[] = $data;
    }
}

/*
            SELECT a1.writer as w1,a2.writer as w2,a1.IP as IP ,COUNT(a1.`idArticles`) 
            FROM articles a1,articles a2 
            WHERE a1.IP = a2.IP and a1.writer != a2.writer and a1.writer=‘b8****33' and a1.IP<>'None’ 
            GROUP BY a1.IP,a2.`writer` 
        */
$sql = "SELECT a1.writer as w1,a2.writer as w2,a1.IP as IP ,COUNT(a1.`idArticles`) as times
        FROM articles a1,articles a2
        WHERE a1.IP = a2.IP and a1.writer != a2.writer and a1.writer='$ID' and a1.IP<>'None'
        GROUP BY a1.IP,a2.`writer`";
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
