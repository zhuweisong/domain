package com.house.DB;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.house.constvalue.DataStruct;
import com.house.utils.Utils;

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
	
	@Override
	public String getSQLCreateTable(){
		String createString = "create table IF not exists " 
				+ TABLE_NAME
				+ "(domain varchar(32) NOT NULL, " + "type int(32) NOT NULL, "
				+ "price int(32) DEFAULT 0, " + "pricetype int(32) NOT NULL, "
				+ "fromWeb int(32) DEFAULT 1, " + "priceDate date not null, "
				+ "inforer char(16) DEFAULT '', " + "memo varchar(512), "
				+ "PRIMARY KEY(domain, priceDate))";
		
		return createString;
	}
	
	public void getSQLBatchUpdate(Statement stmt, int index) {
		
	}
	
	protected void onQueryResult(ResultSet rs, int type) {
		
		switch (type) {
		case 1:
//			String domainName = rs.getString("domain");
//			int type1 = rs.getInt("type");
//			int price = rs.getInt("price");
//			int pricetype = rs.getInt("pricetype");
//			int fromWeb = rs.getInt("fromWeb");
//			java.sql.Date date = rs.getDate("priceDate");

//			DomainPrice p1 = new Struct.DomainPrice(domainName, type1,
//					price, pricetype, fromWeb, date);


			break;

		default:
			break;
		}

	}
	
	
	private void ExecuteSQLQuery(String sql, int type)
			throws SQLException {

		Statement stmt = null;

		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
//				dpl.put(domainName, p1);
			}

		} catch (SQLException e) {
			JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	
	public void insertRow(String domain, int type, int price, int isHot,
			int fromWeb) throws SQLException {
		Statement stmt = null;
		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			java.sql.Date createdDate = Utils.getCurrentSqlDate();

			ResultSet uprs = stmt.executeQuery("SELECT * FROM " + TABLE_NAME);
			uprs.moveToInsertRow();

			uprs.updateString("domain", domain);
			uprs.updateInt("type", type);
			uprs.updateFloat("price", price);
			uprs.updateInt("priceType", isHot);
			uprs.updateInt("fromWeb", fromWeb);
			uprs.updateDate("priceDate", createdDate);

			uprs.insertRow();
			uprs.beforeFirst();

		} catch (SQLException e) {
			JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	

	public void batchUpdate(List<DataStruct.Item> dpl) throws SQLException {

		Statement stmt = null;
		try {

			this.con.setAutoCommit(false);
			stmt = this.con.createStatement();
//			for (DomainPrice entry : dpl) {
//				String sql = getUpdateSql(entry);
//				if (!sql.isEmpty())
//					stmt.addBatch(sql);
//			}

			stmt.executeBatch();
			this.con.commit();

		} catch (BatchUpdateException b) {
			JDBCTutorialUtilities.printBatchUpdateException(b);
		} catch (SQLException ex) {
			JDBCTutorialUtilities.printSQLException(ex);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			this.con.setAutoCommit(true);
		}
	}

	
}
