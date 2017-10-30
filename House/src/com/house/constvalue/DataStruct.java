package com.house.constvalue;

/**
 * 
 * @author sevenzhu
 *
 */
public class DataStruct {
	/**
	 * 基础数据结构
	 */
	public static class DataBase {
		public java.sql.Date priceDate; //日期
		public String HouseDistrict;  //所在区
		public int DealQuantity;	//成交套数
		public int DealArea;		//成交面积
		public int DealPrice;		//成交均价
	}
	
	/**
	 * 按户型统计成交信息 日报
	 */
	public static class DealByHouseType extends DataBase { 
		public int houseType; 		//户型
		public int AvailableSale; //可售套数
		public int AvailableArea; //可售面积
	}
	
	/**
	 * 按面积信息表
	 */
	public static class DealByHouseArea extends DataBase {
		public int AreaSection; //面积区间
	}
	
	/**
	 * 成交信息表
	 */
	public static class DealInfo extends DataBase {
		public int usefulness; //房产用途 公寓, 住宅，商业
		public int AvailableSale; //可售套数
		public int AvailableArea; //可售面积
	}
	
	/**
	 * 存量房成交信息表   
	 */
	public static class DealInfoSecondHand extends DataBase {
		
	}
	

}
