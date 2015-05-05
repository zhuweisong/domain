package com.domain.DB;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct.DomainWhois;


public class DomainStatis extends DomainDBBase{
	
	static final int SQL_UPDATE_PRICE = 1;
	static final int SQL_UPDATE_SELLED = 2;
	static final int SQL_CREATE_PRICE = 3;
	static final int SQL_CREATE_SELLED = 4;
	
	List<Percent> pl = new ArrayList<Percent>();
	
	class Percent {
		public Percent(int type2, int records2,java.sql.Date date) {
			this.type = type2;
			this.records = records2;
			this.date = date;
		}
		int type;
		int records;
		java.sql.Date date;
	};
	
	public DomainStatis(Connection connArg, String dbNameArg, String tName) {
		super(connArg, dbNameArg, "statisSelled");
	}
	
	public String getSQLCreateTable() {
		return getSQL(SQL_UPDATE_SELLED, null);
	}
	
	//create table statisPrice (type int(32) NOT NULL, pricedate date not null, aveprice int(32) NOT NULL, PRIMARY KEY(type, pricedate));
	//CREATE VIEW DomainSelled AS SELECT type, max(update_date) as update_date, domain FROM domainwhois group by domain having count(update_date)>1;
	//CREATE TABLE mailmap (mail varchar(64) NOT NULL, admin varchar(32), memowho varchar(32), memowhen date not null,PRIMARY KEY(mail));


	public void update(){
		statisSelled();
		statisPrice();
	}
	
	public void statisSelled() {
		
		try {
			batchUpdate(SQL_UPDATE_SELLED);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void getSQLBatchUpdate(Statement stmt, int index) throws SQLException {
		String sql = null;
		switch (index) {
		case SQL_UPDATE_SELLED:
			sql = getSQL(SQL_UPDATE_SELLED, null);
			if (!sql.isEmpty()) 
				stmt.addBatch(sql);
			break;
			
		case SQL_UPDATE_PRICE:
			for (Percent p : pl) {
				sql = getSQL(SQL_UPDATE_PRICE, p);
				if (!sql.isEmpty())
					stmt.addBatch(sql);
			}
			break;
		}

	}
	
	public void statisPrice() {
		
		try {
			getPercent();
			batchUpdate(SQL_UPDATE_PRICE);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void getPercent() throws SQLException {
		//去掉每个分类中最高价的20%
		java.sql.Date pre = DomainUtil.getPreDate(DomainUtil.getCurrentSqlDate(), -1);
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT type, ROUND(COUNT(*)*80/100) AS percents FROM domainprice where pricedate='");
		sb.append(pre);
		sb.append("' AND (pricetype=6 OR pricetype=5 OR pricetype=10) GROUP BY type;");
		
		Statement stmt = null;

		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sb.toString());

			while (rs.next()) {

				int type = rs.getInt("type");
				int records = rs.getInt("percents");
				
				Percent p = new Percent(type, records, pre);
				pl.add(p);
			}

		} catch (SQLException e) {
			JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	
	protected String getSQL(int type, Percent p) {
		
		StringBuilder sb = new StringBuilder();

		switch (type) {
			case SQL_UPDATE_PRICE:
				sb.append("INSERT INTO statisPrice(type, pricedate, aveprice)");
				sb.append(" SELECT A.type, A.pricedate, avg(A.price) as aveprice FROM");
				sb.append(" (SELECT type, price, pricedate from domainprice WHERE type=");
				sb.append(p.type); 
				sb.append(" AND pricedate='"); 
				sb.append(p.date); 
				sb.append("'"); 
				sb.append(" AND (pricetype=6 or pricetype=5 or pricetype=10) ORDER BY price LIMIT ");
				sb.append(p.records); 
				sb.append(") AS A"); 
				break;
				
			case SQL_UPDATE_SELLED:
				java.sql.Date pre = DomainUtil.getPreDate(DomainUtil.getCurrentSqlDate(), -1);
				sb.append("INSERT INTO statisselled(type, update_date, cntselled)");
				sb.append(" SELECT type,update_date,COUNT(domain) as cnt FROM DomainSelled WHERE update_date='");
				sb.append(pre);
				sb.append("' GROUP BY type");
				break;
				
			case SQL_CREATE_PRICE:
				sb.append("create table statisselled (type int(32) NOT NULL, update_date date not null, cntselled int(32) NOT NULL, PRIMARY KEY(type, update_date));");
				break;
				
			case SQL_CREATE_SELLED:
				sb.append("create table statisPrice (type int(32) NOT NULL, pricedate date not null, aveprice int(32) NOT NULL, PRIMARY KEY(type, pricedate));");
				break;
		}
		
		return sb.toString();
	}
}
