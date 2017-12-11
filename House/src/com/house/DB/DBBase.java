package com.house.DB;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


abstract public class DBBase {
	protected Connection con;
	protected String tName; 

	public DBBase(Connection connArg, String dbNameArg, String tName) {
		super();
		this.con = connArg;
		this.tName = tName;
	}
	
	abstract public String getSQLCreateTable();

	
	public void createTable() throws SQLException {

		String createString = getSQLCreateTable();
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
			stmt.executeUpdate("DROP TABLE IF EXISTS " + tName);
		} catch (SQLException e) {
			JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
}
