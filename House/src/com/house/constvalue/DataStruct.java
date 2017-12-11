package com.house.constvalue;

/**
 * 
 * @author sevenzhu
 *
 */
public class DataStruct {
	
	public static final int HOUSE_USEFULNESS_COMMERCAIL = 1; //商业
	public static final int HOUSE_USEFULNESS_HOME = 2;  //住宅
	public static final int HOUSE_USEFULNESS_ELSE = 3; //其他
	public static final int HOUSE_USEFULNESS_OFFICE = 4; //办公
	
	/**
	 * 基础数据结构
	 */
	public static class Item {
		@Override
		public String toString() {
			return ((date != null) ? date.toString() : "") + "|" 
						+ HouseDistrict + "|" + Usefulness + "|" + DealQuantity
						+ "|" + DealArea   + "|" + DealPrice ;
		}
		public Item() {}
		public Item(java.sql.Date dt, String dis) {
		}
		public java.sql.Date date; //日期
		public String HouseDistrict;  //所在区
		public int DealQuantity;	//成交套数
		public int DealArea;		//成交面积
		public int DealPrice;		//成交均价
		public String Usefulness; 		//房产用途 公寓, 住宅，商业
	}
	
	/**
	 * 按户型统计成交信息 日报
	 */
	public static class DealByHouseType extends Item { 
		public int houseType; 		//户型
		public int AvailableSale; //可售套数
		public int AvailableArea; //可售面积
	}
	
	/**
	 * 按面积信息表
	 */
	public static class DealByHouseArea extends Item {
		public int AreaSection; //面积区间
	}
	
	/**
	 * 成交信息表
	 */
	public static class DealInfo extends Item {

		public int AvailableSale; //可售套数
		public int AvailableArea; //可售面积
	}
	
	/**
	 * 存量房成交信息表   
	 */
	public static class DealInfoSecondHand extends Item {
		
	}
	

}
