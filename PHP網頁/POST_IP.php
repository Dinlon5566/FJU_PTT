<?php
$debug_mode = 1;
if (!isset($_POST) || !isset($_POST['IP'])) {
    //alert("請輸入欲搜尋之IP");
    echo "<script>alert('請輸入欲搜尋之IP');location.href='search_IP.php';</script>";
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
    echo "<table border='1' align='center' width='100%'>";
    echo "<tr>";
    echo "<th>IP</th>";
    echo "<th>用戶</th>";
    echo "<th>出現次數</th>";
    echo "</tr>";
    while ($row = mysqli_fetch_assoc($result)) {
        echo "<tr>";
        echo "<td>" . $row["IP"] . "</td>";
        echo "<td>" . $row["userID"] . "</td>";
        echo "<td>" . $row["times"] . "</td>";
        echo "</tr>";
    }
    echo "</table>";
}

?>

<html>

<head>
    <title>搜尋IP</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td height="30" align="center" valign="middle" bgcolor="#FFCC00">
                <h1>搜尋對象 :<?php echo $_POST["IP"] ?></h1>
            </td>
        </tr>
        <?php
        $sql = "SELECT * FROM fju_ptt.cache_ip WHERE target = '$IP' or IP='$IP' ORDER BY times DESC";
        $result = mysqli_query($con, $sql);
        if (!$result) {
            die($con->error);
        }

        if (mysqli_num_rows($result) > 0) {
            // output data of each row as a table
            showCache($result);
        } else {
            //若是沒有資料，則進行搜尋
            echo "<script>alert('搜尋中...請稍後');</script>";
            searchDB($IP);

            $sql = "SELECT * FROM fju_ptt.cache_ip WHERE target = '$IP' or IP like '$IP' ORDER BY times DESC";
            $result = mysqli_query($con, $sql);
            if (!$result) {
                die($con->error);
            }

            if (mysqli_num_rows($result) > 0) {
                showCache($result);
            } else {
                echo "<script>alert('查無資料');location.href='search_IP.php';</script>";
            }
        }
        /*
        echo "<p>按下按鈕重新搜尋</p>";
        echo "<form action='reSearchIP.php' method='post'>";
        echo "<form action='search_IP_POST.php' method='post'>";
        echo "<input type='hidden' name='IP' value='$IP'>";
        echo "<input type='submit' value='搜尋'>";
        echo "</form>";
*/
        mysqli_close($con);
        ?>

</html>