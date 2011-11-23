package co.nubetech.crux.dao;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.Statement;

import org.hibernate.Session;
import org.junit.Test;

import co.nubetech.crux.model.Dashboard;
import co.nubetech.crux.util.CruxException;

public class TestDashboardDAO extends DBConnection {

	@Test
	public void testFindById() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.execute("insert into dashboard values(99999,1,1)");
		try {
			Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
			.getNewSession();
			
		DashboardDAO dashboardDAO = new DashboardDAO();
		dashboardDAO.session = session;
		Dashboard dashboard = dashboardDAO.findById(99999l);
		assertEquals(1,dashboard.getIndex());
		assertEquals(1,dashboard.getColumn());
		
		} finally{
		stmt.execute("delete from dashboard where id=99999");
		stmt.close();
		}
	}
	
	@Test
	public void testSave() throws Exception, CruxException {
		Statement stmt = getStatement();
		long dashboardId=0;
		try {
			Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
			.getNewSession();
			
		DashboardDAO dashboardDAO = new DashboardDAO();
		dashboardDAO.session = session;
		dashboardDAO.transaction = session.getTransaction();
		Dashboard dashboard = new Dashboard(1,1);
		dashboardId = dashboardDAO.save(dashboard);
		ResultSet rs = stmt.executeQuery("select * from dashboard where id="
				+ dashboardId);
		
		while (rs.next()) {
			assertEquals(rs.getInt("indexNo"), 1);
			assertEquals(rs.getInt("columnNo"), 1);
		}
		
		} finally{
		stmt.execute("delete from dashboard where id="+dashboardId);
		stmt.close();
		}
	}
	
	@Test
	public void testUpdate() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.execute("insert into dashboard values(99999,1,1)");
		try {
			Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
			.getNewSession();
			
		DashboardDAO dashboardDAO = new DashboardDAO();
		dashboardDAO.session = session;
		dashboardDAO.transaction = session.getTransaction();
		Dashboard dashboard =dashboardDAO.findById(99999l);
		dashboard.setColumn(2);
		dashboardDAO.save(dashboard);
		ResultSet rs = stmt.executeQuery("select * from dashboard where id=99999");
		
		while (rs.next()) {
			assertEquals(rs.getInt("indexNo"), 1);
			assertEquals(rs.getInt("columnNo"), 2);
		}
		
		} finally{
		stmt.execute("delete from dashboard where id=99999");
		stmt.close();
		}
	}
}
