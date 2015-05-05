<?php
	date_default_timezone_set('PRC');
	require_once('./login/auth.php');
	require_once('./login/config.php');
	require_once('util.php');
	
	$domain = $_GET['domain'];
?>
<html>
    <head>
        <title>行情查询</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="./login/loginmodule.css" rel="stylesheet" type="text/css" />
		<script language="javascript" type="text/javascript" src="./My97DatePicker/WdatePicker.js"></script>
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
		<br>
		<form name="myForm" method="get" action="detail.php">
			<b>详情查询:</b>
			<input type="text" value="<?php echo $domain?>"  name='domain'>如:8888.com
			<input type="submit" name="Submit" value="查询">

			<table  border="1" cellspacing="0" cellpadding="0">
				<caption><font color="red"><?php echo $domain ?></font>价格变化</caption>
					<th>域名</th>
					<th>价格</th>
					<th>定价类型</th>
					<th>来自</th>
					<th>日期</th>
					<th>备注</th>
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
			<caption ><font color="red"><?php echo $domain ?></font>换手变化</caption>
				<th>域名</th>
				<th>所有者</th>
				<th>email</th>
				<th>电话</th>
				<th>国家</th>
				<th>注册商</th>
				<th>日期</th>
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
		<a href="<?php echo "mail-setting.php?domain=".$domain."&price=".$price ?>" target="_blank">发询价邮件</a>
		<form name="newForm" method="get" action="memo-exec.php">
				
				<br/><br/><br/>
			<b>新增<font color="red"><?php echo $domain?></font>价格备注:</b><br>
			<input type="hidden" value="<?php echo $domain."++".$type."++".$memo ?>" name='domain' /><br>
			备注价格:<font color="red">*</font><input type="text" value="<?php if ($priceDate==date("Y-m-d")&&$pricetype>=5) echo $price ?>" src="<?php echo "+".$price?>" name='newprice'>(当天存在报价时，新增备注价格应低于当前价格)<br>
			新增备注:<font color="red">*</font><input type="text" cols="30" row="10" name='newmemo' width='600px'/>(可写上卖家QQ和简单描述)<br>
			<input type="submit" name="addmemo" onclick="return testValid(this.form,this)"  value="确定">
		</form>
    </body>
	
<script language="javascript">
	function testValid(form, button)
	{
		if(button.name=="addmemo") {
			var res1 = testPrice(form);//判断button名称,然后调用对应的验证方法
			if (res1) {
				alert("备注价格成功");
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
			alert("价格请输入数字");
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
					alert("新价格应小于现有的报价");
			}else {
				res = true;
			}
		}
		return (res);
	}
</script>

</html>
