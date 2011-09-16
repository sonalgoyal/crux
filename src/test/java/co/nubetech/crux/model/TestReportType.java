package co.nubetech.crux.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestReportType {

	@Test
	public void testHashCode() {

		ReportType reportType = new ReportType();
		reportType.setId(1);
		reportType.setType("type");
		
		ReportType reportType1 = new ReportType();
		reportType1.setId(1);
		reportType1.setType("type");

		assertTrue(reportType.hashCode()==reportType1.hashCode());
	}
	
	@Test
	public void testHashCodeForNull() {

		ReportType reportType = new ReportType();
		
		ReportType reportType1 = new ReportType();

		assertTrue(reportType.hashCode()==reportType1.hashCode());
	}
	
	@Test
	public void testEquals() {

		ReportType reportType = new ReportType();
		reportType.setId(1);
		reportType.setType("type");
		
		ReportType reportType1 = new ReportType();
		reportType1.setId(1);
		reportType1.setType("type");

		assertTrue(reportType.equals(reportType1));
	}
	
	@Test
	public void testEqualsForNull() {

		ReportType reportType = new ReportType();
		reportType.setId(1);
		reportType.setType(null);
		
		ReportType reportType1 = new ReportType();
		reportType1.setId(1);
		reportType1.setType(null);

		assertTrue(reportType.equals(reportType1));
	}
}
