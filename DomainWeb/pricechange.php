<?php
	date_default_timezone_set('PRC');
	require_once('./login/auth.php');
	require_once('./login/config.php');
	require_once('./util.php');
	
	//Start session
	$domain_type = 3;
	if (!empty($_GET['domaintype'])){
		$domain_type = $_GET['domaintype'];
	}
	
	$price_date = getToday();
	if (!empty($_GET['pricedate'])){
		$price_date = $_GET['pricedate'];
	}
	$preday= getPreday($price_date, -1);

?>
<html>
    <head>
        <title>价格变化</title>
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
		
		<form name="myForm" method="get" action="pricechange.php">
			<select name="domaintype" id="domaintype" onchange="return hidden()" > 
				<option value="3" <?php if($domain_type==3) echo "selected='selected'" ?> >3数com</option> 
				<option value="13" <?php if($domain_type==13) echo "selected='selected'" ?> >3数cn</option> 
				<option value="4" <?php if($domain_type==4) echo "selected='selected'" ?> >4数com</option> 
				<option value="14" <?php if($domain_type==14) echo "selected='selected'" ?>  >4数cn</option> 
			</select>
			<input class="Wdate" type="text" name='pricedate' value="<?php echo $price_date ?>"  onClick="WdatePicker()">如:2014-05-12
			<input type="submit" name="Submit" value="查询"> <br>

				<br>
				<table  border="1" cellspacing="0" cellpadding="0">
				<caption><?php echo $price_date?>昨天和今天价格变化情况</caption>
					<th>域名</th>
					<th>昨天价格</th>
					<th>今天价格</th>
					<th>价格类型</th>
					<th>来自</th>
					<th>备注</th>
					<th>邮件</th>
					<?php
						connectDB();
						$exec="SELECT A.domain,A.type,A.price,A.pricetype,A.fromWeb, A.memo, B.price as prePrice FROM ";
						$exec=$exec." (SELECT domain,type,price,pricetype,fromWeb,memo from domainprice where pricedate='";
						$exec=$exec.$price_date;
						$exec=$exec."' AND type=";
						$exec=$exec.$domain_type.") AS A";
						$exec=$exec." LEFT join";
						$exec=$exec." (SELECT domain,price from domainprice where pricedate='";
						$exec=$exec.$preday;
						$exec=$exec."' AND type=".$domain_type.") AS B ON A.domain=B.domain WHERE B.price IS NULL OR B.price != A.price ORDER BY type;";
						//echo $exec;
						$record=0;
						mysql_query("set names gb2312;");
						$result=mysql_query($exec);
						while($rs=mysql_fetch_object($result)){
							$domain=$rs->domain;
							$price=$rs->price;
							$preprice=$rs->prePrice;
							$pricetype=$rs->pricetype;
							$fromWeb=$rs->fromWeb;
							$memo=$rs->memo;
							$record = $record+1;
					?>
				<tr align="center">
				<td><a href="<?php echo "detail.php?domain=".$domain ?>" target="_blank"><?php echo $domain ?></a></td>
				<td><?php echo $preprice ?></td>
				<td><?php echo $price ?></td>
				<td><?php echo getPriceType($pricetype) ?></td>
				<td><?php echo getFromWeb($fromWeb) ?></td>
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