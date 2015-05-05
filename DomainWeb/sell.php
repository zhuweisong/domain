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
		
		
		<form name="myForm" method="post" action="sell.php">
			<b>��ʼʱ��</b><input class="Wdate" type="text" name='datestart'  value="<?php echo $date_start?>"   onClick="WdatePicker()">
			<b>����ʱ��</b><input class="Wdate" type="text" name='dateend' value="<?php echo $date_end?>"  onClick="WdatePicker()">
			<input type="submit" name="Submit" value="��ѯ"> 
				<table  border="1" cellspacing="0" cellpadding="0" >
                    <caption><?php echo $date_start."��".$date_end; ?></font>�ɽ�</caption>
                        <th>����</th>
                        <th>������</th>
                        <th>email</th>
						<th>����</th>
						<th>�۸�</th>
						<th>�۸�����</th>
						<th>����</th>
						<th>��������</th>
						
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
				 �ϼ�<font color='red'><?php echo $record; ?></font>��
		</form>

    </body>
</html>