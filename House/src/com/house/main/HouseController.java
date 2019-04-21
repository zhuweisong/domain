package com.house.main;

import java.util.List;

import com.house.DB.DBConst;
import com.house.DB.DBUpdateProxy;
import com.house.constvalue.DataStruct;
import com.house.model.NewHouseInfoFetcher;
import com.house.model.SecondHandHouseInfoFetcher;
import com.house.utils.Utils;


public class HouseController {
	static final String TAG = "HouseController";
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
		SecondHandHouseInfoFetcher fetchor = new SecondHandHouseInfoFetcher();	
		
		List<DataStruct.Item> dplDay = fetchor.getDataByDay(DBConst.second_Deal);
		if (dplDay != null && dplDay.size()>0) {
			mDBProxy.updateSecondHandHouseInfo(dplDay);
		}
		else {
			Utils.Log(TAG, "updateSecondHandInfo error");
		}
		
		List<DataStruct.Item> dplMonth = fetchor.getDataByMonth(DBConst.second_Deal);
		if (dplMonth != null &&  dplMonth.size() > 0) {
			mDBProxy.updateSecondHandHouseInfoByMonth(dplMonth);
		}
		
	}
	
	private void updateNewHouseInfo() {
		NewHouseInfoFetcher fc1 = new  NewHouseInfoFetcher();
		List<DataStruct.Item> newdp = fc1.getDataByDay(DBConst.first_Deal);
		List<DataStruct.Item> newMonth = fc1.getDataByMonth(DBConst.first_Deal);
		mDBProxy.updateNewHouseInfo(newdp);
	}
}
