package com.domain.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.Struct;
import com.domain.constvalue.Struct.DomainAuction;
import com.domain.constvalue.Struct.DomainPrice;
import com.domain.constvalue.Struct.DomainWhois;
import com.domain.whois.FetchWhois;

public class DBUpdateProxy {
	private final String DBUtilFileName = "./properties/javadb-sample-properties.xml";
	private JDBCTutorialUtilities myJDBCTutorialUtilities;

	public DBUpdateProxy() {
		super();
	}

	public void init() {
		myJDBCTutorialUtilities = JDBCTutorialUtilities.init(DBUtilFileName);
	}

	public void updateprice(List<DomainPrice> dpl) {
		synchronized (this) {
			Connection myConnection = null;

			try {
				System.out.println("-----in--1-updateprice---");
				myConnection = myJDBCTutorialUtilities.getConnection();

				DomainPriceTable myTable = new DomainPriceTable(myConnection,
						myJDBCTutorialUtilities.dbName,
						myJDBCTutorialUtilities.dbms);

				// myCoffeeTable.createTable();

				Map<String, DomainPrice> dp2 = myTable.viewTable(dpl);

				DomainPriceUpdateStrategy up = new DomainPriceUpdateStrategy();
				up.MergePrice(dpl, dp2);
				myTable.batchUpdate(dpl);

			} catch (SQLException e) {
				JDBCTutorialUtilities.printSQLException(e);
			} finally {
				JDBCTutorialUtilities.closeConnection(myConnection);
			}
			System.out.println("-----out--1-updateprice---");
		}
	}

	public void updateBiddingURL(List<DomainAuction> dpl) {
		synchronized (this) {
			System.out.println("-----in---updateBiddingURL---");
			Connection myConnection = null;

			try {
				myConnection = myJDBCTutorialUtilities.getConnection();

				DomainAuctionURLTable myTable = new DomainAuctionURLTable(
						myConnection, myJDBCTutorialUtilities.dbName, "");

				// myTable.createTable();
				Map<String, DomainAuction> dp2 = myTable.viewTable(dpl);

				DomainAutcionStrategy up = new DomainAutcionStrategy();
				up.MergePrice(dpl, dp2);
				myTable.batchUpdate(dpl);

			} catch (SQLException e) {
				JDBCTutorialUtilities.printSQLException(e);
			} finally {
				JDBCTutorialUtilities.closeConnection(myConnection);
			}
			System.out.println("-----out---updateBiddingURL---");
		}
	}

	public void updateWhois(List<DomainWhois> dwl, int type, int start, int end) {

		synchronized (this) {
			System.out.println("-----in---updateWhois---");
			Connection myConnection = null;

			try {
				myConnection = myJDBCTutorialUtilities.getConnection();

				DomainWhoisTable whois = new DomainWhoisTable(myConnection,
						myJDBCTutorialUtilities.dbName,
						myJDBCTutorialUtilities.dbms);

				// whois.createTable();

				Map<String, DomainWhois> dw2 = whois
						.viewTable(type, start, end);
				DomainWhoisUpdateStrategy up = new DomainWhoisUpdateStrategy();
				up.Merge(dwl, dw2);

				whois.batchUpdate(dwl);

			} catch (SQLException e) {
				JDBCTutorialUtilities.printSQLException(e);
			} finally {
				JDBCTutorialUtilities.closeConnection(myConnection);
			}
			System.out.println("-----out---updateWhois---");
		}
	}
	
	public void updateWhois(List<DomainWhois> dwl, int type) {

		synchronized (this) {
			System.out.println("-----in---updateWhois---");
			Connection myConnection = null;

			try {
				myConnection = myJDBCTutorialUtilities.getConnection();

				DomainWhoisTable whois = new DomainWhoisTable(myConnection,
						myJDBCTutorialUtilities.dbName,
						myJDBCTutorialUtilities.dbms);

				Map<String, DomainWhois> dw2 = whois.viewTable(dwl, type);
				DomainWhoisUpdateStrategy up = new DomainWhoisUpdateStrategy();
				up.Merge(dwl, dw2);
				
//				for (DomainWhois dw : dwl) {
//					if (!dw.reg_email.isEmpty() || !dw.reg_name.isEmpty()) 
//						dw.op = DomainConst.DOMAIN_PRICE_OPRATOR_UPDATE;
//				}

				whois.batchUpdate(dwl);

			} catch (SQLException e) {
				JDBCTutorialUtilities.printSQLException(e);
			} finally {
				JDBCTutorialUtilities.closeConnection(myConnection);
			}
			System.out.println("-----out---updateWhois---");
		}
	}

