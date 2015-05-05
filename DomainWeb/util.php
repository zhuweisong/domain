<?php
	date_default_timezone_set('PRC');
	require_once('login/config.php');
	
	//
    define('DOMAIN_ISNOT_PRICE', 0);
    define('DOMAIN_MAKE_OFFER', 2);
    define('DOMAIN_SALE_OFFER', 4);
    define('DOMAIN_BIN_BUYNOW', 5);
    define('DOMAIN_HOT_SALE', 6);
    define('DOMAIN_QQ_BID', 7);
    define('DOMAIN_BIN_AUCTION_WITH_PRICE', 9);
    define('DOMAIN_BIN_AUCTION_ONE', 10);
	
    define('DOMAIN_PRICE_FROM_KingMing', 1);
    define('DOMAIN_PRICE_FROM_ENAME', 2);
    define('DOMAIN_PRICE_FROM_AMing', 3);
    define('DOMAIN_PRICE_FROM_ARTIFICIAL_BID', 100);


	define('DOMAIN_TYPE_NOT_DEF', 0);
	define('DOMAIN_TYPE_1_COM', 1);
	define('DOMAIN_TYPE_2_COM', 2);
	define('DOMAIN_TYPE_3_COM', 3);
	define('DOMAIN_TYPE_4_COM', 4);
	define('DOMAIN_TYPE_5_COM', 5);
	define('DOMAIN_TYPE_6_COM', 6);
	define('DOMAIN_TYPE_7_COM', 7);
	define('DOMAIN_TYPE_8_COM', 8);
	define('DOMAIN_TYPE_9_COM', 9);
	define('DOMAIN_TYPE_10_COM', 10);
	
	define('DOMAIN_TYPE_1_CN', 11);
	define('DOMAIN_TYPE_2_CN', 12);
	define('DOMAIN_TYPE_3_CN', 13);
	define('DOMAIN_TYPE_4_CN', 14);
	define('DOMAIN_TYPE_5_CN', 15);
	define('DOMAIN_TYPE_6_CN', 16);
	define('DOMAIN_TYPE_7_CN', 17);
	define('DOMAIN_TYPE_8_CN', 18);
	define('DOMAIN_TYPE_9_CN', 19);
	define('DOMAIN_TYPE_10_CN', 20);
	
	define('DOMAIN_TYPE_1_NET', 21);
	define('DOMAIN_TYPE_2_NET', 22);
	define('DOMAIN_TYPE_3_NET', 23);
	define('DOMAIN_TYPE_4_NET', 24);
	define('DOMAIN_TYPE_5_NET', 25);
	define('DOMAIN_TYPE_6_NET', 26);
	define('DOMAIN_TYPE_7_NET', 27);
	define('DOMAIN_TYPE_8_NET', 28);
	define('DOMAIN_TYPE_9_NET', 29);
	define('DOMAIN_TYPE_10_NET', 30);
	
	define('DOMAIN_TYPE_PIN', 100);
	
	define('DOMAIN_TYPE_GENERAL', 1);
	define('DOMAIN_TYPE_AAB', 2);
	define('DOMAIN_TYPE_ABA', 4);
	define('DOMAIN_TYPE_ABB', 8);
	
	define('DOMAIN_TYPE_AABC', 16);
	define('DOMAIN_TYPE_ABAC', 32);
	define('DOMAIN_TYPE_ABBC', 64);
	define('DOMAIN_TYPE_ABCC', 128);	
	define('DOMAIN_TYPE_ABCA', 256);		
	define('DOMAIN_TYPE_ABCB', 512);		
	
	function connectDB() {
			//Connect to mysql server
		$link = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
		if(!$link) {
			die('Failed to connect to server: ' . mysql_error());
		}
		
		//Select database
		$db = mysql_select_db(DB_DATABASE);
		if(!$db) {
			die("Unable to select database");
		}
	}
	
		//Function to sanitize values received from the form. Prevents SQL injection
	function clean($str) {
		$str = @trim($str);
		if(get_magic_quotes_gpc()) {
			$str = stripslashes($str);
		}
		return mysql_real_escape_string($str);
	}
	
	function getPriceType($type) {
		switch ($type) {
			case DOMAIN_ISNOT_PRICE:
			return "没有定价";
			case DOMAIN_MAKE_OFFER:
			return "买家询价";
			case DOMAIN_SALE_OFFER:
			return "卖家询价";
			case DOMAIN_BIN_BUYNOW:
			return "一口价";
			case DOMAIN_HOT_SALE:
			return "热卖";
			case DOMAIN_QQ_BID:
			return "人工询价";
			case DOMAIN_BIN_AUCTION_WITH_PRICE:
			return "带底价拍";
			case DOMAIN_BIN_AUCTION_ONE:
			return "1元拍";
		}
	}
	
	function getFromWeb($type) {
		switch ($type) {
			case DOMAIN_PRICE_FROM_KingMing:
			return "4N";
			case DOMAIN_PRICE_FROM_ENAME:
			return "Ename";
			case DOMAIN_PRICE_FROM_AMing:
			return "22CN";
			case DOMAIN_PRICE_FROM_ARTIFICIAL_BID:
			return "价格备注";
		}
	}
	
	function getDomainType($d) {
		$result = DOMAIN_TYPE_NOT_DEF;
		$domain = explode(".",$d);
		$array_len = count($domain);

		if ($array_len == 2) {
			$pre=$domain[0];
			$sufix = $domain[1];

			if (is_numeric($pre)) {
				$prelen = strlen($pre);

				switch ($prelen) {
				case 5:
					if (strtolower($sufix)=="com")
						$result = DOMAIN_TYPE_5_COM;
					else if (strtolower($sufix)=="cn")
						$result = DOMAIN_TYPE_5_CN;
					else if (strtolower($sufix)=="net")
						$result = DOMAIN_TYPE_5_NET;
					break;
					
				case 4:
					if (strtolower($sufix)=="com")
						$result = DOMAIN_TYPE_4_COM;
					else if (strtolower($sufix)=="cn")
						$result = DOMAIN_TYPE_4_CN;
					else if (strtolower($sufix)=="net")
						$result = DOMAIN_TYPE_4_NET;
					break;
				case 3:
					if (strtolower($sufix)=="com")
						$result = DOMAIN_TYPE_3_COM;
					else if (strtolower($sufix)=="cn")
						$result = DOMAIN_TYPE_3_CN;
					else if (strtolower($sufix)=="net")
						$result = DOMAIN_TYPE_3_NET;
					break;

				case 2:
					if (strtolower($sufix)=="com")
						$result = DOMAIN_TYPE_2_COM;
					else if (strtolower($sufix)=="cn")
						$result = DOMAIN_TYPE_2_CN;
					else if (strtolower($sufix)=="net")
						$result = DOMAIN_TYPE_2_NET;
					break;

				}
			}
			else {
				$result = DOMAIN_TYPE_PIN;
			}
		}
		return $result;
	}
	
	function getMailType($addr) {
		$addr_arry = explode("@",$addr);
		if (strlen($addr_arry[1])>0) {
			$company_arry = explode(".",$addr_arry[1]);
			$company = $company_arry[0];
			return $company;
		}
		return "";
	}

	function getToday() {
		return date("Y-m-d");
	}
	
	function getPreday($mday, $diff) {
		return date('Y-m-d',strtotime($diff.' day',strtotime($mday)));
	}

	
	function getFilterType($domain) {
		$type = DOMAIN_TYPE_GENERAL;
		$domain_arry = explode(".",$domain);
		$pre = $domain_arry[0];
		$domaintype = getDomainType($domain);
		switch ($domaintype) {
			case DOMAIN_TYPE_4_COM:
			case DOMAIN_TYPE_4_NET:
			case DOMAIN_TYPE_4_CN:
				$d0 = substr($pre, 0,1);
				$d1 = substr($pre, 1,1);
				$d2 = substr($pre, 2,1);
				$d3 = substr($pre, 3,1);
				if ($d0==$d1) $type = DOMAIN_TYPE_AABC;
				if ($d0==$d2) $type = DOMAIN_TYPE_ABAC;
				if ($d1==$d2) $type = DOMAIN_TYPE_ABBC;
				if ($d2==$d3) $type = DOMAIN_TYPE_ABCC;
				if ($d0==$d3) $type = DOMAIN_TYPE_ABCA;
				if ($d1==$d3) $type = DOMAIN_TYPE_ABCB;
			break;
			
			case DOMAIN_TYPE_3_NET:
			case DOMAIN_TYPE_3_COM:
			case DOMAIN_TYPE_3_CN:
				$d0 = substr($pre, 0,1);
				$d1 = substr($pre, 1,1);
				$d2 = substr($pre, 2,1);
				if ($d0==$d1) $type = DOMAIN_TYPE_AAB;
				if ($d0==$d2) $type = DOMAIN_TYPE_ABA;
				if ($d1==$d2) $type = DOMAIN_TYPE_ABB;
				
			break;
		}
		return $type;
	}
	
	function getChineseByType($t) {
	
		switch ($t) {
			case DOMAIN_TYPE_2_COM:
			return "2数com";
			
			case DOMAIN_TYPE_3_COM:
			return "3数com";
			
			case DOMAIN_TYPE_4_COM:
			return "4数com";
			
			case DOMAIN_TYPE_5_COM:
			return "5数com";
			
			case DOMAIN_TYPE_2_CN:
			return "2数cn";
			
			case DOMAIN_TYPE_3_CN:
			return "3数cn";
			
			case DOMAIN_TYPE_4_CN:
			return "4数cn";
			
			case DOMAIN_TYPE_5_CN:
			return "5数cn";
			
			case DOMAIN_TYPE_2_NET:
			return "2数net";
			
			case DOMAIN_TYPE_3_NET:
			return "3数net";
			
			case DOMAIN_TYPE_4_NET:
			return "4数net";
			
			case DOMAIN_TYPE_5_NET:
			return "5数net";
			
			case DOMAIN_TYPE_PIN:
			return "拼音";
		}
	}
	
