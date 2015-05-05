package com.domain.DB;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domain.constvalue.DomainConst;
import com.domain.constvalue.Struct.DomainPrice;
import com.domain.constvalue.Struct.DomainWhois;

public class DomainWhoisTable extends DomainDBBase {
	static final int SELECT_RENCENT = 1;
	static final int SELECT_CONDITION = 2;
	static final int SELECT_DOMAIN = 3;

	
	public DomainWhoisTable(Connection connArg, String dbNameArg, String dbmsArg) {
		super(connArg,dbNameArg,dbmsArg);
	}

	public void createTable() throws SQLException {
		String createString = "create table DomainWhois "
				+ "(domain varchar(32) NOT NULL, " + "type int(32) NOT NULL, "
				+ "reg_email varchar(128), " + "reg_name varchar(128), "
				+ "reg_phone varchar(32), " + "reg_url varchar(128), "
				+ "reg_country varchar(32), " + "update_date date not null, "
				+ "PRIMARY KEY(domain, update_date));";

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
			stmt.executeUpdate("DROP TABLE IF EXISTS domainwhois");
		} catch (SQLException e) {
			JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}


	public Map<String, DomainWhois> viewTable(int dType, int start, int end)
			throws SQLException {
		String sql = getQuerySQL(SELECT_RENCENT, null, dType, "", "", start,
				end, null);
		return executeQuery(sql);
	}

	public Map<String, DomainWhois> viewTable(int dType, java.sql.Date d,
			String mail, String name) throws SQLException {

		String sql = getQuerySQL(SELECT_CONDITION, d, dType, "", "", 0, 0, null);

		return executeQuery(sql);
	}

	public Map<String, DomainWhois> viewTable(List<DomainWhois> dw, int dType)
			throws SQLException {
		String sql = getQuerySQL(SELECT_DOMAIN, null, dType, "", "", 0, 0, dw);
		return executeQuery(sql);
	}

