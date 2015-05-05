<?php
	date_default_timezone_set('PRC');
	require_once('./login/auth.php');
	require_once('./login/config.php');
	require_once('./util.php');
	
	//Start session
	$date_start = getToday();
	if (!empty($_POST['datestart'])){
		$date_start = $_POST['datestart'];
	}
	
	$date_end = getToday();
	if (!empty($_POST['dateend'])){
		$date_end = $_POST['dateend'];
	}
		
	$pre_date = getPreday($date_start,-1);
?>
<html>
    <head>
        <title>换手信息</title>
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
		
		
		<form name="myForm" method="post" action="sell.php">
			<b>开始时间</b><input class="Wdate" type="text" name='datestart'  value="<?php echo $date_start?>"   onClick="WdatePicker()">
			<b>结束时间</b><input class="Wdate" type="text" name='dateend' value="<?php echo $date_end?>"  onClick="WdatePicker()">
			<input type="submit" name="Submit" value="查询"> 
				<table  border="1" cellspacing="0" cellpadding="0" >
                    <caption><?php echo $date_start."到".$date_end; ?></font>成交</caption>
                        <th>域名</th>
                        <th>所有者</th>
                        <th>email</th>
						<th>国家</th>
						<th>价格</th>
						<th>价格类型</th>
						<th>来自</th>
						<th>出价日期</th>
						
                        <?php
                            connectDB();

                            $exec="SELECT A.domain, A.reg_email,A.reg_name, A.reg_phone, A.reg_country,A.type,C.price,C.pricedate,C.fromWeb,C.pricetype";
							$exec=$exec." FROM";
							$exec=$exec." (SELECT domain, reg_email, reg_name, reg_phone, reg_country, type,update_date FROM domainwhois WHERE update_date<='".$date_end."' and update_date>='".$date_start."') as A";
							$exec=$exec." JOIN";
							$exec=$exec." (SELECT domain FROM domainwhois group by domain having count(update_date)>1 ) as B";
							$exec=$exec." ON A.domain=B.domain";
							$exec=$exec." LEFT JOIN";
							$exec=$exec." (SELECT domain, price, pricedate,pricetype,fromWeb FROM domainprice WHERE pricedate>='".$pre_date."') as C";
							$exec=$exec." ON A.domain = C.domain AND C.pricedate=replace(date_sub(A.update_date,interval 1 day),'-','') order by A.type,C.price";
							//echo $exec;
							mysql_query("set names gb2312;");
                            $result=mysql_query($exec);
							$record = 0;
                            while($rs=mysql_fetch_object($result)){
                                $domain=$rs->domain;
                                $reg_email=strlen($rs->reg_email)>12?substr($rs->reg_email,0,20):$rs->reg_email;
                                $reg_name=strlen($rs->reg_name)>12?substr($rs->reg_name,0,16):$rs->reg_name;
								$reg_phone=strlen($rs->reg_phone)>12?substr($rs->reg_phone,0,16):$rs->reg_phone;
								$reg_country=$rs->reg_country;
								$price=$rs->price;
								$pricetype=$rs->pricetype;
								$pricedate=$rs->pricedate;
								$fromWeb=$rs->fromWeb;
								$record++;
                        ?>
                    <tr align="center">
                    <td align="left"><a href="<?php echo "detail.php?domain=".$domain ?>" target="_blank"><?php echo $domain?></a></td>
                    <td style="text-align:right;"><?php echo $reg_name?></td>
                    <td style="text-align:right;"><?php echo $reg_email?></td>
					<td ><?php echo $reg_country?></td>
					<td style="text-align:right;"><?php echo $price?></td>
					<td ><?php echo getPriceType($pricetype) ?></td>
                    <td style="text-align:right;"><?php echo getFromWeb($fromWeb) ?></td>
                    <td style="text-align:right;"><?php echo $pricedate?></td>
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