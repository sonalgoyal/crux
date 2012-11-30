package co.nubetech.crux.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.junit.Test;

import co.nubetech.crux.server.functions.CruxFunction;
import co.nubetech.crux.util.CruxException;


public class TestReport {
	private final static Logger logger = Logger.getLogger(TestReport.class);
	@Test
	public void testEquals() {
		User user = new User();
		ReportType reportType = new ReportType();

		Report report = new Report();
		report.setId(0);
		report.setName("Report");
		report.setReportType(reportType);
		report.setUser(user);
		report.setNumRecordsPerPage(25l);

		Report report1 = new Report();
		report1.setId(0);
		report1.setName("Report");
		report1.setReportType(reportType);
		report1.setUser(user);
		report1.setNumRecordsPerPage(25l);

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
		report.setNumRecordsPerPage(0);

		Report report1 = new Report();
		report1.setId(0);
		report1.setName(null);
		report1.setReportType(reportType);
		report1.setUser(user);
		report1.setNumRecordsPerPage(0);

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
		report.setNumRecordsPerPage(24);

		Report report1 = new Report();
		report1.setId(0);
		report1.setName(null);
		report1.setReportType(reportType);
		report1.setUser(user);
		report1.setNumRecordsPerPage(25);

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
		report.setNumRecordsPerPage(25);
		
		Report report1 = new Report();
		report1.setId(0);
		report1.setName("Report");
		report1.setReportType(reportType);
		report1.setUser(user);
		report1.setNumRecordsPerPage(25);

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
		report.setNumRecordsPerPage(0);

		Report report1 = new Report();
		report1.setId(0);
		report1.setName(null);
		report1.setReportType(reportType);
		report1.setUser(user);
		report1.setNumRecordsPerPage(0);

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
	
	@Test
	public void testGetAggregators() throws CruxException{
		Report report = TestingUtil.getReport(); 
		List<Stack<CruxFunction>> functionList = report.getFunctions();
		assertEquals(3, functionList.size());
		Stack<CruxFunction> xFnStack = functionList.get(0);
		assertEquals(2,xFnStack.size());
		assertEquals(co.nubetech.crux.server.functions.SumDoubleAggregator.class, xFnStack.pop().getClass());
		assertEquals(co.nubetech.crux.server.functions.Ceil.class, xFnStack.pop().getClass());
		Stack<CruxFunction> yFnStack = functionList.get(1);
		assertEquals(1, yFnStack.size());
		assertEquals(co.nubetech.crux.server.functions.AverageAggregator.class, yFnStack.pop().getClass());
		Stack<CruxFunction> yFnStack1 = functionList.get(2);
		assertEquals(1, yFnStack1.size());		
	}
	
	@Test
	public void testGetAggregatorsNoFunctions() throws CruxException{
		Report report = TestingUtil.getReport(); 
		for (ReportDesign design: report.getDesigns()) {
			design.setReportDesignFunctionList(null);
		}
		List<Stack<CruxFunction>> functionList = report.getFunctions();
		assertEquals(3, functionList.size());
		Stack<CruxFunction> xFnStack = functionList.get(0);
		assertEquals(0,xFnStack.size());
		Stack<CruxFunction> yFnStack = functionList.get(1);
		assertEquals(0, yFnStack.size());
		Stack<CruxFunction> yFnStack1 = functionList.get(2);
		assertEquals(0, yFnStack1.size());		
	}
	
	@Test
	public void testIsAggregateTrue() throws CruxException{
		Report report = TestingUtil.getReport();
		assertTrue(report.isAggregateReport());
	}
	
	@Test
	public void testIsAggregateFalse() throws CruxException{
		Report report = TestingUtil.getReportWithoutAggregateFunctions();
		assertFalse(report.isAggregateReport());
	}
	
	
}
