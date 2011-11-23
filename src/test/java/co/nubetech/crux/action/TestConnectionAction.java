package co.nubetech.crux.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import co.nubetech.crux.dao.ConnectionDAO;
import co.nubetech.crux.dao.UserDAO;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.ConnectionProperty;
import co.nubetech.crux.model.Datastore;
import co.nubetech.crux.model.User;
import co.nubetech.crux.util.CruxConstants;
import co.nubetech.crux.view.ConnectionView;


public class TestConnectionAction {
	//Still to test - deleteConnection()
	//Still to test - saveConnection()
	//Still to test - updateConnection()
	
	@Test
	public void testInitializeConnections(){
		
		User user = new User();
		user.setId(12);
		user.setName("user");
		user.setPassword("password1");

		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
		
		//connection 1
		Connection connection1 = new Connection();
		ConnectionProperty connectionProperty1 = new ConnectionProperty(connection1, CruxConstants.HBASE_ZOOKEEPER_PROPERTY,"value1");
		ConnectionProperty connectionProperty2 = new ConnectionProperty(connection1, CruxConstants.HBASE_ZOOKEEPER_PROPERTY,"value2");
		
		connection1.addProperty(connectionProperty1);
		connection1.addProperty(connectionProperty2);
		connection1.setDatastore(datastore);
		connection1.setUser(user);
		connection1.setId(1);
		connection1.setName("ConnectionName1");				
				
		//connection 2
		Connection connection2 = new Connection();
		ConnectionProperty connectionProperty3 = new ConnectionProperty(connection2, CruxConstants.HBASE_ZOOKEEPER_PROPERTY, "value1");
		ConnectionProperty connectionProperty4 = new ConnectionProperty(connection2, CruxConstants.HBASE_ZOOKEEPER_PROPERTY, "value2");
		
		connection2.addProperty(connectionProperty3);
		connection2.addProperty(connectionProperty4);
		connection2.setDatastore(datastore);
		connection2.setUser(user);
		connection2.setId(2);
		connection2.setName("ConnectionName2");
		
		//connection 3
		Connection connection3 = new Connection();
		ConnectionProperty connectionProperty5 = new ConnectionProperty(connection3, CruxConstants.HBASE_ZOOKEEPER_PROPERTY, "value1");
		ConnectionProperty connectionProperty6 = new ConnectionProperty(connection3, CruxConstants.HBASE_ZOOKEEPER_PROPERTY, "value2");
		
		connection3.addProperty(connectionProperty5);
		connection3.addProperty(connectionProperty6);
		connection3.setDatastore(datastore);
		connection3.setUser(user);
		connection3.setId(3);
		connection3.setName("ConnectionName3");
		
		//List of above created connections
		List<Connection> connectionList = new ArrayList<Connection>();
		connectionList.add(connection1);
		connectionList.add(connection2);
		connectionList.add(connection3);
		
		//mock the dao
		ConnectionDAO mockedConnectionDAO = mock(ConnectionDAO.class);
		ConnectionAction connectionAction = new ConnectionAction();
		connectionAction.setConnectionDAO(mockedConnectionDAO);
		
		when(mockedConnectionDAO.findAll()).thenReturn(connectionList);
		String successString = connectionAction.initializeConnections();
		
		int totalConnections = connectionList.size();
		
		// Comparing connections using connection list passed and connectionViewList produced in initialize connection method,
		// using connection properties available in the ConnectionView class.
		
		assertEquals(totalConnections, 3); 
		
		assertEquals(successString, "success");
		
		//Testing connection 1
		assertEquals(connectionList.get(0).getName(), connectionAction.getConnectionViewList().get(0).getName());
		assertEquals(connectionList.get(0).getId(), connectionAction.getConnectionViewList().get(0).getId());
		assertEquals(connectionList.get(0).getDatastore().getName(), connectionAction.getConnectionViewList().get(0).getDatastoreName());
		//Testing connection 2
		assertEquals(connectionList.get(1).getName(), connectionAction.getConnectionViewList().get(1).getName());
		assertEquals(connectionList.get(1).getId(), connectionAction.getConnectionViewList().get(1).getId());
		assertEquals(connectionList.get(1).getDatastore().getName(), connectionAction.getConnectionViewList().get(1).getDatastoreName());
		//Testing connection 3
		assertEquals(connectionList.get(2).getName(), connectionAction.getConnectionViewList().get(2).getName());
		assertEquals(connectionList.get(2).getId(), connectionAction.getConnectionViewList().get(2).getId());
		assertEquals(connectionList.get(2).getDatastore().getName(), connectionAction.getConnectionViewList().get(2).getDatastoreName());
			
		// View index starts from integer 1
		assertEquals(connectionAction.getConnectionViewList().get(0).getIndex(), 1);
	}
		
	
	@Test 
	public void testInitializeConnectionsWithEmptyList(){		
		
		//no connections in this list
		List<Connection> connectionList = new ArrayList<Connection>();
		
		//mock the dao to return the list of connections
		ConnectionDAO mockedConnectionDAO = mock(ConnectionDAO.class);
		ConnectionAction connectionAction = new ConnectionAction();
		connectionAction.setConnectionDAO(mockedConnectionDAO);
		when(mockedConnectionDAO.findAll()).thenReturn(connectionList);
		
		//initialize connections
		connectionAction.initializeConnections();
		
		//Testing size
		assertTrue(connectionAction.getConnectionViewList().size() == 0);
		
	}
	
