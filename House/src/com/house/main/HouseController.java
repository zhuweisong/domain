package com.house.main;

import java.util.List;
import com.house.constvalue.DataStruct;
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
		
		
	}
	
	
	
}
