package co.nubetech.crux.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.hibernate.Session;
import org.junit.Test;

import co.nubetech.crux.dao.DBConnection;
import co.nubetech.crux.dao.DashboardDAO;
import co.nubetech.crux.dao.ReportDAO;
import co.nubetech.crux.model.Dashboard;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.util.CruxError;

public class TestDashboardAction extends DBConnection {
	
	@Test
	public void testGetDashboardWhenReportsAreOnDashboard(){
		Report report1 = new Report(null,"first",null,new Dashboard(1,1));
		Report report3 = new Report(null,"third",null,new Dashboard(0,0));
		
		ArrayList<Report> reportList = new ArrayList<Report>();
		reportList.add(report1);
		reportList.add(report3);
		
		ReportDAO reportDAO = mock(ReportDAO.class);
		when(reportDAO.findDashboardReports()).thenReturn(reportList);
		
		DashBoardAction dashboardAction = new DashBoardAction();
		dashboardAction.setReportDAO(reportDAO);
		String result = dashboardAction.getDashBoard();
		ArrayList<Report> fetchedReportList = dashboardAction.getReportListForDashBoard();
		
		assertEquals(result,"success");
		assertEquals(fetchedReportList.size(),2);
		assertEquals(fetchedReportList.get(0),report1);
		assertEquals(fetchedReportList.get(1),report3);		
	}
	
	@Test
	public void testGetDashboardWhenNoReportsAreOnDashboard(){
		
		ArrayList<Report> reportList = new ArrayList<Report>();
		
		ReportDAO reportDAO = mock(ReportDAO.class);
		when(reportDAO.findDashboardReports()).thenReturn(reportList);
		
		DashBoardAction dashboardAction = new DashBoardAction();
		dashboardAction.setReportDAO(reportDAO);
		String result = dashboardAction.getDashBoard();
		ArrayList<Report> fetchedReportList = dashboardAction.getReportListForDashBoard();
		CruxError error = dashboardAction.getError();
		assertEquals(result,"success");
		assertEquals(fetchedReportList.size(),0);
		assertTrue(error.isError());	
	}
	
	@Test
	public void testGetDashboardWhenNoReports(){
				
		ReportDAO reportDAO = mock(ReportDAO.class);
		when(reportDAO.findAll()).thenReturn(new ArrayList<Report>());
		
		DashBoardAction dashboardAction = new DashBoardAction();
		dashboardAction.setReportDAO(reportDAO);
		String result = dashboardAction.getDashBoard();
		ArrayList<Report> fetchedReportList = dashboardAction.getReportListForDashBoard();
		CruxError error = dashboardAction.getError();
		assertEquals(result,"success");
		assertEquals(fetchedReportList.size(),0);
		assertTrue(error.isError());	
	}
	
	@Test
	public void testSaveDashboard() throws Exception{
		Statement stmt = getStatement();
		stmt.execute("insert into dashboard values(99999,0,0)");
		stmt.execute("insert into dashboard values(99998,2,0)");
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
		.getNewSession();
		try {
		DashboardDAO dashboardDAO = new DashboardDAO();
		dashboardDAO.session = session;
		dashboardDAO.transaction = session.getTransaction();
		ArrayList<String> dashboardInfo = new ArrayList<String>();
		dashboardInfo.add("99999:1:0");
		dashboardInfo.add("99998:2:2");
		DashBoardAction dashboardAction = new DashBoardAction();
		dashboardAction.setDashboardInfo(dashboardInfo);
		dashboardAction.setDashboardDAO(dashboardDAO);
		dashboardAction.saveDashBoard();
		ResultSet rs = stmt.executeQuery("select * from dashboard where id=99999");
		
		while (rs.next()) {
			assertEquals(rs.getInt("indexNo"), 1);
			assertEquals(rs.getInt("columnNo"), 0);
		}
		
		ResultSet rs1 = stmt.executeQuery("select * from dashboard where id=99998");
		while (rs1.next()) {
			assertEquals(rs1.getInt("indexNo"), 2);
			assertEquals(rs1.getInt("columnNo"), 2);
		}
		rs.close();
		rs1.close();
		} finally{
			stmt.execute("delete from dashboard where id>=99998");
			stmt.close();
			session.close();
		}
	}
	
/*	@Test
	public void testGetDashboardAction() throws Exception{
		 HashMap<String, String> params = new HashMap<String, String>();
		  ServletContext servletContext = mock(ServletContext.class);
		  Dispatcher dispatcher = StrutsTestContext.prepareDispatcher(
			       servletContext, params);

		  StrutsTestContext context = new StrutsTestContext(
			      dispatcher, servletContext);
		  Map<String, String> requestParams = new HashMap<String, String>();
		  Map<String, Object> sessionAttributes = new HashMap<String, Object>();

		  ActionProxy proxy = context.createActionProxy(
			      "/",      // namespace
			      "getDashBoard", // Action
			      requestParams,
			      sessionAttributes);

		  assertTrue(proxy.getAction() instanceof DashBoardAction);
		  DashBoardAction action = (DashBoardAction) proxy.getAction();
		  
		    Report report1 = new Report(null,"first",null,new Dashboard(1,1));
			Report report3 = new Report(null,"third",null,new Dashboard(0,0));
			
			ArrayList<Report> reportList = new ArrayList<Report>();
			reportList.add(report1);
			reportList.add(report3);
			
			ReportDAO reportDAO = mock(ReportDAO.class);
			when(reportDAO.findDashboardReports()).thenReturn(reportList);
		    action.setReportDAO(reportDAO);
		    
		    String result = proxy.execute();
		    assertEquals("success", result);
	}*/
}