function getStatisDataSelled($d1, $d2, &$arr_com2, &$arr_com3, &$arr_com4, &$arr_cn2, &$arr_cn3, &$arr_cn4, &$arr_net2, &$arr_net3, &$arr_net4) 
{

	$com2 = 0;
	$com3 = 0;
	$com4 = 0;
	$cn2 = 0;
	$cn3 = 0;
	$cn4 = 0;
	
	$net2 = 0;
	$net3 = 0;
	$net4 = 0;
	
	$date_com2 = $d1;
	$date_com3 = $d1;
	$date_com4 = $d1;

	$date_cn2 = $d1;
	$date_cn3 = $d1;
	$date_cn4 = $d1;
	
	$date_net2 = $d1;
	$date_net3 = $d1;
	$date_net4 = $d1;
	
	$link=mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
	mysql_select_db(DB_DATABASE);
	$exec= "SELECT type, update_date, cntselled FROM statisselled WHERE update_date>='";
	$exec= $exec.$d1."' AND update_date<'";
	$exec= $exec.$d2."' group by type, update_date";
	
	//echo $exec;
	$record=0;
	$result=mysql_query($exec);
	while($rs=mysql_fetch_object($result)){
		
		switch ($rs->type) {
			case DOMAIN_TYPE_2_COM:
			fillDataVoid($date_com2, $rs->update_date, $arr_com2, $com2);
			$arr_com2[$com2] = $rs->cntselled;
			
			$com2 = $com2+1;
			$date_com2 = getPreday($date_com2, 1);
			break;
			
			case DOMAIN_TYPE_3_COM:
			fillDataVoid($date_com3, $rs->update_date, $arr_com3, $com3);
			$arr_com3[$com3] = $rs->cntselled;
			
			$com3 = $com3+1;
			$date_com3 = getPreday($date_com3, 1);
			break;
			
			case DOMAIN_TYPE_4_COM:
			fillDataVoid($date_com4, $rs->update_date, $arr_com4, $com4);
			$arr_com4[$com4] = $rs->cntselled;
			
			$date_com4 = getPreday($date_com4, 1);
			$com4 = $com4+1;
			break;
			
			case DOMAIN_TYPE_2_CN:
			fillDataVoid($date_cn2, $rs->update_date, $arr_cn2, $cn2);
			$arr_cn2[$cn2] = $rs->cntselled;
			
			$date_cn2 = getPreday($date_cn2, 1);
			$cn2 = $cn2+1;
			break;
			
			case DOMAIN_TYPE_3_CN:
			fillDataVoid($date_cn3, $rs->update_date, $arr_cn3, $cn3);
			$arr_cn3[$cn3] = $rs->cntselled;
			
			$date_cn3 = getPreday($date_cn3, 1);
			$cn3 = $cn3+1;
			break;
			
			case DOMAIN_TYPE_4_CN:
			fillDataVoid($date_cn4, $rs->update_date, $arr_cn4, $cn4);
			$arr_cn4[$cn4] = $rs->cntselled;
			
			$date_cn4 = getPreday($date_cn4, 1);
			$cn4 = $cn4+1;
			break;
			
			case DOMAIN_TYPE_2_NET:
			fillDataVoid($date_net2, $rs->update_date, $arr_net2, $net2);
			$arr_net2[$net2] = $rs->cntselled;
			
			$date_net2 = getPreday($date_net2, 1);
			$net2 = $net2+1;
			break;
			
			case DOMAIN_TYPE_3_NET:
			fillDataVoid($date_net3, $rs->update_date, $arr_net3, $net3);
			$arr_net3[$net3] = $rs->cntselled;
			
			$date_net3 = getPreday($date_net3, 1);
			$net3 = $net3+1;
			break;
			
			case DOMAIN_TYPE_4_NET:
			fillDataVoid($date_net4, $rs->update_date, $arr_net4, $net4);
			$arr_net4[$net4] = $rs->cntselled;
			
			$date_net4 = getPreday($date_net4, 1);
			$net4 = $net4+1;
			break;
		}
	}
//	echo "xxx:".$date_com3; 
	fillDataVoid($date_com2, $d2, $arr_com2, $com2);
	fillDataVoid($date_com3, $d2, $arr_com3, $com3);
	fillDataVoid($date_com4, $d2, $arr_com4, $com4);
	
	fillDataVoid($date_cn2, $d2, $arr_cn2, $cn2);
	fillDataVoid($date_cn3, $d2, $arr_cn3, $cn3);
	fillDataVoid($date_cn4, $d2, $arr_cn4, $cn4);
	
	fillDataVoid($date_net2, $d2, $arr_net2, $net2);
	fillDataVoid($date_net3, $d2, $arr_net3, $net3);
	fillDataVoid($date_net4, $d2, $arr_net4, $net4);
}

