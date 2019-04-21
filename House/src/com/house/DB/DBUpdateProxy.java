package com.house.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.house.constvalue.DataStruct;
import com.house.constvalue.DataStruct.Item;
import com.house.utils.Utils;

/**
 * 用于更新数据库代理
 * @author sevenzhu
 */
public class DBUpdateProxy {
	private static String TAG = "DBUpdateProxy";
	private JDBCTutorialUtilities myJDBCTutorialUtilities;

	public DBUpdateProxy() {
		super();
	}

	public void init() {
		myJDBCTutorialUtilities = JDBCTutorialUtilities.init();
		
	}

	public void updateSecondHandHouseInfo(List<DataStruct.Item > dpl) {
		synchronized (this) {
			Connection myConnection = null;

			try {
				System.out.println("-----in--1-updateprice---");
				myConnection = myJDBCTutorialUtilities.getConnection();

				DealInfoDateTableByDay myTable = new DealInfoDateTableByDay(myConnection,
						myJDBCTutorialUtilities.dbName,
						myJDBCTutorialUtilities.dbms);

				myTable.insert(dpl, 1);

			} catch (SQLException e) {
				JDBCTutorialUtilities.printSQLException(e);
			} finally {
				JDBCTutorialUtilities.closeConnection(myConnection);
			}
			System.out.println("-----out--1-updateprice---");
		}
	}
	
	public void updateSecondHandHouseInfoByMonth(List<DataStruct.Item > dpl) {
		synchronized (this) {
			Connection myConnection = null;

			try {
				System.out.println("-----in--1-updateprice---");
				myConnection = myJDBCTutorialUtilities.getConnection();

				DealInfoDateTableByMonth myTable = new DealInfoDateTableByMonth(myConnection,
						myJDBCTutorialUtilities.dbName,
						myJDBCTutorialUtilities.dbms);

				myTable.insert(dpl, 1);

			} catch (SQLException e) {
				JDBCTutorialUtilities.printSQLException(e);
			} finally {
				JDBCTutorialUtilities.closeConnection(myConnection);
			}
			Utils.Log(TAG,"-----out--1-updateprice---");
		}
	}

	public void updateNewHouseInfo(List<Item> newdp) {
		
		synchronized (this) {
			Connection myConnection = null;

			try {
				System.out.println("-----in--1-updateNewHouseInfo---");
				myConnection = myJDBCTutorialUtilities.getConnection();

				DealInfoDateTableByDay myTable = new DealInfoDateTableByDay(myConnection,
						myJDBCTutorialUtilities.dbName,
						myJDBCTutorialUtilities.dbms);

				//myTable.insert(dpl, 1);

			} catch (SQLException e) {
				JDBCTutorialUtilities.printSQLException(e);
			} finally {
				JDBCTutorialUtilities.closeConnection(myConnection);
			}
			System.out.println("-----out--1-updateprice---");
		}
	}
	
	public List<DataStruct.Item> getPrice(int type, java.sql.Date d) {

		synchronized (this) {
			System.out.println("-----in---getPrice---");
			Connection myConnection = null;

			System.out.println("-----out---getPrice---");
			return null;
		}
	}





}
