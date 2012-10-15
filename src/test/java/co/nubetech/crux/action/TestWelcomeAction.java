package co.nubetech.crux.action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import co.nubetech.crux.dao.ConnectionDAO;
import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.dao.ReportDAO;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.Dashboard;
import co.nubetech.crux.model.Datastore;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportType;
import co.nubetech.crux.model.User;

public class TestWelcomeAction {

	@Test
	public void testWelcomeDashboardExists(){
		User user = new User();
		user.setId(12);
		user.setName("user");
		user.setPassword("password1");

		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
		
		//connections
		Connection connection1 = new Connection();
		Connection connection2 = new Connection();
		Connection connection3 = new Connection();
				
		//List of above created connections
		List<Connection> connectionList = new ArrayList<Connection>();
		connectionList.add(connection1);
		connectionList.add(connection2);
		connectionList.add(connection3);
		
		//mock the dao
		ConnectionDAO mockedConnectionDAO = mock(ConnectionDAO.class);
		WelcomeAction welcomeAction = new WelcomeAction();
		welcomeAction.setConnectionDAO(mockedConnectionDAO);
		when(mockedConnectionDAO.findAll()).thenReturn(connectionList);
		
			
		Mapping mapping1 = new Mapping();
		Mapping mapping2 = new Mapping();
		Mapping mapping3 = new Mapping();
		
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping1);
		mappingList.add(mapping2);
		mappingList.add(mapping3);
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		welcomeAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findAll()).thenReturn(mappingList);
		
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
		welcomeAction.setReportDAO(mockedReportDAO);
		when(mockedReportDAO.findAll()).thenReturn(reportList);
		
		assertEquals(welcomeAction.welcome(), "dashboard");
	}
	
	@Test
	public void testWelcomeNoDashboard(){
		User user = new User();
		user.setId(12);
		user.setName("user");
		user.setPassword("password1");

		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
		
		//connections
		Connection connection1 = new Connection();
		Connection connection2 = new Connection();
		Connection connection3 = new Connection();
				
		//List of above created connections
		List<Connection> connectionList = new ArrayList<Connection>();
		connectionList.add(connection1);
		connectionList.add(connection2);
		connectionList.add(connection3);
		
		//mock the dao
		ConnectionDAO mockedConnectionDAO = mock(ConnectionDAO.class);
		WelcomeAction welcomeAction = new WelcomeAction();
		welcomeAction.setConnectionDAO(mockedConnectionDAO);
		when(mockedConnectionDAO.findAll()).thenReturn(connectionList);
		
			
		Mapping mapping1 = new Mapping();
		Mapping mapping2 = new Mapping();
		Mapping mapping3 = new Mapping();
		
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping1);
		mappingList.add(mapping2);
		mappingList.add(mapping3);
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		welcomeAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findAll()).thenReturn(mappingList);
		
		//No dashboard with reports.
		
		Report report1 = new Report();
		Report report2 = new Report();
		Report report3 = new Report();
		
		List<Report> reportList = new ArrayList<Report>();
		reportList.add(report1);
		reportList.add(report2);
		reportList.add(report3);
				
		ReportDAO mockedReportDAO = mock(ReportDAO.class);
		welcomeAction.setReportDAO(mockedReportDAO);
		when(mockedReportDAO.findAll()).thenReturn(reportList);
		
		assertEquals(welcomeAction.welcome(), "report");
	}
	
	@Test
	public void testWelcomeWithEmptyReportsList(){
		User user = new User();
		user.setId(12);
		user.setName("user");
		user.setPassword("password1");

		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
		
		//connections
		Connection connection1 = new Connection();
		Connection connection2 = new Connection();
		Connection connection3 = new Connection();
				
		//List of above created connections
		List<Connection> connectionList = new ArrayList<Connection>();
		connectionList.add(connection1);
		connectionList.add(connection2);
		connectionList.add(connection3);
		
		//mock the dao
		ConnectionDAO mockedConnectionDAO = mock(ConnectionDAO.class);
		WelcomeAction welcomeAction = new WelcomeAction();
		welcomeAction.setConnectionDAO(mockedConnectionDAO);
		when(mockedConnectionDAO.findAll()).thenReturn(connectionList);
		
			
		Mapping mapping1 = new Mapping();
		Mapping mapping2 = new Mapping();
		Mapping mapping3 = new Mapping();
		
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping1);
		mappingList.add(mapping2);
		mappingList.add(mapping3);
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		welcomeAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findAll()).thenReturn(mappingList);
		
		List<Report> reportList = new ArrayList<Report>();
			
		ReportDAO mockedReportDAO = mock(ReportDAO.class);
		welcomeAction.setReportDAO(mockedReportDAO);
		when(mockedReportDAO.findAll()).thenReturn(reportList);
		
		assertEquals(welcomeAction.welcome(), "design");
	}
	
	@Test
	public void testWelcomeWithEmptyMappingList(){
		
		//connections
		Connection connection1 = new Connection();
		Connection connection2 = new Connection();
		Connection connection3 = new Connection();
				
		//List of above created connections
		List<Connection> connectionList = new ArrayList<Connection>();
		connectionList.add(connection1);
		connectionList.add(connection2);
		connectionList.add(connection3);
				
		//mock the dao
		ConnectionDAO mockedConnectionDAO = mock(ConnectionDAO.class);
		WelcomeAction welcomeAction = new WelcomeAction();
		welcomeAction.setConnectionDAO(mockedConnectionDAO);
		when(mockedConnectionDAO.findAll()).thenReturn(connectionList);
				
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		welcomeAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findAll()).thenReturn(mappingList);
		
		List<Report> reportList = new ArrayList<Report>();
			
		ReportDAO mockedReportDAO = mock(ReportDAO.class);
		welcomeAction.setReportDAO(mockedReportDAO);
		when(mockedReportDAO.findAll()).thenReturn(reportList);
		
		assertEquals(welcomeAction.welcome(), "mapping");
	}
	
	@Test
	public void testWelcomeWithEmptyMappingListEmptyConnectionList(){
				
		//List of above created connections
		List<Connection> connectionList = new ArrayList<Connection>();
		
		ConnectionDAO mockedConnectionDAO = mock(ConnectionDAO.class);
		WelcomeAction welcomeAction = new WelcomeAction();
		welcomeAction.setConnectionDAO(mockedConnectionDAO);
		when(mockedConnectionDAO.findAll()).thenReturn(connectionList);
				
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		welcomeAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findAll()).thenReturn(mappingList);
		
		List<Report> reportList = new ArrayList<Report>();
			
		ReportDAO mockedReportDAO = mock(ReportDAO.class);
		welcomeAction.setReportDAO(mockedReportDAO);
		when(mockedReportDAO.findAll()).thenReturn(reportList);
		
		assertEquals(welcomeAction.welcome(), "connection");
	}
}
