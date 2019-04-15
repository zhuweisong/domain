package com.house.main;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigManagerImpl extends configManager {
	final static String fileName = "config.xml";

	
	Properties prop;
	
	void init() {
		prop = new Properties();
		
	    try {
			FileInputStream fis = new FileInputStream(fileName);
			prop.loadFromXML(fis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	String getStringValue(String key) {
	    String value = prop.getProperty(key);
		return value;
	}

	@Override
	int getIntValue(String key) {
	    String value = prop.getProperty(key);
	    int real = -1;
	    try {
	    	real = Integer.valueOf(value);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		return real;
	}
	

}
