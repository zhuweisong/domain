<?php
	date_default_timezone_set('PRC');
	require_once('./login/auth.php');
	require_once('./login/config.php');
	require_once('util.php');
	
	$domain = $_GET['domain'];
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
		<form name="myForm" method="get" action="detail.php">
			<b>�����ѯ:</b>
			<input type="text" value="<?php echo $domain?>"  name='domain'>��:8888.com
			<input type="submit" name="Submit" value="��ѯ">

			<table  border="1" cellspacing="0" cellpadding="0">
				<caption><font color="red"><?php echo $domain ?></font>�۸�仯</caption>
					<th>����</th>
					<th>�۸�</th>
					<th>��������</th>
					<th>����</th>
					<th>����</th>
					<th>��ע</th>
					<?php
						connectDB();
						$exec="SELECT domain,price,priceDate,memo,type,pricetype,fromWeb";
						$exec=$exec." FROM domainprice WHERE domain='".$domain."' order by priceDate";
						
						//echo $exec;
						mysql_query("set names gb2312;");
						$result=mysql_query($exec);
						while($rs=mysql_fetch_object($result)){
							$domain=$rs->domain;
							$price=$rs->price;
							$pricetype=$rs->pricetype;
							$fromWeb=$rs->fromWeb;
							$priceDate=$rs->priceDate;
							$type=$rs->type;
							$memo=$rs->memo;
					?>
				<tr align="center">
				<td><?php echo $domain ?></td>
				<td><?php echo $price ?></td>
				<td><?php echo getPriceType($pricetype) ?></td>
				<td><?php echo getFromWeb($fromWeb)?></td>
				<td><?php echo $priceDate?></td>
				<td style="text-align:left;"><?php echo $memo?></td>
			</tr>
			<?php
				}
				mysql_close();
			?>
		</table>
		<br>
		<table  border="1" cellspacing="0" cellpadding="0">
			<caption ><font color="red"><?php echo $domain ?></font>���ֱ仯</caption>
				<th>����</th>
				<th>������</th>
				<th>email</th>
				<th>�绰</th>
				<th>����</th>
				<th>ע����</th>
				<th>����</th>
					<?php
						connectDB();
						$exec = "SELECT A.domain, A.reg_email,A.reg_name,A.reg_phone,A.reg_country,A.update_date,A.reg_url, B.admin FROM ";
						$exec=$exec."(SELECT domain, reg_email,reg_name,reg_phone,reg_country,update_date,reg_url FROM domainwhois WHERE domain='".$domain."') AS A";
						$exec=$exec." LEFT JOIN (SELECT admin, mail FROM mailmap) AS B";
						$exec=$exec." ON A.reg_email=B.mail order by update_date ";
						

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
							$reg_url=$rs->reg_url;
							$admin=$rs->admin;
							
					?>
				<tr align="center">
				<td><?php echo $domain?></td>
				<td style="text-align:right;"><?php echo $reg_name?></td>
				<td style="text-align:right;"><a href="<?php echo "detail-mail.php?email=".$reg_email."&admin=".$admin ?>" target="_blank"><?php echo $reg_email."(".$admin.")" ?></a></td>
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
		<a href="<?php echo "mail-setting.php?domain=".$domain."&price=".$price ?>" target="_blank">��ѯ���ʼ�</a>
		<form name="newForm" method="get" action="memo-exec.php">
				
				<br/><br/><br/>
			<b>����<font color="red"><?php echo $domain?></font>�۸�ע:</b><br>
			<input type="hidden" value="<?php echo $domain."++".$type."++".$memo ?>" name='domain' /><br>
			��ע�۸�:<font color="red">*</font><input type="text" value="<?php if ($priceDate==date("Y-m-d")&&$pricetype>=5) echo $price ?>" src="<?php echo "+".$price?>" name='newprice'>(������ڱ���ʱ��������ע�۸�Ӧ���ڵ�ǰ�۸�)<br>
			������ע:<font color="red">*</font><input type="text" cols="30" row="10" name='newmemo' width='600px'/>(��д������QQ�ͼ�����)<br>
			<input type="submit" name="addmemo" onclick="return testValid(this.form,this)"  value="ȷ��">
		</form>
    </body>
	
<script language="javascript">
	function testValid(form, button)
	{
		if(button.name=="addmemo") {
			var res1 = testPrice(form);//�ж�button����,Ȼ����ö�Ӧ����֤����
			if (res1) {
				alert("��ע�۸�ɹ�");
			}
			return res1;
		}
	}

	
	function testPrice(form)
	{
		var ctrl = form.newprice;
		var newvalue = ctrl.value;
		var res = false;
		
		if(isNaN(newvalue)) {
			alert("�۸�����������");
		}
		else {
			var i = ctrl.src.indexOf('+',0);
			var oldPrice = ctrl.src.substring(i+1);

			if (oldPrice.length>2) {
				var o = parseInt(oldPrice);
				var n = parseInt(newvalue);
		
				if(n<o && n>0 && n<10000000)
					res = true;
				else
					alert("�¼۸�ӦС�����еı���");
			}else {
				res = true;
			}
		}
		return (res);
	}
</script>

</html>
