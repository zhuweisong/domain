package com.house.main;

import java.util.List;
import com.house.constvalue.DataStruct;
import com.house.model.HouseNewInfoFetcher;
import com.house.model.HouseSecondHandInfoFetcher;


public class HouseController {
	
	configManager mConfigManager;
	
	void init() {
		mConfigManager = new ConfigManagerImpl();
	}
	
	void update() {
		updateSecondHandInfo();
	}
	
	
	private void updateSecondHandInfo() {
		HouseSecondHandInfoFetcher fetchor = new HouseSecondHandInfoFetcher();
		
		List<DataStruct.Item> dplDay = fetchor.getDataForDay();
		List<DataStruct.Item> dplMonth = fetchor.getDataForMonth();
		
		HouseNewInfoFetcher fc1 = new  HouseNewInfoFetcher();
		List<DataStruct.Item> dp1 = fc1.getDataForDay();
		List<DataStruct.Item> dp2 = fc1.getDataForDay();
	}
	
	
	
}
