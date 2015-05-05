<?php
	date_default_timezone_set('PRC');
	require_once('./util.php');
	require_once('./login/auth.php');
	require_once('./login/config.php');

	
	$domain = $_GET['domain'];
	$newprice = $_GET['newprice'];
	$newmemo = $_GET['newmemo'];
	$date_update = getToday();
	$user = $_SESSION['SESS_FIRST_NAME'];
	
	$msg_arr = explode("++",$domain);
	$domain=strtolower($msg_arr[0]);
	$type=$msg_arr[1];
	
	//echo $domain;
	
	if (strlen($type)>0) {
	} else {
		$type=getDomainType($domain);
	}

	$oldmemo=$msg_arr[2];


	//connect the DB
	connectDB();
	
	//Create query

	$newmemo = clean($newmemo);
	$newprice = clean($newprice);
	
	$qry="SELECT * FROM domainprice";
	$qry=$qry." WHERE domain='".$domain."' AND priceDate='".$date_update."'";
	//Check whether the query was successful or not
	//echo $qry;
	mysql_query("set names gb2312;");
	$result=mysql_query($qry);
	
	if($result) {
		if (mysql_num_rows($result) == 1) {
		$qry="UPDATE domainprice SET price=".$newprice.",memo='".$oldmemo."|".$user.":".$newmemo."',pricetype=7";
		$qry=$qry." WHERE domain='".$domain."' AND priceDate='".$date_update."'";
		$result=mysql_query($qry);
		//echo $qry;
		}
		else {
		$qry="INSERT INTO domainprice VALUES('".$domain."',".$type.",".$newprice.",7,100,'".$date_update."','".$user.":".$newmemo."')";
		$result=mysql_query($qry);
		//echo $qry;
		}
	}
	
	header("location: detail.php?domain=".$domain);
	exit();
?>

			
			