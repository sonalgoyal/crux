package co.nubetech.crux.action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import co.nubetech.crux.dao.ReportDAO;
import co.nubetech.crux.model.Dashboard;
import co.nubetech.crux.model.Datastore;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportType;
import co.nubetech.crux.model.User;

public class TestDeleteReportAction {
	@Test
	public void testDeleteReport(){
		User user = new User();
		user.setId(12);
		user.setName("user");
		user.setPassword("password1");

		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
		
		ReportType reportType1 = new ReportType(121, "ReportType1");
		ReportType reportType2= new ReportType(122, "ReportType2");
		ReportType reportType3 = new ReportType(123, "ReportType3");
		
		Dashboard dashboard = new Dashboard(1, 2);
		
		Report report1 = new Report(user,"Report1", reportType1, dashboard);
		Report report2 = new Report(user,"Report2", reportType2, dashboard);
		Report report3 = new Report(user,"Report3", reportType3, dashboard);
		
		List<Report> reportList = new ArrayList<Report>();
		reportList.add(report1);
		reportList.add(report2);
		reportList.add(report3);
		
		ReportDAO mockedReportDAO = mock(ReportDAO.class);
		DeleteReportAction deleteReportAction = new DeleteReportAction();
		deleteReportAction.setReportDAO(mockedReportDAO);
		when(mockedReportDAO.findAll()).thenReturn(reportList);
		
		String successString = deleteReportAction.deleteReport();
		
		assertEquals(successString, "success");
		
		assertEquals(deleteReportAction.getReportList().get(0).getName(), report1.getName());
		assertEquals(deleteReportAction.getReportList().get(1).getName(), report2.getName());
		assertEquals(deleteReportAction.getReportList().get(2).getName(), report3.getName());
		
		assertEquals(deleteReportAction.getReportList().get(0).getReportType(), report1.getReportType().getType());
		assertEquals(deleteReportAction.getReportList().get(1).getReportType(), report2.getReportType().getType());
		assertEquals(deleteReportAction.getReportList().get(2).getReportType(), report3.getReportType().getType());
		
		// Report view index starts with integer 1.
		assertEquals(deleteReportAction.getReportList().get(0).getIndex(), 1);
	}
	
	@Test
	public void testDeleteReportWithEmptyReports(){
		ReportType reportType = new ReportType(121, "ReportType");
		Report report = new Report();
		
		// Setting report type is must, otherwise NUllPointerException.
		report.setReportType(reportType);
		
		List<Report> reportList = new ArrayList<Report>();
		reportList.add(report);
		
		ReportDAO mockedReportDAO = mock(ReportDAO.class);
		DeleteReportAction deleteReportAction = new DeleteReportAction();
		deleteReportAction.setReportDAO(mockedReportDAO);
		when(mockedReportDAO.findAll()).thenReturn(reportList);
		
		String successString = deleteReportAction.deleteReport();
		
		assertEquals(successString, "success");
		
		assertEquals(deleteReportAction.getReportList().get(0).getName(), null);
		assertEquals(deleteReportAction.getReportList().get(0).getName(), report.getName());
		assertEquals(deleteReportAction.getReportList().get(0).getReportType(), report.getReportType().getType());
	}
	
	@Test
	public void testDeleteReportWithNoReports(){
		
		List<Report> reportList = new ArrayList<Report>();
		
		
		ReportDAO mockedReportDAO = mock(ReportDAO.class);
		DeleteReportAction deleteReportAction = new DeleteReportAction();
		deleteReportAction.setReportDAO(mockedReportDAO);
		when(mockedReportDAO.findAll()).thenReturn(reportList);
		
		String successString = deleteReportAction.deleteReport();
		
		assertEquals(successString, "success");
		
		//IndexOutOfBoundsException : 
		//assertEquals(deleteReportAction.getReportList().get(0).getName(), null);
	}
}
