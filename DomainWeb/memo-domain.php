<?php
	date_default_timezone_set('PRC');
	require_once('./login/auth.php');
	require_once('./login/config.php');
	require_once('./util.php');
	
	//Start session
	$date_start = getPreday(getToday(),-1);
	if (!empty($_GET['datestart'])){
		$date_start = $_GET['datestart'];
	}
	
	$date_end = getToday();
	if (!empty($_GET['dateend'])){
		$date_end = $_GET['dateend'];
	}
?>
<html>
    <head>
        <title>已询价域名</title>
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
		
		<form name="myForm" method="get" action="memo-domain.php">
			<b>开始时间</b><input class="Wdate" type="text" name='datestart'  value="<?php echo $date_start?>"   onClick="WdatePicker()">
			<b>结束时间</b><input class="Wdate" type="text" name='dateend' value="<?php echo $date_end?>"  onClick="WdatePicker()">
			<input type="submit" name="Submit" value="查询"> 
				<table  border="1" cellspacing="0" cellpadding="0">
				<caption>已询价域名</caption>
					<th>域名</th>
					<th>价格</th>
					<th>价格类型</th>
					<th>日期</th>
					<th>备注</th>
					<th>邮件询价</th>
					<?php
						connectDB();
						$exec="select domain,price,pricetype,fromWeb,memo,pricedate";
						$exec=$exec." FROM domainprice WHERE pricetype=".DOMAIN_QQ_BID." AND pricedate>='".$date_start."' AND pricedate<='".$date_end."'";
						$exec = $exec." order by type, domain, pricedate";
						//echo $exec;
						$record=0;
						mysql_query("set names gb2312;");
						$result=mysql_query($exec);
						while($rs=mysql_fetch_object($result)){
							$domain=$rs->domain;
							$price=$rs->price;
							$pricedate=$rs->pricedate;
							$pricetype=$rs->pricetype;
							$memo=$rs->memo;
							$record = $record+1;
					?>
				<tr align="center">
				<td><a href="<?php echo "detail.php?domain=".$domain ?>" target="_blank"><?php echo $domain ?></a></td>
				<td><?php echo $price ?></td>
				<td><?php echo getPriceType($pricetype) ?></td>
				<td><?php echo $pricedate ?></td>
				<td style="text-align:left;"><?php echo $memo?></td>
				<td ><a href="<?php echo "mail-setting.php?domain=".$domain."&price=".$price ?>" target="_blank">发询价邮件</a></td>
			</tr>
			<?php
				}
				mysql_close();
			?>
			</table>
			合计<font color='red'><?php echo $record; ?></font>条
		</form>

    </body>
</html>