<html>
<head>
<title>功能選單</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="60" align="center" valign="middle" bgcolor="#FFCC00" style="font-size: 40;">功能選單</td>
 </tr>
  <tr>
    <td height="30" align="center" valign="middle" bgcolor="#FFCC00"><a href="index.php">首頁</a></td>
  </tr>
  <tr>
    <td height="30" align="center" valign="middle" bgcolor="#FFCC00"><a href="./functionPage/search_IP.php">搜尋IP</a></td>
  </tr>
  <tr>
    <td height="30" align="center" valign="middle" bgcolor="#FFCC00"><a href="./functionPage/search_IDIP.php">搜尋ID-IP紀錄</a></td>
  </tr>
  <tr>
    <td height="30" align="center" valign="middle" bgcolor="#FFCC00"><a href="./functionPage/search_multi.php">搜尋ID-多重紀錄</a></td>
  </tr>
  <tr>
    <td height="30" align="center" valign="middle" bgcolor="#FFCC00"><a href="./functionPage/search_articleLog.php">搜尋ID-發文紀錄</a></td>
  </tr>
  <tr>
    <td height="30" align="center" valign="middle" bgcolor="#FFCC00"><a href="./functionPage/search_article.php">搜尋文章</a></td>
  </tr>
      <tr>
      <td>
        <h2>json說明</h2>
        <p>json_IP.php : 查詢IP</p>
        <p>curl -X POST --data "IP=140.136.%" 140.136.151.135/functionPage/json_IP.php</p>
        <p>json_IDIP.php : 查詢IP</p>
        <p>curl -X POST --data "ID=a0" 140.136.151.135/functionPage/json_IDIP.php</p>
        <p>json_article.php : 查文章</p>
        <p>
          curl -X POST --data "ID=M.1660997963.A.D6F" 140.136.151.135/functionPage/json_article.php
        </p>
        <p>json_articleLog.php : 查詢ID-發文紀錄</p>
        <p>curl -X POST --data "ID=a000000000" 140.136.151.135/functionPage/json_articleLog.php</p>
        <p>json_multi.php : 查詢ID-多重紀錄</p>
        <p>curl -X POST --data "ID=b8103033" 140.136.151.135/functionPage/json_multi.php</p>
      </td>
    </tr>
</body>
</html>