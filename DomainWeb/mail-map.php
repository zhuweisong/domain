<?php
	date_default_timezone_set('PRC');
	require_once('./login/auth.php');
	require_once('./login/config.php');
	require_once('util.php');
	
	$email = $_GET['email'];
	$admin = $_GET['admin'];
?>
<html>
    <head>
        <title>邮箱对应</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="./login/loginmodule.css" rel="stylesheet" type="text/css" />
		<script language="javascript" type="text/javascript" src="./My97DatePicker/WdatePicker.js"></script>
    </head>
    <body>
	<h1>Welcome <?php echo $_SESSION['SESS_FIRST_NAME'];?></h1>
	
		<form name="myForm" method="get" action="mail-map-exec.php">
			<b>邮箱对应:</b><br>
			邮箱：<input type="text" value="<?php echo $email ?>"  name='email'><br>
			所有人：<input type="text" value="<?php echo $admin ?>"  name='admin'><br>
			<input type="submit" name="Submit" value="确定">
		</form>
    </body>

</html>
