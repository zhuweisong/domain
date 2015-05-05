<?php   
date_default_timezone_set('PRC');
require_once('./login/auth.php');
require_once('./login/config.php');
require_once('util.php');

//Start session
$date_start = getPreday(getToday(), -15);
if (!empty($_POST['datestart'])){
	$date_start = $_POST['datestart'];
}

$date_end = getToday();
if (!empty($_POST['dateend'])){
	$date_end = $_POST['dateend'];
}
	
 /* Library settings */
 define("CLASS_PATH", "./pChart/class");
 define("FONT_PATH", "./pChart/fonts");

 /* pChart library inclusions */
 include(CLASS_PATH."/pData.class.php");
 include(CLASS_PATH."/pDraw.class.php");
 include(CLASS_PATH."/pImage.class.php");


 
$width = 560;
$height = 360;
$imagePriceFile = "tmp/price.png";
$imageSelledFile = "tmp/selled.png";


$arr_com2 = array();
$arr_com3 = array();
$arr_com4 = array();
$arr_cn2 = array();
$arr_cn3 = array();
$arr_cn4 = array();

$arr_net2 = array();
$arr_net3 = array();
$arr_net4 = array();

$arr_date = array();
getDateArry($date_start, $date_end,$arr_date);

getStatisDataPrice($date_start, $date_end, $arr_com2, $arr_com3, $arr_com4, $arr_cn2, $arr_cn3, $arr_cn4, $arr_net2, $arr_net3, $arr_net4);
DrawChart($arr_date, $arr_com2, $arr_com3, $arr_com4, $arr_cn2, $arr_cn3, $arr_cn4, $arr_net2, $arr_net3, $arr_net4, $width,$height,$imagePriceFile, "Price");


getStatisDataSelled($date_start, $date_end, $arr_com2, $arr_com3, $arr_com4, $arr_cn2, $arr_cn3, $arr_cn4, $arr_net2, $arr_net3, $arr_net4);
DrawChart($arr_date, $arr_com2, $arr_com3, $arr_com4, $arr_cn2, $arr_cn3, $arr_cn4, $arr_net2, $arr_net3, $arr_net4, $width,$height,$imageSelledFile,"Turnover");	
	
function DrawChart($arr_date, $arr_com2, $arr_com3, $arr_com4, $arr_cn2, $arr_cn3, $arr_cn4, $arr_net2, $arr_net3, $arr_net4, $width, $height,$imgFile, $title) {
 /* Create and populate the pData object */
 $MyData = new pData();  
 
 $MyData->addPoints($arr_com4,"4com");
 
// $MyData->addPoints($arr_cn2,"2cn");
 $MyData->addPoints($arr_cn3,"3cn");
 $MyData->addPoints($arr_cn4,"4cn");
 
 //$MyData->addPoints($arr_net2,"2net");
 $MyData->addPoints($arr_net3,"3net");
 $MyData->addPoints($arr_net4,"4net");
 
  if (strcmp($title,"Turnover")==0) {
//	$MyData->addPoints($arr_cn2,"2cn");
//	$MyData->addPoints($arr_com2,"2com");
	$MyData->addPoints($arr_com3,"3com");
	}
	
 $MyData->setSerieWeight("2com",1);
 $MyData->setSerieWeight("3com",1);
 $MyData->setSerieWeight("4com",1);
 $MyData->setSerieWeight("2cn",1);
 $MyData->setSerieWeight("3cn",1);
 $MyData->setSerieWeight("4cn",1);
 $MyData->setSerieWeight("2net",1);
 $MyData->setSerieWeight("3net",1);
 $MyData->setSerieWeight("4net",1);
 
 $MyData->setAxisName(0,"price");
 $MyData->addPoints($arr_date,"Labels");
 $MyData->setSerieDescription("Labels","Months");
 $MyData->setAbscissa("Labels");


 /* Create the pChart object */
 $myPicture = new pImage($width,$height,$MyData);

 /* Turn of Antialiasing */
 $myPicture->Antialias = FALSE;

 /* Add a border to the picture */
 $myPicture->drawRectangle(10,10,$width-10,$height-10,array("R"=>0,"G"=>0,"B"=>0));
 
 /* Write the chart title */ 
 $myPicture->setFontProperties(array("FontName"=>FONT_PATH."/Forgotte.ttf","FontSize"=>11));
 $myPicture->drawText(100,35,$title,array("FontSize"=>20,"Align"=>TEXT_ALIGN_BOTTOMMIDDLE));

 /* Set the default font */
 $myPicture->setFontProperties(array("FontName"=>FONT_PATH."/pf_arma_five.ttf","FontSize"=>6));

 /* Define the chart area */
 $myPicture->setGraphArea(50,20,$width-30,$height-40);

 /* Draw the scale */
 $scaleSettings = array("XMargin"=>10,"YMargin"=>10,"Floating"=>TRUE,"GridR"=>200,"GridG"=>200,"GridB"=>200,"DrawSubTicks"=>TRUE,"CycleBackground"=>TRUE);
 $myPicture->drawScale($scaleSettings);

 /* Turn on Antialiasing */
 $myPicture->Antialias = TRUE;

 /* Draw the line chart */
 $Settings = array("RecordImageMap"=>TRUE);
 $myPicture->drawLineChart($Settings);
 $myPicture->drawPlotChart(array("PlotBorder"=>TRUE,"BorderSize"=>1,"Surrounding"=>-60,"BorderAlpha"=>80));

 /* Write the chart legend */
 $myPicture->drawLegend($width-220,20,array("Style"=>LEGEND_NOBORDER,"Mode"=>LEGEND_HORIZONTAL));

 $myPicture->Render($imgFile);
}

