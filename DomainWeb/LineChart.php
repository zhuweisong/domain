<?php   
date_default_timezone_set('PRC');
require_once('./login/auth.php');
require_once('./login/config.php');
require_once('util.php');
	
 /* Library settings */
 define("CLASS_PATH", "./pChart/class");
 define("FONT_PATH", "./pChart/fonts");

 /* pChart library inclusions */
 include(CLASS_PATH."/pData.class.php");
 include(CLASS_PATH."/pDraw.class.php");
 include(CLASS_PATH."/pImage.class.php");

 /* Create and populate the pData object */
 $MyData = new pData();  
 
 	$date_start = '2014-05-08';
	$date_end = '2014-06-07';
	
$width = 960;
$height = 460;
$arr_com2 = array();
$arr_com3 = array();
$arr_com4 = array();
$arr_cn2 = array();
$arr_cn3 = array();
$arr_cn4 = array();

$arr_net2 = array();
$arr_net3 = array();
$arr_net4 = array();

getStatisData($date_start, $date_end, $arr_com2, $arr_com3, $arr_com4, $arr_cn2, $arr_cn3, $arr_cn4, $arr_net2, $arr_net3, $arr_net4);


$arr_date = array();
getDateArry($date_start, $date_end,$arr_date);


							
// $MyData->addPoints($arr_com2,"2com");
// $MyData->addPoints($arr_com3,"3com");
 $MyData->addPoints($arr_com4,"4com");
 
// $MyData->addPoints($arr_cn2,"2cn");
 $MyData->addPoints($arr_cn3,"3cn");
 $MyData->addPoints($arr_cn4,"4cn");
 
 //$MyData->addPoints($arr_net2,"2net");
 $MyData->addPoints($arr_net3,"3net");
 $MyData->addPoints($arr_net4,"4net");
	
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

 /* Draw the background */
// $Settings = array("R"=>170, "G"=>183, "B"=>87, "Dash"=>1, "DashR"=>190, "DashG"=>203, "DashB"=>107);
// $myPicture->drawFilledRectangle(0,0,$width,$height,$Settings);

 /* Overlay with a gradient */
// $Settings = array("StartR"=>219, "StartG"=>231, "StartB"=>139, "EndR"=>1, "EndG"=>138, "EndB"=>68, "Alpha"=>50);
// $myPicture->drawGradientArea(0,0,$width,$height,DIRECTION_VERTICAL,$Settings);

 /* Add a border to the picture */
 $myPicture->drawRectangle(10,10,$width-10,$height-10,array("R"=>0,"G"=>0,"B"=>0));
 
 /* Write the chart title */ 
 $myPicture->setFontProperties(array("FontName"=>FONT_PATH."/MSYHBD.TTF","FontSize"=>11));
 $myPicture->drawText(100,35,"Price",array("FontSize"=>20,"Align"=>TEXT_ALIGN_BOTTOMMIDDLE));

 /* Set the default font */
 $myPicture->setFontProperties(array("FontName"=>FONT_PATH."/pf_arma_five.ttf","FontSize"=>6));

 /* Define the chart area */
 $myPicture->setGraphArea(60,40,$width-60,$height-40);

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
 $myPicture->drawLegend(540,20,array("Style"=>LEGEND_NOBORDER,"Mode"=>LEGEND_HORIZONTAL));

 $imageFile = "tmp/price.png";
 $myPicture->Render($imageFile);
 echo '<img src="'.$imageFile.'">';

?>