<?php
    $debugMode=0;
    if(!isset($_POST['IP'])){
        exit();
    }
    $IP = $_POST['IP'];
    $con = require "connect.php";

    //if IP length <4, then exit
    if(strlen($IP)<3){
        echo "<script>alert('IP長度不足');</script>";
        return;
    }

    /*
SELECT `userID`,COUNT(`idArticles`) as
FROM `messages` 
where `IP` LIKE '$IP’ 
GROUP by `userID` 
ORDER by COUNT(`idArticles`) desc;
    */

    $sql="SELECT `userID`,COUNT(`idArticles`) as `times`,IP FROM `messages` where `IP` LIKE '$IP' GROUP by `userID`,IP";
    $result = mysqli_query($con, $sql);
    if (!$result) {
        echo "Error: " . $sql . "<br>" . mysqli_error($con);
        die($con->error);
    }
    //save to cache_ip
    if(mysqli_num_rows($result) > 0){
        while($row = mysqli_fetch_assoc($result)){
            $sql = "INSERT INTO `cache_ip`(`target`,`IP`, `userID`, `times`) VALUES ('$IP','".$row['IP']."', '".$row['userID']."', '".$row['times']."')";
            $ins = mysqli_query($con, $sql);
            if (!$ins&&$debugMode) {
                echo "Error: " . $sql . "<br>" . mysqli_error($con);
            }
        }
    }
    mysqli_close($con);
   return;
?>