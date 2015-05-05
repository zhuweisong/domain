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
        <title>�۸�仯</title>
		<script language="javascript" type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="./login/loginmodule.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
	
		<h1>Welcome <?php echo $_SESSION['SESS_FIRST_NAME'];?></h1>
		<a href="sell.php">�ɽ���ѯ</a> | 
		<a href="auction.php">������ѯ</a> | 
		<a href="price.php">������ѯ</a> |
		<a href="memo-domain.php">��ѯ������</a> | 
		<a href="detail-search.php">�����ѯ</a> | 
		<a href="statis.php">����ͳ��</a> | 
		<a href="./login/logout.php">�˳�</a>
		
		<form name="myForm" method="get" action="pricechange.php">
			<select name="domaintype" id="domaintype" onchange="return hidden()" > 
				<option value="3" <?php if($domain_type==3) echo "selected='selected'" ?> >3��com</option> 
				<option value="13" <?php if($domain_type==13) echo "selected='selected'" ?> >3��cn</option> 
				<option value="4" <?php if($domain_type==4) echo "selected='selected'" ?> >4��com</option> 
				<option value="14" <?php if($domain_type==14) echo "selected='selected'" ?>  >4��cn</option> 
			</select>
			<input class="Wdate" type="text" name='pricedate' value="<?php echo $price_date ?>"  onClick="WdatePicker()">��:2014-05-12
			<input type="submit" name="Submit" value="��ѯ"> <br>

				<br>
				<table  border="1" cellspacing="0" cellpadding="0">
				<caption><?php echo $price_date?>����ͽ���۸�仯���</caption>
					<th>����</th>
					<th>����۸�</th>
					<th>����۸�</th>
					<th>�۸�����</th>
					<th>����</th>
					<th>��ע</th>
					<th>�ʼ�</th>
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
				<td ><a href="<?php echo "mail-setting.php?domain=".$domain."&price=".$price ?>" target="_blank">��ѯ���ʼ�</a></td>
			</tr>
			<?php
				}
				mysql_close();
			?>
			</table>
			�ϼ�<font color='red'><?php echo $record; ?></font>��
		</form>

    </body>

</html>