?>


<html>
    <head>
        <title>拍卖信息</title>
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
		<br><br>
		<form name="myForm" method="post" action="statis.php">
		
			<b>开始时间</b><input class="Wdate" type="text" name='datestart'  value="<?php echo $date_start?>"   onClick="WdatePicker()">
			<b>结束时间</b><input class="Wdate" type="text" name='dateend' value="<?php echo $date_end?>"  onClick="WdatePicker()">
			<input type="submit" name="Submit" value="查询"> <br/>
			<img src="<?php echo $imageSelledFile?>"><img src="<?php echo $imagePriceFile?>"><br/>
			<table> <tr> <td valign="top" >
				<table  border="1" cellspacing="0" cellpadding="0">
				<caption>成交趋势</caption>
					<th>类型</th>
					<th>日期</th>
					<th>成交数</th>
					<th>来自</th>
					
					<?php
						$link=mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
						mysql_select_db(DB_DATABASE);
						$exec= "SELECT type, update_date, cntselled FROM statisselled WHERE update_date>='2014-05-20' group by type, update_date";
						//echo $exec;
						$record=0;
						$result=mysql_query($exec);
						while($rs=mysql_fetch_object($result)){
							$type=$rs->type;
							$update_date=$rs->update_date;
							$cnt=$rs->cntselled;
					?>
				<tr align="center">
				<td><?php echo getChineseByType($type)?></td>
				<td><?php echo $update_date?></td>
				<td><?php echo $cnt ?></td>
				<td><?php echo "详情"?></td>
				</tr>
			<?php
				}
				mysql_close();
			?>
			</table>
</td>
<td valign="top" >&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td valign="top" >
				<table  border="1" cellspacing="0" cellpadding="0">
				<caption>价格趋势</caption>
					<th>类型</th>
					<th>日期</th>
					<th>均价</th>
					<th>来自</th>
					
					<?php
						$link=mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
						mysql_select_db(DB_DATABASE);
						$exec= "SELECT type, pricedate, aveprice FROM statisprice WHERE pricedate>='2014-05-20' group by type, pricedate";
						//echo $exec;
						$record=0;
						$result=mysql_query($exec);
						while($rs=mysql_fetch_object($result)){
							$type=$rs->type;
							$update_date=$rs->pricedate;
							$aveprice=$rs->aveprice;
					?>
				<tr align="center">
				<td><?php echo getChineseByType($type)?></td>
				<td><?php echo $update_date?></td>
				<td><?php echo $aveprice ?></td>
				<td><?php echo "详情"?></td>
				</tr>
			<?php
				}
				mysql_close();
			?>
			</table>
			</td>
			</tr> </table> 
		</form>

    </body>
</html>