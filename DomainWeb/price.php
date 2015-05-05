<?php
	date_default_timezone_set('PRC');
	require_once('./login/auth.php');
	require_once('./login/config.php');
	require_once('./util.php');
	
	//Start session
	$domain_type = 3;
	if (!empty($_POST['domaintype'])){
		$domain_type = $_POST['domaintype'];
	}
	
	$price_date = getToday();
	if (!empty($_POST['pricedate'])){
		$price_date = $_POST['pricedate'];
	}
	
	$filter = 4095;
	
	if(($domain_type==DOMAIN_TYPE_3_COM || $domain_type==DOMAIN_TYPE_3_CN || $domain_type==DOMAIN_TYPE_3_NET)
		&& !empty($_POST['filter'])){  
		$array = $_POST['filter'];
		$filter = 0;
		$size = count($array);  
		for($i=0; $i< $size; $i++) {
			//echo $array[$i]." ";
			$filter = $filter + intval($array[$i]);
		}
	}

	if(($domain_type==DOMAIN_TYPE_4_COM || $domain_type==DOMAIN_TYPE_4_CN || $domain_type==DOMAIN_TYPE_4_NET) && 
		!empty($_POST['filter4'])){  
		$array = $_POST['filter4'];
		$filter = 0;
		$size = count($array);  
		for($i=0; $i< $size; $i++) {
			//echo $array[$i]." ";
			$filter = $filter + intval($array[$i]);
		}
	}

?>
<html>
    <head>
        <title>�۸���Ϣ</title>
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
		
		<form name="myForm" method="post" action="price.php">
			<select name="domaintype" id="domaintype" onchange="return hidden()" > 
				<option value="3" <?php if($domain_type==3) echo "selected='selected'" ?> >3��com</option> 
				<option value="13" <?php if($domain_type==13) echo "selected='selected'" ?> >3��cn</option> 
				<option value="23" <?php if($domain_type==23) echo "selected='selected'" ?>  >3��net</option> 
				<option value="4" <?php if($domain_type==4) echo "selected='selected'" ?> >4��com</option> 
				<option value="14" <?php if($domain_type==14) echo "selected='selected'" ?>  >4��cn</option> 
				<option value="24" <?php if($domain_type==24) echo "selected='selected'" ?>  >4��net</option> 
			</select>
			<input class="Wdate" type="text" name='pricedate' value="<?php echo $price_date ?>"  onClick="WdatePicker()">��:2014-05-12
			<input type="submit" name="Submit" value="��ѯ"> <br>
			3����:
			<input type="checkbox" name="filter[]" value="2">AAB &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="filter[]" value="8">ABB &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="filter[]" value="4">ABA &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="filter[]" value="1">��ͨ&nbsp;&nbsp;&nbsp;&nbsp;
			<br>
			4����:
			<input type="checkbox" name="filter4[]" value="16">AABC &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="filter4[]" value="32">ABAC &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="filter4[]" value="64">ABBC &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="filter4[]" value="128">ABCC &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="filter4[]" value="256">ABCA &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="filter4[]" value="512">ABCB &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="filter4[]" value="1">��ͨ&nbsp;&nbsp;&nbsp;&nbsp;
			
			<br><br>
				<table  border="1" cellspacing="0" cellpadding="0">
				<caption><?php echo $price_date?>�۸��&nbsp; &nbsp;&nbsp;<a href="pricechange.php?pricedate=<?php echo $price_date ?>&domaintype=<?php echo $domain_type ?>" target="_blank">�۸�仯</a> <br><br></caption>
					<th>����</th>
					<th>�۸�</th>
					<th>�۸�����</th>
					<th>����</th>
					<th>��ע</th>
					<th>�ʼ�</th>
					<?php
						connectDB();
						$exec="select domain,price,pricetype,fromWeb,memo";
						$exec=$exec." FROM domainprice WHERE (pricetype=".DOMAIN_BIN_BUYNOW." OR pricetype=".DOMAIN_HOT_SALE." OR pricetype=".DOMAIN_QQ_BID." OR pricetype=".DOMAIN_BIN_AUCTION_WITH_PRICE.") AND type=".$domain_type;
						$exec = $exec." AND pricedate='".$price_date."' order by price";
						//echo $exec;
						$record=0;
						mysql_query("set names gb2312;");
						$result=mysql_query($exec);
						while($rs=mysql_fetch_object($result)){
							$type = getFilterType($rs->domain);
							if (($filter&$type)==0)
								continue;
							$domain=$rs->domain;
							$price=$rs->price;
							$fromWeb=$rs->fromWeb;
							$pricetype=$rs->pricetype;
							$memo=$rs->memo;
							$record = $record+1;
					?>
				<tr align="center">
				<td><a href="<?php echo "detail.php?domain=".$domain ?>" target="_blank"><?php echo $domain ?></a></td>
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
	<script>
		function hidden(){
		alert("�۸�����������");
		   var oSelect  = document.getElementsByName('filter');//������е�select
		   for(var i=0;i<oSelect.length;i++){
			  if(oSelect[i].style.display==''){
				array.push(oSelect[i]); //�Ѷ����������
				oSelect[i].style.display="none";
			}
		   }
		   return true;
		}
	</script>
</html>