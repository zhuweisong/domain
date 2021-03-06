package com.domain.DB;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DomainDBBase {
	protected Connection con;
	protected String tName; 

	public DomainDBBase(Connection connArg, String dbNameArg, String tName) {
		super();
		this.con = connArg;
		this.tName = tName;
	}
	
	public String getSQLCreateTable() {
		String createString = "";
		return createString;
	}
	
	public void getSQLBatchUpdate(Statement stmt, int index) throws SQLException {
	}
	
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
	
	public void batchUpdate(int index) throws SQLException {

		Statement stmt = null;
		try {

			this.con.setAutoCommit(false);
			stmt = this.con.createStatement();

			getSQLBatchUpdate(stmt, index);

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