	@Test(expected= IndexOutOfBoundsException.class)
	public void testInitializeConnectionsWithEmptyListGetValue(){		
		
		//no connections in this list
		List<Connection> connectionList = new ArrayList<Connection>();
		
		//mock the dao to return the list of connections
		ConnectionDAO mockedConnectionDAO = mock(ConnectionDAO.class);
		ConnectionAction connectionAction = new ConnectionAction();
		connectionAction.setConnectionDAO(mockedConnectionDAO);
		when(mockedConnectionDAO.findAll()).thenReturn(connectionList);
		
		//initialise connections
		String successString = connectionAction.initializeConnections();
		
		assertEquals(successString, "success");
		
		//We expect IndexOutOfBoundsException here
		assertEquals(connectionList.get(0).getName(), connectionAction.getConnectionViewList().get(0).getName());
				
	}
	
	@Test
	public void testInitializeConnectionsAndParametersForConnectionView(){
		
		User user = new User();
		user.setId(12);
		user.setName("user");
		user.setPassword("password1");

		Datastore datastore = new Datastore();
		datastore.setId(121212);
		
		// We are hiding the datastore name
		//datastore.setName("Hbase");
		
		//connection object
		Connection connection = new Connection();
		connection.setDatastore(datastore);
		
		// We are hiding the connection id.
		//connection.setId(8001);
		
		connection.setName("ConnectionName");
		ConnectionProperty connectionProperty = new ConnectionProperty(connection, CruxConstants.HBASE_ZOOKEEPER_PROPERTY,"SomeValue");
		
		connection.addProperty(connectionProperty);
		//List of above created connections
		List<Connection> connectionList = new ArrayList<Connection>();
		connectionList.add(connection);
		//mock the dao
		ConnectionDAO mockedConnectionDAO = mock(ConnectionDAO.class);
		ConnectionAction connectionAction = new ConnectionAction();
		connectionAction.setConnectionDAO(mockedConnectionDAO);
		
		when(mockedConnectionDAO.findAll()).thenReturn(connectionList);
		String successString = connectionAction.initializeConnections();
		
		assertEquals(successString, "success");
				
		//Connection's datastore name parameter when not set has value null.
		assertEquals(connectionAction.getConnectionViewList().get(0).getDatastoreName(), null);

		//Connection's id parameter, when not set has value 0.
		assertEquals(connectionAction.getConnectionViewList().get(0).getId(), 0);
		

	}
	

	@Test(expected= NullPointerException.class)
	public void testInitializeConnectionsWithoutConnectionProperty(){
		
		User user = new User();
		user.setId(12);
		user.setName("user");
		user.setPassword("password1");

		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
		
		//connection object
		Connection connection = new Connection();
		connection.setDatastore(datastore);
		connection.setId(800180018);
		connection.setName("ConnectionName");
		
		// This will not be used.
		ConnectionProperty connectionProperty = new ConnectionProperty(connection, CruxConstants.HBASE_ZOOKEEPER_PROPERTY,"SomeValue");
		// We are not setting the ConnectionProperty object into Connection's properties.
		//connection.addProperty(connectionProperty);
		
		//List of above created connection
		List<Connection> connectionList = new ArrayList<Connection>();
		connectionList.add(connection);
		//mock the dao
		ConnectionDAO mockedConnectionDAO = mock(ConnectionDAO.class);
		ConnectionAction connectionAction = new ConnectionAction();
		connectionAction.setConnectionDAO(mockedConnectionDAO);
		when(mockedConnectionDAO.findAll()).thenReturn(connectionList);
		String successString = connectionAction.initializeConnections();
		
		assertEquals(successString, "success");
		
		// connection.getProperties().get(CruxConstants.HBASE_ZOOKEEPER_PROPERTY).getValue() in the ConnectionView() cannot work,
		// because the ConnectionProperty is not set for Connection object.
		// So, getConnectionViewList() does not work. We expect NullPointerException.
		
		assertEquals(connectionAction.getConnectionViewList().get(0).getName(), connection.getName());
		
	}
	
}