	public void updateStatis() {
		synchronized (this) {
			System.out.println("-----in---updateStatis---");
			Connection myConnection = null;

			try {
				myConnection = myJDBCTutorialUtilities.getConnection();

				DomainStatis statis = new DomainStatis(myConnection,
						myJDBCTutorialUtilities.dbName,
						myJDBCTutorialUtilities.dbms);

				statis.update();

			} catch (SQLException e) {
				JDBCTutorialUtilities.printSQLException(e);
			} finally {
				JDBCTutorialUtilities.closeConnection(myConnection);
			}
			System.out.println("-----out---updateStatis---");
		}
	}

	public Map<String, DomainAuction> getAuctionURL() {
		synchronized (this) {
			System.out.println("-----in---getAuctionURL---");
			Connection myConnection = null;
			Map<String, DomainAuction> dp2 = null;

			try {

				myConnection = myJDBCTutorialUtilities.getConnection();

				DomainAuctionURLTable myTable = new DomainAuctionURLTable(
						myConnection, myJDBCTutorialUtilities.dbName, "");

				// myTable.createTable();
				dp2 = myTable.viewTableAll();

			} catch (SQLException e) {
				JDBCTutorialUtilities.printSQLException(e);
			} finally {
				JDBCTutorialUtilities.closeConnection(myConnection);
			}
			System.out.println("-----out---updateprice---");
			return dp2;

		}
	}

	public Map<String, DomainWhois> getEmptyWhois(int type, java.sql.Date d) {

		synchronized (this) {
			System.out.println("-----in---getEmptyWhois---");
			Connection myConnection = null;
			Map<String, DomainWhois> dw2 = null;

			try {
				myConnection = myJDBCTutorialUtilities.getConnection();

				DomainWhoisTable whois = new DomainWhoisTable(myConnection,
						myJDBCTutorialUtilities.dbName,
						myJDBCTutorialUtilities.dbms);

				dw2 = whois.viewTable(type, d, "", "");
				
				//ɾ������еĿհף���������������ظ���¼
				List<Map.Entry<String, DomainWhois>> dwB = new ArrayList<Map.Entry<String, DomainWhois>>();
				dwB.addAll(dw2.entrySet());
				List<DomainWhois> dwL = new ArrayList<DomainWhois>();
				for (java.util.Map.Entry<String, DomainWhois> entry : dwB) {
					DomainWhois dw = entry.getValue();
					dw.op = DomainConst.DOMAIN_PRICE_OPRATOR_DELETE;
					dwL.add(dw);
				}
				whois.batchUpdate(dwL);

			} catch (SQLException e) {
				JDBCTutorialUtilities.printSQLException(e);
			} finally {
				JDBCTutorialUtilities.closeConnection(myConnection);
			}

			System.out.println("-----out---getEmptyWhois---");
			return dw2;
		}
	}
	
	public Map<String, DomainPrice> getPrice(int type, int pricetype, java.sql.Date d) {

		synchronized (this) {
			System.out.println("-----in---getPrice---");
			Connection myConnection = null;
			Map<String, DomainPrice> dp2 = null;

			try {
				myConnection = myJDBCTutorialUtilities.getConnection();

				DomainPriceTable myTable = new DomainPriceTable(myConnection,
						myJDBCTutorialUtilities.dbName,
						myJDBCTutorialUtilities.dbms);

				dp2 = myTable.viewTable(type, pricetype);
				
			} catch (SQLException e) {
				JDBCTutorialUtilities.printSQLException(e);
			} finally {
				JDBCTutorialUtilities.closeConnection(myConnection);
			}

			System.out.println("-----out---getPrice---");
			return dp2;
		}
	}	

	public static void main(String[] args) {
		DBUpdateProxy proxy = new DBUpdateProxy();
		proxy.init();
		Map<String, DomainPrice> dw = proxy.getPrice(
				DomainConst.DOMAIN_TYPE_PIN, DomainConst.DOMAIN_QQ_BID, DomainUtil.getCurrentSqlDate());
		FetchWhois f = new FetchWhois();
		List<Struct.DomainWhois> dl = f.getAllWhois(dw, DomainConst.DOMAIN_TYPE_PIN);
		proxy.updateWhois(dl, DomainConst.DOMAIN_TYPE_PIN);
	}

}
