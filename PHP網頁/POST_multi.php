<?php
$debug_mode = 1;
if (!isset($_POST) || !isset($_POST['ID'])) {
    echo "<script>alert('請輸入欲搜尋之ID');location.href='search_IDPI.php';</script>";
    exit();
}
$ID = $_POST['ID'];
$con = require_once "connect.php";
function showTable($result){
    echo "<table border='1' align='center' width='100%'>";
    echo "<tr>";
    echo "<th>ID1</th>";
    echo "<th>ID2</th>";
    echo "<th>IP</th>";
    echo "<th>出現次數</th>";
    echo "</tr>";
    while ($row = mysqli_fetch_assoc($result)) {
        echo "<tr>";
        echo "<td>" . $row["w1"] . "</td>";
        echo "<td>" . $row["w2"] . "</td>";
        echo "<td>" . $row["IP"] . "</td>";
        echo "<td>" . $row["times"] . "</td>";
        echo "</tr>";
    }
}
?>
<html>

<head>
    <title>搜尋ID-多重帳號</title>
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
        /*
            SELECT a1.writer as w1,a2.writer as w2,a1.IP as IP ,COUNT(a1.`idArticles`) 
            FROM articles a1,articles a2 
            WHERE a1.IP = a2.IP and a1.writer != a2.writer and a1.writer=‘b8****33' and a1.IP<>'None’ 
            GROUP BY a1.IP,a2.`writer` 
        */
        $sql="SELECT a1.writer as w1,a2.writer as w2,a1.IP as IP ,COUNT(a1.`idArticles`) as times
        FROM articles a1,articles a2
        WHERE a1.IP = a2.IP and a1.writer != a2.writer and a1.writer='$ID' and a1.IP<>'None'
        GROUP BY a1.IP,a2.`writer`";
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