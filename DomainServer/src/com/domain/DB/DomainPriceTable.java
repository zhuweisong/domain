package com.domain.DB;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.DomainConst;
import com.domain.constvalue.Struct;
import com.domain.constvalue.Struct.DomainPrice;

public class DomainPriceTable {

	private Connection con;
	private String dbms;
	static final int SELECT_BYDOMIAN = 1;
	static final int SELECT_BYTYPE = 2;

	public DomainPriceTable(Connection connArg, String dbNameArg, String dbmsArg) {
		super();
		this.con = connArg;
		this.dbms = dbmsArg;
	}

	public void createTable() throws SQLException {

		String createString = "create table IF not exists domainprice "
				+ "(domain varchar(32) NOT NULL, " + "type int(32) NOT NULL, "
				+ "price int(32) DEFAULT 0, " + "pricetype int(32) NOT NULL, "
				+ "fromWeb int(32) DEFAULT 1, " + "priceDate date not null, "
				+ "inforer char(16) DEFAULT '', " + "memo varchar(512), "
				+ "PRIMARY KEY(domain, priceDate))";
		
//		create table Domainmail
//		(domain varchar(32) NOT NULL,
//		oldprice int(32) DEFAULT 0, 
//		newprice int(32) DEFAULT 0, 
//		priceDate date not null,
//		mailer char(16) DEFAULT '',
//		PRIMARY KEY(domain, priceDate));
		
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(createString);
		} catch (SQLException e) {
			JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public void dropTable() throws SQLException {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			if (this.dbms.equals("mysql")) {
				stmt.executeUpdate("DROP TABLE IF EXISTS DomainPrice");
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
			java.sql.Date createdDate = DomainUtil.getCurrentSqlDate();

			ResultSet uprs = stmt.executeQuery("SELECT * FROM DomainPrice");
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

	private String getUpdateSql(DomainPrice dpA) {

		String domain = dpA.domain;
		StringBuilder sb = new StringBuilder();

		switch (dpA.op) {
		case DomainConst.DOMAIN_PRICE_OPRATOR_INSERT:
			sb.append("INSERT INTO domainprice ");
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
			sb.append("',''");
			sb.append(",''");
			sb.append(")");
			break;

		case DomainConst.DOMAIN_PRICE_OPRATOR_UPDATE:
			sb.append("UPDATE domainprice SET ");
			sb.append("price=");
			sb.append(dpA.price);
			sb.append(" ,");
			sb.append(" pricetype=");
			sb.append(dpA.pricetype);
			sb.append(" ,");
			sb.append(" fromWeb=");
			sb.append(dpA.fromWeb);
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

	public void batchUpdate(List<DomainPrice> dpl) throws SQLException {

		Statement stmt = null;
		try {

			this.con.setAutoCommit(false);
			stmt = this.con.createStatement();

			for (DomainPrice entry : dpl) {
				String sql = getUpdateSql(entry);
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

	public String getUrl(int op, List<Struct.DomainPrice> dpl, int dtype, int dpricetype) {
		StringBuilder sb = new StringBuilder();
	
		switch (op) {
		case SELECT_BYDOMIAN:
			sb.append("SELECT * FROM domainprice WHERE priceDate='");
			sb.append(DomainUtil.getCurrentSqlDate());
			sb.append("'");
			sb.append(" AND domain IN (");

			for (DomainPrice dp : dpl) {
				sb.append("'");
				sb.append(dp.domain);
				sb.append("'");
				sb.append(",");
			}
			
			String sql = sb.toString();
			sql = sql.substring(0, sql.length()-1);//Remove the Last ','

			return sql + ")";
			
		case SELECT_BYTYPE:
			sb.append("SELECT * FROM domainprice WHERE pricetype=");
			sb.append(dpricetype);
			sb.append(" AND type=");
			sb.append(dtype);
			
			break;
		}
		
		return sb.toString();
	}

	public Map<String, DomainPrice> viewTable(List<Struct.DomainPrice> dpA) {

		Map<String, DomainPrice> dpB = new HashMap<String, DomainPrice>();
		
		if (dpA.size()>0) {
			String sql = getUrl(SELECT_BYDOMIAN, dpA, 0,0);
			
			try {
				ExecuteSQL(sql, dpB);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dpB;
	}

	public Map<String, DomainPrice> viewTable(int qtype, int qpricetype) {

		Map<String, DomainPrice> dpB = new HashMap<String, DomainPrice>();
		String sql = getUrl(SELECT_BYTYPE, null, qtype,qpricetype);
		
		try {
			ExecuteSQL(sql, dpB);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dpB;
	}
	
	private void ExecuteSQL(String sql, Map<String, DomainPrice> dpl)
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

				DomainPrice p1 = new Struct.DomainPrice(domainName, type,
						price, pricetype, fromWeb, date);

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

	public static Map<String, DomainPrice> viewTable(Connection con,
			int domaintype, int start, int end) throws SQLException {
		Map<String, DomainPrice> dpl = new HashMap<String, DomainPrice>();

		Statement stmt = null;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM domainprice WHERE priceDate='");
		sb.append(DomainUtil.getCurrentSqlDate());
		sb.append("'");
		sb.append(" AND type=");
		sb.append(domaintype);
		sb.append(" HAVING SUBSTRING_INDEX(domain,'.',1)>=");
		sb.append(start);
		sb.append(" AND");
		sb.append(" SUBSTRING_INDEX(domain,'.',1)<");
		sb.append(end);

		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sb.toString());

			while (rs.next()) {
				String domainName = rs.getString("domain");
				int type = rs.getInt("type");
				int price = rs.getInt("price");
				int pricetype = rs.getInt("pricetype");
				int fromWeb = rs.getInt("fromWeb");
				java.sql.Date date = rs.getDate("priceDate");

				DomainPrice p1 = new Struct.DomainPrice(domainName, type,
						price, pricetype, fromWeb, date);

				dpl.put(domainName, p1);
			}

		} catch (SQLException e) {
			JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return dpl;
	}
}
