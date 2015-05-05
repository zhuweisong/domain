<?php
	date_default_timezone_set('PRC');
	require_once('./login/auth.php');
	require_once('./login/config.php');
?>
<html>
    <head>
        <title>行情查询</title>
		<script language="javascript" type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="./login/loginmodule.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
	<h1>Welcome <?php echo $_SESSION['SESS_FIRST_NAME'];?></h1>
		<a href="sell.php">成交查询</a> | 
		<a href="auction.php">拍卖查询</a> | 
		<a href="price.php">在卖查询</a> |
		<a href="memo-domain.php">已询价域名</a> | 
		<a href="detail-search.php">详情查询</a> | 
		<a href="statis.php">趋势统计</a> | 
		<a href="./login/logout.php">退出</a>
		&nbsp;<br>
		<form name="myForm" method="get" action="detail.php">
			<b>详情查询:</b>
			<input type="text" name='domain'>如:8888.com
			<input type="submit" name="Submit" value="查询"> 
		</form>
    </body>
</html>
