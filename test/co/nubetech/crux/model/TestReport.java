package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestReport {
	@Test
	public void testEquals() {
		User user = new User();
		ReportType reportType = new ReportType();

		Report report = new Report();
		report.setId(0);
		report.setName("Report");
		report.setReportType(reportType);
		report.setUser(user);

		Report report1 = new Report();
		report1.setId(0);
		report1.setName("Report");
		report1.setReportType(reportType);
		report1.setUser(user);

		assertTrue(report.equals(report1));
	}
	
	@Test
	public void testEqualsForValueNull() {
		User user = new User();
		ReportType reportType = new ReportType();

		Report report = new Report();
		report.setId(0);
		report.setName(null);
		report.setReportType(reportType);
		report.setUser(user);

		Report report1 = new Report();
		report1.setId(0);
		report1.setName(null);
		report1.setReportType(reportType);
		report1.setUser(user);

		assertTrue(report.equals(report1));
	}
	
	@Test
	public void testForNotEquals() {
		User user = new User();
		ReportType reportType = new ReportType();

		Report report = new Report();
		report.setId(0);
		report.setName("Report");
		report.setReportType(reportType);
		report.setUser(user);

		Report report1 = new Report();
		report1.setId(0);
		report1.setName(null);
		report1.setReportType(reportType);
		report1.setUser(user);

		assertTrue(!report.equals(report1));
	}
	
	@Test
	public void testHashCode() {
		User user = new User();
		ReportType reportType = new ReportType();

		Report report = new Report();
		report.setId(0);
		report.setName("Report");
		report.setReportType(reportType);
		report.setUser(user);

		Report report1 = new Report();
		report1.setId(0);
		report1.setName("Report");
		report1.setReportType(reportType);
		report1.setUser(user);

		assertTrue((report.hashCode()==report1.hashCode()));
	}
	
	@Test
	public void testHashCodeForNullValue() {
		User user = new User();
		ReportType reportType = new ReportType();

		Report report = new Report();
		report.setId(0);
		report.setName(null);
		report.setReportType(reportType);
		report.setUser(user);

		Report report1 = new Report();
		report1.setId(0);
		report1.setName(null);
		report1.setReportType(reportType);
		report1.setUser(user);

		assertTrue((report.hashCode()==report1.hashCode()));
	}
	
	public void testIsDashboard(){
		Report report1 = new Report();
		report1.setId(0);
		report1.setName("First Report");
		report1.setDashboard(null);
		
		Report report = new Report();
		report.setId(0);
		report.setName(null);
		report.setDashboard(new Dashboard());
		
		assertTrue(!report1.onDashboard());
		assertTrue(report.onDashboard());
	}
}