function getStatisDataPrice($d1, $d2, &$arr_com2, &$arr_com3, &$arr_com4, &$arr_cn2, &$arr_cn3, &$arr_cn4, &$arr_net2, &$arr_net3, &$arr_net4) 
{

	$com2 = 0;
	$com3 = 0;
	$com4 = 0;
	$cn2 = 0;
	$cn3 = 0;
	$cn4 = 0;
	
	$net2 = 0;
	$net3 = 0;
	$net4 = 0;
	
	$date_com2 = $d1;
	$date_com3 = $d1;
	$date_com4 = $d1;

	$date_cn2 = $d1;
	$date_cn3 = $d1;
	$date_cn4 = $d1;
	
	$date_net2 = $d1;
	$date_net3 = $d1;
	$date_net4 = $d1;
	
	$link=mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
	mysql_select_db(DB_DATABASE);
	$exec= "SELECT type, pricedate, aveprice FROM statisprice WHERE pricedate>='";
	$exec= $exec.$d1."' AND pricedate<'";
	$exec= $exec.$d2."' group by type, pricedate";
	//echo $exec;
	$record=0;
	$result=mysql_query($exec);
	while($rs=mysql_fetch_object($result)){
		
		switch ($rs->type) {
			case DOMAIN_TYPE_2_COM:
			fillDataVoid($date_com2, $rs->pricedate, $arr_com2, $com2);
			$arr_com2[$com2] = $rs->aveprice;
			
			$com2 = $com2+1;
			$date_com2 = getPreday($date_com2, 1);
			break;
			
			case DOMAIN_TYPE_3_COM:
			fillDataVoid($date_com3, $rs->pricedate, $arr_com3, $com3);
			$arr_com3[$com3] = $rs->aveprice;
			
			$com3 = $com3+1;
			$date_com3 = getPreday($date_com3, 1);
			break;
			
			case DOMAIN_TYPE_4_COM:
			fillDataVoid($date_com4, $rs->pricedate, $arr_com4, $com4);
			$arr_com4[$com4] = $rs->aveprice;
			
			$date_com4 = getPreday($date_com4, 1);
			$com4 = $com4+1;
			break;
			
			case DOMAIN_TYPE_2_CN:
			fillDataVoid($date_cn2, $rs->pricedate, $arr_cn2, $cn2);
			$arr_cn2[$cn2] = $rs->aveprice;
			
			$date_cn2 = getPreday($date_cn2, 1);
			$cn2 = $cn2+1;
			break;
			
			case DOMAIN_TYPE_3_CN:
			fillDataVoid($date_cn3, $rs->pricedate, $arr_cn3, $cn3);
			$arr_cn3[$cn3] = $rs->aveprice;
			
			$date_cn3 = getPreday($date_cn3, 1);
			$cn3 = $cn3+1;
			break;
			
			case DOMAIN_TYPE_4_CN:
			fillDataVoid($date_cn4, $rs->pricedate, $arr_cn4, $cn4);
			$arr_cn4[$cn4] = $rs->aveprice;
			
			$date_cn4 = getPreday($date_cn4, 1);
			$cn4 = $cn4+1;
			break;
			
			case DOMAIN_TYPE_2_NET:
			fillDataVoid($date_net2, $rs->pricedate, $arr_net2, $net2);
			$arr_net2[$net2] = $rs->aveprice;
			
			$date_net2 = getPreday($date_net2, 1);
			$net2 = $net2+1;
			break;
			
			case DOMAIN_TYPE_3_NET:
			fillDataVoid($date_net3, $rs->pricedate, $arr_net3, $net3);
			$arr_net3[$net3] = $rs->aveprice;
			
			$date_net3 = getPreday($date_net3, 1);
			$net3 = $net3+1;
			break;
			
			case DOMAIN_TYPE_4_NET:
			fillDataVoid($date_net4, $rs->pricedate, $arr_net4, $net4);
			$arr_net4[$net4] = $rs->aveprice;
			
			$date_net4 = getPreday($date_net4, 1);
			$net4 = $net4+1;
			break;
		}
	}
//	echo "xxx:".$date_com3; 
	fillDataVoid($date_com2, $d2, $arr_com2, $com2);
	fillDataVoid($date_com3, $d2, $arr_com3, $com3);
	fillDataVoid($date_com4, $d2, $arr_com4, $com4);
	
	fillDataVoid($date_cn2, $d2, $arr_cn2, $cn2);
	fillDataVoid($date_cn3, $d2, $arr_cn3, $cn3);
	fillDataVoid($date_cn4, $d2, $arr_cn4, $cn4);
	
	fillDataVoid($date_net2, $d2, $arr_net2, $net2);
	fillDataVoid($date_net3, $d2, $arr_net3, $net3);
	fillDataVoid($date_net4, $d2, $arr_net4, $net4);
}

function fillDataVoid(&$d1, $d2, &$arr, &$cn) {

	while ($d1 < $d2) {
//		echo "xxx:".$d1." ".$d2." ".$cn."<br>"; 
		$arr[$cn] = 0;
		$d1 = getPreday($d1, 1);
		$cn = $cn+1;
	}
}

function getDateArry($d0, $d2, &$arr) {
	$cn = 0;
	$d1 = $d0;
	while ($d1 < $d2) {
		$arr[$cn] = substr($d1, 5, 10);
		$d1 = getPreday($d1, 1);
		$cn = $cn+1;
	}
}


?>