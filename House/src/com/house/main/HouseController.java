package com.house.main;

import java.util.List;

import com.house.DB.DBUpdateProxy;
import com.house.DB.DealInfoDateTable;
import com.house.constvalue.DataStruct;
import com.house.model.NewHouseInfoFetcher;
import com.house.model.SecondHandHouseInfoFetcher;


public class HouseController {
	
	configManager mConfigManager;
	DBUpdateProxy mDBProxy;
	
	void init() {
		
		mConfigManager = new ConfigManagerImpl();
		mDBProxy = new DBUpdateProxy();
		mDBProxy.init();
	}
	
	void update() {
		updateSecondHandInfo();
		
		//updateNewHouseInfo();
	
	}
	
	private void updateSecondHandInfo() {
		SecondHandHouseInfoFetcher shfetchor = new SecondHandHouseInfoFetcher();	
		List<DataStruct.Item> dplDay = shfetchor.getDataForDay();
		List<DataStruct.Item> dplMonth = shfetchor.getDataForMonth();
		mDBProxy.updateSecondHandHouseInfo(dplDay);
	}
	
	private void updateNewHouseInfo() {
		NewHouseInfoFetcher fc1 = new  NewHouseInfoFetcher();
		List<DataStruct.Item> newdp = fc1.getDataForDay();
		List<DataStruct.Item> newMonth = fc1.getDataForMonth();
		mDBProxy.updateNewHouseInfo(newdp);
	}
}
