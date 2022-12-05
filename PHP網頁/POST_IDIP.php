<?php
$debug_mode = 1;
if (!isset($_POST) || !isset($_POST['ID'])) {
    echo "<script>alert('請輸入欲搜尋之ID');location.href='search_IDPI.php';</script>";
    exit();
}
$ID = $_POST['ID'];
$con = require_once "connect.php";

function showCache($result)
{
    // output data of each row as a table
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
    <title>搜尋ID-IP</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td height="30" align="center" valign="middle" bgcolor="#FFCC00">
                <h1>搜尋對象 :<?php echo $ID ?></h1>
            </td>
        </tr>
        <?php
        $sql = "SELECT * FROM fju_ptt.cache_idip WHERE userID='$ID'";
        $result = mysqli_query($con, $sql);
        if (!$result) {
            die($con->error);
        }

        if (mysqli_num_rows($result) > 0) {
            showCache($result);
        } else {
            echo "<script>alert('搜尋中，請稍後');</script>";
            require_once "reSearchIDIP.php";
            $sql = "SELECT * FROM fju_ptt.cache_idip WHERE userID='$ID'";
            $result = mysqli_query($con, $sql);
            if (mysqli_num_rows($result) > 0) {
                showCache($result);
            }else{
                echo "<script>alert('查無此ID');location.href='search_IDIP.php';</script>";
            }
        }
        ?>
</body>

</html>