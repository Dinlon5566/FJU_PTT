
<?php
$debug_mode = 1;
if (!isset($_POST) || !isset($_POST['ID'])) {
    echo "{'error':'no ID'}";
    exit();
}
$ID = $_POST['ID'];
$con = require_once "connect.php";

function showCache($result)
{
    $data=array();
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
    echo json_encode($json_array);
}


$sql = "SELECT * FROM fju_ptt.cache_idip WHERE userID='$ID' ORDER BY times DESC";
$result = mysqli_query($con, $sql);
if (!$result) {
    die($con->error);
}

if (mysqli_num_rows($result) > 0) {
    showCache($result);
} else {
    //echo "<script>alert('搜尋中，請稍後');</script>";
    require_once "reSearchIDIP.php";
    $sql = "SELECT * FROM fju_ptt.cache_idip WHERE userID='$ID' ORDER BY times DESC";
    $result = mysqli_query($con, $sql);
    if (mysqli_num_rows($result) > 0) {
        showCache($result);
    } else {
        echo "{'error':'no result'}";
    }
}
?>