	Map<String, DomainWhois> executeQuery(String sql) throws SQLException {

		Statement stmt = null;
		Map<String, DomainWhois> dwl = new HashMap<String, DomainWhois>();

		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String domain = rs.getString("domain");
				int type = rs.getInt("type");
				String reg_email = rs.getString("reg_email");
				String reg_name = rs.getString("reg_name");
				String reg_phone = rs.getString("reg_phone");
				String reg_URL = rs.getString("reg_URL");
				String reg_country = rs.getString("reg_country");
				java.sql.Date date = rs.getDate("update_date");

				DomainWhois p1 = new DomainWhois(domain, type, reg_email,
						reg_name, reg_phone, reg_URL, reg_country, date);

				// System.out.println(p1.toString());

				dwl.put(domain, p1);
			}

		} catch (SQLException e) {
			JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return dwl;

	}

	public void batchUpdate(List<DomainWhois> dwl) throws SQLException {
		if (dwl.size()<0)
			return;
		
		Statement stmt = null;
		try {

			this.con.setAutoCommit(false);
			stmt = this.con.createStatement();

			for (DomainWhois dw : dwl) {
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
	
	private String getUpdateSql(DomainWhois dw) {

		StringBuilder sb = new StringBuilder();

		switch (dw.op) {
		case DomainConst.DOMAIN_PRICE_OPRATOR_INSERT:
			sb.append(" INSERT INTO domainwhois");
			sb.append(" VALUES('");
			sb.append(dw.domain);
			sb.append("',");
			sb.append(dw.type);
			sb.append(",'");
			sb.append(dw.reg_email);
			sb.append("','");
			sb.append(dw.reg_name);
			sb.append("','");
			sb.append(dw.reg_phone);
			sb.append("','");
			sb.append(dw.reg_URL);
			sb.append("','");
			sb.append(dw.reg_country);
			sb.append("','");
			sb.append(dw.reg_date);
			sb.append("')");
			break;

		case DomainConst.DOMAIN_PRICE_OPRATOR_UPDATE:
			sb.append(" UPDATE domainwhois");
			sb.append(" SET type=");
			sb.append(dw.type);
			sb.append(",reg_email='");
			sb.append(dw.reg_email);
			sb.append("',");
			sb.append(" reg_name='");
			sb.append(dw.reg_name);
			sb.append("',");
			sb.append(" reg_phone='");
			sb.append(dw.reg_phone);
			sb.append("',");
			sb.append(" reg_URL='");
			sb.append(dw.reg_URL);
			sb.append("',");
			sb.append(" reg_country='");
			sb.append(dw.reg_country);
			sb.append("'");
			sb.append(" WHERE domain='");
			sb.append(dw.domain);
			sb.append("' AND");
			sb.append(" update_date='");
			sb.append(dw.reg_date);
			sb.append("'");
			break;

		case DomainConst.DOMAIN_PRICE_OPRATOR_DELETE:
			sb.append(" DELETE FROM domainwhois WHERE domain='");
			sb.append(dw.domain);
			sb.append("' AND update_date='");
			sb.append(dw.reg_date);
			sb.append("'");
			break;

		default:
			break;
		}

		return sb.toString();
	}

	String getQuerySQL(int type, java.sql.Date d, int dType, String name,
			String mail, int start, int end, List<DomainWhois> dwl) {
		StringBuilder sb = new StringBuilder();

		switch (type) {
		case SELECT_CONDITION:

			sb.append("SELECT domain, type, reg_email, reg_name, reg_phone, reg_URL, reg_country, update_date FROM domainwhois ");
			sb.append("WHERE update_date='");
			sb.append(d);
			sb.append("' AND type=");
			sb.append(dType);
			sb.append(" AND reg_name='");
			sb.append(name);
			sb.append("' AND reg_email='");
			sb.append(mail);
			sb.append("'");
			break;

		case SELECT_RENCENT:
			sb.append(" SELECT A.domain, A.type, A.reg_email, A.reg_name, A.reg_phone, A.reg_URL, A.reg_country, A.update_date FROM");
			sb.append(" (SELECT domain, type, reg_email, reg_name, reg_phone, reg_URL, reg_country, update_date FROM domainwhois");
			sb.append(" WHERE type=");
			sb.append(dType);
			sb.append(" HAVING SUBSTRING_INDEX(domain,'.',1)>=");
			sb.append(start);
			sb.append(" AND");
			sb.append(" SUBSTRING_INDEX(domain,'.',1)<");
			sb.append(end);
			sb.append(" ) AS A");
			sb.append(" JOIN");
			sb.append(" (SELECT domain, MAX(update_date) update_date FROM domainwhois WHERE type=");
			sb.append(dType);
			sb.append(" GROUP BY domain) AS B");
			sb.append(" ON A.domain=B.domain AND A.update_date=B.update_date;");
			break;

		case SELECT_DOMAIN:
			sb.append(" SELECT A.domain, A.type, A.reg_email, A.reg_name, A.reg_phone, A.reg_URL, A.reg_country, A.update_date FROM");
			sb.append(" (SELECT domain, type, reg_email, reg_name, reg_phone, reg_URL, reg_country, update_date FROM domainwhois");
			sb.append(" WHERE domain IN (");
			for (DomainWhois dp : dwl) {
				sb.append("'");
				sb.append(dp.domain);
				sb.append("'");
				sb.append(",");
			}
			String sqlt = sb.toString();
			sqlt = sqlt.substring(0, sqlt.length() - 1);// Remove the Last ','
			sb.setLength(0);
			sb.append(sqlt);
			sb.append(" )) AS A");
			sb.append(" JOIN");
			sb.append(" (SELECT domain, MAX(update_date) update_date FROM domainwhois WHERE type=");
			sb.append(dType);
			sb.append(" GROUP BY domain) AS B");
			sb.append(" ON A.domain=B.domain AND A.update_date=B.update_date;");

			break;
		}

		return sb.toString();
	}
}
