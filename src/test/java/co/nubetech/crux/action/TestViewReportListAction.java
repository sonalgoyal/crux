package co.nubetech.crux.action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.junit.Test;

import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.dao.ReportDAO;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.ConnectionProperty;
import co.nubetech.crux.model.Dashboard;
import co.nubetech.crux.model.Datastore;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportType;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.User;
import co.nubetech.crux.util.CruxConstants;

public class TestViewReportListAction {
	@Test
	public void testDisplayReportWithCompleteFields(){
		
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
		ViewReportListAction viewReportListAction = new ViewReportListAction();
		viewReportListAction.setReportDAO(mockedReportDAO);
		when(mockedReportDAO.findAll()).thenReturn(reportList);
		
		String successString = viewReportListAction.displayReportList();
		
		assertEquals(successString, "success");
				
		assertEquals(viewReportListAction.getReportList().get(0).getName(), report1.getName());
		assertEquals(viewReportListAction.getReportList().get(1).getName(), report2.getName());
		assertEquals(viewReportListAction.getReportList().get(2).getName(), report3.getName());
		
		// Report view index starts with integer 1.
		assertEquals(viewReportListAction.getReportList().get(0).getIndex(), 1);
			
	}

	@Test
	public void testDisplayReportListWithEmptyReportFields(){
		
		ReportType reportType = new ReportType(121, "ReportType");
		Report report = new Report();
		
		// Setting report type is must, otherwise NUllPointerException.
		report.setReportType(reportType);
		
		List<Report> reportList = new ArrayList<Report>();
		reportList.add(report);
		
		ReportDAO mockedReportDAO = mock(ReportDAO.class);
		ViewReportListAction viewReportListAction = new ViewReportListAction();
		viewReportListAction.setReportDAO(mockedReportDAO);
		when(mockedReportDAO.findAll()).thenReturn(reportList);
		
		String successString = viewReportListAction.displayReportList();
		
		assertEquals(successString, "success");
		
		//ReporType is the only assigned field to report..
		assertEquals(viewReportListAction.getReportList().get(0).getReportType(), report.getReportType().getType());
		
		// Other unassigned fields return their respective default values. 
		assertEquals(viewReportListAction.getReportList().get(0).getName(), null);
		assertEquals(viewReportListAction.getReportList().get(0).getId(), 0);
		assertEquals(viewReportListAction.getReportList().get(0).getIndex(), 1);
		
	}
	
	@Test(expected= NullPointerException.class)
	public void testDisplayReportListWithoutReportType(){
		
		Report report = new Report();
		
		// Setting report type is must, otherwise NullPointerException.
		// We are hiding the ReportType for report object
		
		
		List<Report> reportList = new ArrayList<Report>();
		reportList.add(report);
		
		ReportDAO mockedReportDAO = mock(ReportDAO.class);
		ViewReportListAction viewReportListAction = new ViewReportListAction();
		viewReportListAction.setReportDAO(mockedReportDAO);
		when(mockedReportDAO.findAll()).thenReturn(reportList);
		
		// with this, report.getReportType().getType() is called in ReportView(index, report)
		// which NullPointerException as there is no ReportType for report object.
		String successString = viewReportListAction.displayReportList();
		
		assertEquals(successString, "success");
		
		//ReporType is the only assigned field to report..
		//assertEquals(viewReportListAction.getReportList().get(0).getReportType(), null);
		
	}
	
	@Test
	public void testDisplayReportListWithNoReports(){

		// When there are no reports in reportList, the size of list is zero.
		// In this case  new ReportDesignAction().populateMappingList(mappingDAO,mappingList); is called..
		// .. by the displayReportList() method and "report" string is returned.
		
		//user
		User user = new User();
		user.setId(12);
		user.setName("user");
		user.setPassword("password1");

		//datastore
		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");

		//connection 
		Connection connection = new Connection();
		ConnectionProperty connectionProperty1 = new ConnectionProperty(connection, CruxConstants.HBASE_ZOOKEEPER_PROPERTY,"value1");
		ConnectionProperty connectionProperty2 = new ConnectionProperty(connection, CruxConstants.HBASE_ZOOKEEPER_PROPERTY,"value2");
		connection.addProperty(connectionProperty1);
		connection.addProperty(connectionProperty2);
		connection.setDatastore(datastore);
		connection.setUser(user);
		connection.setId(1);
		connection.setName("ConnectionName1");	
		
		Map<String, ColumnAlias> columnAliasMap = null;
		SortedMap<String, RowAlias> rowAliasMap = null;
		
		
		//mappings
		Mapping mapping1 = new Mapping(connection, "mappingParameterName1", "ParameterTableName1", columnAliasMap, rowAliasMap);
		Mapping mapping2 = new Mapping(connection, "mappingParameterName2", "ParameterTableName2", columnAliasMap, rowAliasMap);
		Mapping mapping3 = new Mapping(connection, "mappingParameterName3", "ParameterTableName3", columnAliasMap, rowAliasMap);
		
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping1);
		mappingList.add(mapping2);
		mappingList.add(mapping3);
		
		// Nothing in the report list in this test. 
		List<Report> reportList = new ArrayList<Report>();
		
		ReportDAO mockedReportDAO = mock(ReportDAO.class);
		ViewReportListAction viewReportListAction = new ViewReportListAction();
		viewReportListAction.setReportDAO(mockedReportDAO);
		
		when(mockedReportDAO.findAll()).thenReturn(reportList);
		
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		viewReportListAction.setMappingDAO(mockedMappingDAO);
		
		when(mockedMappingDAO.findAll()).thenReturn(mappingList);
		
	
		String successString = viewReportListAction.displayReportList();
		
		assertEquals(successString, "report");
		
		assertEquals(viewReportListAction.getMappingList().get(0), mapping1);
		assertEquals(viewReportListAction.getMappingList().get(1), mapping2);
		assertEquals(viewReportListAction.getMappingList().get(2), mapping3);
				
	}
}

