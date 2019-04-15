package com.house.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.house.constvalue.DataStruct;

/**
 * 成交信息表  日报 DealInfoDate
 * @author zhuweisong
 *
 */
public class DealInfoDateTable extends DBBase {
	private Connection con;
	private String dbms;
	private final static String TABLE_NAME = "DealInfoDate"; 
	public DealInfoDateTable(Connection connArg, String dbNameArg, String dbmsArg) {
		super(connArg, null, TABLE_NAME);
		this.con = connArg;
		this.dbms = dbmsArg;
	}
	
	
	public String getSQLCreateTable(){
		String createString = "create table IF not exists " 
				+ TABLE_NAME
				+ "(id int(32) NOT NULL auto_increment, "
				+ "DealDate date not null, " 
				+ "HouseDistrict char(64) NOT NULL, "
				+ "Usefulness char(64) NOT NULL, " 
				+ "DealQuantity int(32) DEFAULT 0, "
				+ "DealArea int(32) DEFAULT 0, " 
				+ "DealPrice int(32) DEFAULT 0,"
				+ "StaticType int(32) DEFAULT 0,"
				+ "PRIMARY KEY(id))";
		
		return createString;
	}

	
	/**
	 * 写入Item项
	 * @param items
	 * @param type
	 */
	public void insert(List<DataStruct.Item> items, int type) {
		List<String> sqls = new ArrayList<String>();
		for (DataStruct.Item item : items) {
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO ");
			sb.append(TABLE_NAME);
			sb.append(" VALUES (");
			sb.append("'" + item.date.toGMTString()+ "'");
			sb.append(",");
			sb.append("'" + item.HouseDistrict + "'");
			sb.append(",");
			sb.append("'" + item.Usefulness + "'");
			sb.append(",");
			sb.append(item.DealQuantity);
			sb.append(",");
			sb.append(item.DealArea);
			sb.append(",");
			sb.append(item.DealPrice);
			sb.append(",");
			sb.append(type);
			sb.append(");");
			
			String sql = sb.toString();
			sqls.add(sql);
		}
		
		try {
			batchUpdate(sqls);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
