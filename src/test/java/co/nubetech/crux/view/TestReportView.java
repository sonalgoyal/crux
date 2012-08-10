package co.nubetech.crux.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportType;

public class TestReportView {

	@Test
	public void testReportView(){
		
		Report report = new Report();
		report.setId(12);
		
		ReportType reportType = new ReportType(1,"String");
		
		report.setName("report1");
		report.setReportType(reportType);
		
		ReportView reportView = new ReportView(1,report);
		
		assertEquals(reportView.getName(),"report1");
		assertEquals(reportView.getReportType(),report.getReportType().getType());
		assertEquals(reportView.getIndex(),1);
		assertEquals(reportView.getId(),report.getId());
		assertEquals(reportView.getName(),report.getName());
		
	}
}
