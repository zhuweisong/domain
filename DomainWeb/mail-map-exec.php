<?php
	date_default_timezone_set('PRC');
	require_once('./login/auth.php');
	require_once('./login/config.php');
	require_once('util.php');
	
	$email = $_GET['email'];
	$admin = $_GET['admin'];	
	
	connectDB();

	$qry="SELECT * FROM mailmap WHERE mail='".$email."'";

	//Check whether the query was successful or not
	//echo $qry;
	mysql_query("set names gb2312;");
	$result=mysql_query($qry);
	
	if($result) {
		$user=$_SESSION['SESS_FIRST_NAME'];
		if (mysql_num_rows($result) <= 0) {
			$qry="INSERT INTO mailmap VALUES('".$email."','".$admin."','".$user."','".getToday()."')";
			
			$result=mysql_query($qry);
			//echo $qry." ".$result;
		}
		else  {
			$qry="UPDATE  mailmap SET admin='".$admin."', memowho='".$user."', memowhen='".getToday()."' WHERE mail='".$email."'";
			//echo $qry;
			$result=mysql_query($qry);
		}
	}
	echo '更新成功';
?>

	

