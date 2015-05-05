package com.domain.DB;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct;
import com.domain.constvalue.Struct.DomainAuction;
import com.domain.constvalue.Struct.DomainPrice;


public class DomainAuctionURLTable extends DomainDBBase {
	static final private int SQL_TYPE_VIEW_ALL = 1;
	static final private int SQL_TYPE_VIEW_BY_NAME = 2;
	static final private int SQL_TYPE_UPDATE = 3;
	
	public DomainAuctionURLTable(Connection connArg, String dbNameArg,
			String tName) {
		super(connArg, dbNameArg, "DomainAuctionURL");
		// TODO Auto-generated constructor stub
	}
	
	public String getSQLCreateTable() {
		String createString = "create table DomainAuctionURL "
				+ "(domain varchar(32) NOT NULL, " + "type int(32) NOT NULL, "
				+ "price int(32) DEFAULT 0, " + "pricetype int(32) NOT NULL, "
				+ "fromWeb int(32) DEFAULT 1, " + "priceDate date not null, "
				+ "URL varchar(64), " + "AcutionEnd Time NOT NULL, "
				+ "PRIMARY KEY(domain, priceDate));";
		return createString;
	}
	
	public Map<String, DomainAuction> viewTable(List<DomainAuction> dpA) {

		Map<String, DomainAuction> dpB = new HashMap<String, DomainAuction>();
		String sql = getSQL(SQL_TYPE_VIEW_BY_NAME, dpA);
		
		try {
			ExecuteSQL(sql, dpB);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dpB;
	}

	public Map<String, DomainAuction> viewTableAll() {

		Map<String, DomainAuction> dpB = new HashMap<String, DomainAuction>();
		String sql = getSQL(SQL_TYPE_VIEW_ALL, null);
		
		try {
			ExecuteSQL(sql, dpB);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dpB;
	}
	
	private void ExecuteSQL(String sql, Map<String, DomainAuction> dpl)
			throws SQLException {

		Statement stmt = null;

		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String domainName = rs.getString("domain");
				int type = rs.getInt("type");
				int price = rs.getInt("price");
				int pricetype = rs.getInt("pricetype");
				int fromWeb = rs.getInt("fromWeb");
				java.sql.Date date = rs.getDate("priceDate");
				java.sql.Time AcutionEnd = rs.getTime("AcutionEnd");
				String url = rs.getString("URL");
				
				DomainAuction p1 = new Struct.DomainAuction(domainName, type,
						price, pricetype, fromWeb, date, url, AcutionEnd);

				dpl.put(domainName, p1);
			}

		} catch (SQLException e) {
			JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public String getSQL(int sqltype, List<DomainAuction> dpl) {
		StringBuilder sb = new StringBuilder();
		String sql = "";
		
		switch (sqltype) {
			case SQL_TYPE_VIEW_BY_NAME:
				sb.append("SELECT * FROM DomainAuctionURL WHERE priceDate='");
				sb.append(DomainUtil.getCurrentSqlDate());
				sb.append("'");
				sb.append(" AND domain IN (");

				for (DomainPrice dp : dpl) {
					sb.append("'");
					sb.append(dp.domain);
					sb.append("'");
					sb.append(",");
				}
				
				sql = sb.toString();
				sql = sql.substring(0, sql.length()-1);//Remove the Last ','
				sql = sql + ")";
				break;
				
			case SQL_TYPE_VIEW_ALL:
				sb.append("SELECT * FROM DomainAuctionURL WHERE priceDate='");
				sb.append(DomainUtil.getCurrentSqlDate());
				sb.append("'");
				sql = sb.toString();
				break;
				
			case SQL_TYPE_UPDATE:
				break;
		}



		return sql;
	}
	
	public void batchUpdate(List<DomainAuction> dpl) throws SQLException {
		
		Statement stmt = null;
		try {

			this.con.setAutoCommit(false);
			stmt = this.con.createStatement();

			for (DomainAuction dw : dpl) {
				String sql = getUpdateSql(dw);
				if (!sql.isEmpty())
					stmt.addBatch(sql);
			}

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

	private String getUpdateSql(DomainAuction dpA) {

		String domain = dpA.domain;
		StringBuilder sb = new StringBuilder();

		switch (dpA.op) {
		case DomainConst.DOMAIN_PRICE_OPRATOR_INSERT:
			sb.append("INSERT INTO DomainAuctionURL ");
			sb.append("VALUES('");
			sb.append(domain);
			sb.append("',");
			sb.append(dpA.type);
			sb.append(",");
			sb.append(dpA.price);
			sb.append(",");
			sb.append(dpA.pricetype);
			sb.append(",");
			sb.append(dpA.fromWeb);
			sb.append(",'");
			sb.append(dpA.priceDate);
			sb.append("','");
			sb.append(dpA.AuctionUrl);
			sb.append("','");
			sb.append(dpA.AuctionEnd);
			sb.append("')");
			break;

		case DomainConst.DOMAIN_PRICE_OPRATOR_UPDATE:
			sb.append("UPDATE DomainAuctionURL SET ");
			sb.append("price=");
			sb.append(dpA.price);
			sb.append(",");
			sb.append(" pricetype=");
			sb.append(dpA.pricetype);
			sb.append(",");
			sb.append(" fromWeb=");
			sb.append(dpA.fromWeb);
			sb.append(",");
			sb.append(" URL='");
			sb.append(dpA.AuctionUrl);
			sb.append("',");
			sb.append(" AcutionEnd='");
			sb.append(dpA.AuctionEnd);
			sb.append("'");
			sb.append(" WHERE ");
			sb.append("domain='");
			sb.append(domain);
			sb.append("' AND ");
			sb.append("priceDate='");
			sb.append(dpA.priceDate);
			sb.append("'");
			break;
		}

		return sb.toString();
	}
}
