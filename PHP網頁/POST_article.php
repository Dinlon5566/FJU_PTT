<?php
$debug_mode = 1;
if (!isset($_POST) || !isset($_POST['ID'])) {
    echo "<script>alert('請輸入欲搜尋之文章ID');location.href='search_IDPI.php';</script>";
    exit();
}
$ID = $_POST['ID'];
$con = require_once "connect.php";
function showTable($result){
    echo "<table border='1' align='center' width='100%'>";
    echo "<tr>";
    echo "<th>看板</th>";
    echo "<th>代號</th>";
    echo "<th>作者</th>";
    echo "<th>標題</th>";
    echo "<th>時間</th>";
    echo "<th>IP</th>";
    echo "<th>內容</th>";
    echo "</tr>";
    while ($row = mysqli_fetch_assoc($result)) {
        echo "<tr>";
        echo "<td>" . $row["board"] . "</td>";
        echo "<td>" . $row["idArticles"] . "</td>";
        echo "<td>" . $row["writer"] . "</td>";
        echo "<td>" . $row["title"] . "</td>";
        echo "<td>" . $row["time"] . "</td>";
        echo "<td>" . $row["IP"] . "</td>";
        echo "<td>" . $row["body"] . "</td>";
        echo "</tr>";
    }
}
?>
<html>

<head>
    <title>搜尋文章</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td height="30" align="center" valign="middle" bgcolor="#FFCC00">
                <h1>搜尋ID :<?php echo $ID ?></h1>
            </td>
        </tr>
        <?php
        /*
        SELECT * FROM fju_ptt.articles where idArticles='M.1670224062.A.B62' ;
        */
        $sql="SELECT * FROM fju_ptt.articles where idArticles='$ID'";
        $result = mysqli_query($con, $sql);
        if (!$result) {
            echo "Error: " . $sql . "<br>" . mysqli_error($con);
            exit();
        }
        if(mysqli_num_rows($result) > 0){
            showTable($result);
        }else{
            echo "<script>alert('查無資料');location.href='index.php';</script>";
        }
        ?>
</body>

</html>