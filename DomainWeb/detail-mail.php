<?php
	date_default_timezone_set('PRC');
	require_once('./login/auth.php');
	require_once('./login/config.php');
	require_once('util.php');
	
	$email = $_GET['email'];
	$admin = $_GET['admin'];
?>
<html>
    <head>
        <title>�����ѯ</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="./login/loginmodule.css" rel="stylesheet" type="text/css" />
		<script language="javascript" type="text/javascript" src="./My97DatePicker/WdatePicker.js"></script>
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
		<br>
		<form name="myForm" method="get" action="detail-mail.php">
			<b>�����ѯ:</b>
			<input type="text" value="<?php echo $email?>"  name='email'>��:8888@qq.com
			<input type="submit" name="Submit" value="��ѯ">

		<table  border="1" cellspacing="0" cellpadding="0">
			<caption ><font color="red"><?php echo $email."(".$admin.")" ?></font><a href="<?php echo "mail-map.php?email=".$email."&admin=".$admin ?>" target="_blank">��ע��������������</a></caption>
				<th>����</th>
				<th>������</th>
				<th>email</th>
				<th>�绰</th>
				<th>����</th>
				<th>ע����</th>
				<th>����</th>
					<?php
						connectDB();
						$exec="SELECT A.domain,A.reg_email,A.reg_name,A.reg_phone,A.reg_country,A.update_date,A.reg_url FROM";
						$exec=$exec." (SELECT domain,reg_email,reg_name,reg_phone,reg_country,update_date,reg_url FROM domainwhois WHERE reg_email='".$email."') AS A";
						$exec=$exec." JOIN (SELECT domain, max(update_date) as update_date FROM domainwhois group by domain) AS B";
						$exec=$exec." ON A.domain=B.domain AND A.update_date=B.update_date";
						
						//echo $exec;
						mysql_query("set names gb2312;");
						$result=mysql_query($exec);
						while($rs=mysql_fetch_object($result)){
							$domain=$rs->domain;
							$reg_email=$rs->reg_email;
							$reg_name=$rs->reg_name;
							$reg_phone=$rs->reg_phone;
							$reg_country=$rs->reg_country;
							$update_date=$rs->update_date;
							$reg_url=$rs->reg_url
							
					?>
				<tr align="center">
				<td><?php echo $domain?></td>
				<td style="text-align:right;"><?php echo $reg_name?></td>
				<td style="text-align:right;"><?php echo $reg_email?></td>
				<td style="text-align:right;"><?php echo $reg_phone?></td>
				<td ><?php echo $reg_country?></td>
				<td ><?php echo $reg_url?></td>
				<td style="text-align:right;"><?php echo $update_date?></td>
			</tr>
			<?php
				}
				mysql_close();
			?>
		</table>
		</form>
    </body>

</html>
