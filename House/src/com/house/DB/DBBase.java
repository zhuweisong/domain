package com.house.DB;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


abstract public class DBBase {
	protected Connection con;
	protected String tName; 
	
	public DBBase(Connection connArg, String dbNameArg, String tName) {
		super();
		this.con = connArg;
		this.tName = tName;
	}
	
	abstract public String getSQLCreateTable();

	
	public void createTable()  {

		String createString = getSQLCreateTable();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(createString);
		} catch (SQLException e) {
			JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void dropTable() throws SQLException {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS " + tName);
		} catch (SQLException e) {
			JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	/**
	 * 批量执行sql
	 * @param sqls
	 * @throws SQLException
	 */
	protected void batchUpdate(List<String> sqls) throws SQLException {

		Statement stmt = null;
		try {
			this.con.setAutoCommit(false);
			stmt = this.con.createStatement();
			
			for (String sql : sqls) {
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
}
