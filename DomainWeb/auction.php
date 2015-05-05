<?php
	//Start session
	date_default_timezone_set('PRC');
	require_once('./login/auth.php');
	require_once('./login/config.php');
	require_once('./util.php');
	
	$price_date = getToday();
	if (!empty($_POST['pricedate'])){
		$price_date = $_POST['pricedate'];
	}
?>
<html>
    <head>
        <title>������Ϣ</title>
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
		
		<form name="myForm" method="post" action="auction.php">
		<b>������ѯ:</b>
			<input class="Wdate" type="text" name='pricedate' value="<?php echo $price_date ?>" onClick="WdatePicker()">��:2014-05-12
			<input type="submit" name="Submit" value="��ѯ"> 
				<table  border="1" cellspacing="0" cellpadding="0">
				<caption><?php echo $price_date?>�����ױ�</caption>
					<th>����</th>
					<th>�ɼ�ʱ�۸�</th>
					<th>��������</th>
					<th>����ʱ��</th>
					<th>����</th>
					<th>���ճɽ��۸�</th>
					<th>����ҳ��</th>
					<?php
						$link=mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
						mysql_select_db(DB_DATABASE);
						$exec= "SELECT A.domain, A.price, A.pricetype, A.AcutionEnd, A.URL, A.fromWeb, B.endprice FROM ";
						$exec=$exec."(select domain,price,AcutionEnd,URL,type,pricetype,fromWeb";
						$exec=$exec." FROM domainauctionurl WHERE (pricetype=".DOMAIN_BIN_AUCTION_WITH_PRICE." OR pricetype=".DOMAIN_BIN_AUCTION_ONE.")";
						$exec=$exec." AND pricedate='".$price_date."' ) as A";
						$exec=$exec." LEFT JOIN ";
						$exec=$exec."(select domain, price as endprice FROM domainprice WHERE (pricetype=".DOMAIN_BIN_AUCTION_WITH_PRICE." OR pricetype=".DOMAIN_BIN_AUCTION_ONE.")";
						$exec=$exec." AND pricedate='".$price_date."' ) as B";
						$exec=$exec." ON A.domain=B.domain ";
						if ($price_date == date("Y-m-d")) 
							$exec=$exec." ORDER BY A.pricetype desc, A.type, A.AcutionEnd";
						else
							$exec=$exec." ORDER BY A.pricetype desc, A.type, B.endprice";
						
						//echo $exec;
						$record=0;
						$result=mysql_query($exec);
						while($rs=mysql_fetch_object($result)){
							$domain=$rs->domain;
							$price=$rs->price;
							$pricetype=$rs->pricetype;
							$fromWeb=$rs->fromWeb;
							$AcutionEnd=$rs->AcutionEnd;
							$URL=$rs->URL;
							$endprice=$rs->endprice;
							$record = $record+1;
					?>
				<tr align="center">
				<td><a href="<?php echo "detail.php?domain=".$domain ?>" target="_blank"><?php echo $domain?></a></td>
				<td><?php echo $price?></td>
				<td><?php echo getPriceType($pricetype)?></td>
				<td><?php echo $AcutionEnd?></td>
				<td><?php echo getFromWeb($fromWeb) ?></td>
				<td><?php echo ($endprice==-1 || $endprice==99999999 )? "����": $endprice?></td>
				<td align="left"><a href="<?php echo $URL ?>" target="_blank">����ҳ</a></td>
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