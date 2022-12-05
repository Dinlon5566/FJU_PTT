<?php
$debugMode=0;
if(!isset($_POST)||!isset($_POST['ID'])){
    echo "<script>alert('請輸入欲搜尋之ID');location.href='search_IDPI.php';</script>";
    exit();
}
$ID=$_POST['ID'];
$con=require "connect.php";

/*
SELECT IP,COUNT(`idArticles`) as '出沒次數’ 
FROM messages 
WHERE `userID`=‘don****12’ 
GROUP by IP 
ORDER BY COUNT(`idArticles`) desc;

*/

$sql="SELECT IP,COUNT(`idArticles`) as 'times' FROM messages WHERE `userID`='$ID' GROUP by IP ;";
$result=mysqli_query($con,$sql);
if(!$result){
    die($con->error);
}
if(mysqli_num_rows($result)>0){
    while($row=mysqli_fetch_assoc($result)){
        $sql="INSERT INTO cache_idip (IP,userID,times) VALUES ('{$row['IP']}','$ID','{$row['times']}');";
        $result2=mysqli_query($con,$sql);
        if(!$result2){
           echo $con->error;
        }
    }
}
return ;