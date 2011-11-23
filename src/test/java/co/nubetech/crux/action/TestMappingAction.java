package co.nubetech.crux.action;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import co.nubetech.crux.dao.ConnectionDAO;
import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.dao.ValueTypeDAO;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.ConnectionProperty;
import co.nubetech.crux.model.Datastore;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.User;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.util.CruxConstants;
import co.nubetech.crux.util.CruxException;
import co.nubetech.crux.view.MappingView;
import static org.mockito.Mockito.*;

public class TestMappingAction {
	@Test
	public void testInitializeMappings(){
		
		//Code for creating list<Connection> & mock for it.
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
		
		//create list of the above created connections
		List<Connection> connectionList = new ArrayList<Connection>();
		connectionList.add(connection1);
		connectionList.add(connection2);
		connectionList.add(connection3);
		
		MappingAction mappingAction = new MappingAction();
		
		//mock the dao to return the list of connections
		ConnectionDAO mockedConnectionDAO = mock(ConnectionDAO.class);
		mappingAction.setConnectionDAO(mockedConnectionDAO);
		when(mockedConnectionDAO.findAll()).thenReturn(connectionList);
				
		//Code for creating List<ValueType> & mock for it.
		ValueType valueType1 = new ValueType(101, datastore, "valueType1", "className1", true);
		ValueType valueType2 = new ValueType(102, datastore, "valueType2", "className2", true);
		ValueType valueType3 = new ValueType(103, datastore, "valueType3", "className3", true);
		
		List<ValueType> valueTypeList = new ArrayList<ValueType>();
		valueTypeList.add(valueType1);
		valueTypeList.add(valueType2);
		valueTypeList.add(valueType3);
		
		ValueTypeDAO mockedValueTypeDAO = mock(ValueTypeDAO.class);
		mappingAction.setValueTypeDAO(mockedValueTypeDAO);
		when(mockedValueTypeDAO.findAll()).thenReturn(valueTypeList);
		
		//Code for creating List<Mapping> & mock for it.
		Mapping mapping = new Mapping();
		ColumnAlias columnAlias1 = new ColumnAlias(mapping, valueType1,"columnFamily", "qualifier", "alias");
		ColumnAlias columnAlias2 = new ColumnAlias(mapping, valueType1,"columnFamily", "qualifier", "alias");
		ColumnAlias columnAlias3= new ColumnAlias(mapping, valueType1,"columnFamily", "qualifier", "alias");
				
		Map<String, ColumnAlias> columnAliasMap = new HashMap<String, ColumnAlias>();
		columnAliasMap.put("columnAlias1", columnAlias1);
		columnAliasMap.put("columnAlias2", columnAlias2);
		columnAliasMap.put("columnAlias3", columnAlias3);
		
		RowAlias rowAlias1 = new RowAlias(901, mapping, valueType1, 4 ,"rowAliasName1");
		RowAlias rowAlias2 = new RowAlias(902, mapping, valueType1, 4 ,"rowAliasName2");
		RowAlias rowAlias3 = new RowAlias(903, mapping, valueType1, 4 ,"rowAliasName3");
		
		Map<String, RowAlias> rowAliasMap = new HashMap<String, RowAlias>();
		rowAliasMap.put("rowAlias1", rowAlias1);
		rowAliasMap.put("rowAlias2", rowAlias2);
		rowAliasMap.put("rowAlias3", rowAlias3);
		
				
		Mapping mapping1 = new Mapping(connection1, "mappingParameterName1", "ParameterTableName1", columnAliasMap, rowAliasMap);
		Mapping mapping2 = new Mapping(connection2, "mappingParameterName2", "ParameterTableName2", columnAliasMap, rowAliasMap);
		Mapping mapping3 = new Mapping(connection3, "mappingParameterName3", "ParameterTableName3", columnAliasMap, rowAliasMap);
		
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping1);
		mappingList.add(mapping2);
		mappingList.add(mapping3);
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		mappingAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findAll()).thenReturn(mappingList);
		
		String successString = mappingAction.initializeMappings();
		
		assertEquals(successString, "success");
		
		//Test for same connections
		assertEquals(mappingAction.getConnections().get(0), connection1);
		assertEquals(mappingAction.getConnections().get(1), connection2);
		assertEquals(mappingAction.getConnections().get(2), connection3);
		
		//Test for same valueTypes - columnTypeList in the MappingAction class
		assertEquals(mappingAction.getColumnTypeList().get(0), valueType1);
		assertEquals(mappingAction.getColumnTypeList().get(1), valueType2);
		assertEquals(mappingAction.getColumnTypeList().get(2), valueType3);
		
		//Test for same mappings
		//test mapping1
		assertEquals(mappingAction.getMappingViewList().get(0).getId(), mapping1.getId());
		assertEquals(mappingAction.getMappingViewList().get(0).getName(), mapping1.getName());
		assertEquals(mappingAction.getMappingViewList().get(0).getConnectionName(), mapping1.getConnection().getName());
		assertEquals(mappingAction.getMappingViewList().get(0).getTableName(), mapping1.getTableName());
		//test mapping2
		assertEquals(mappingAction.getMappingViewList().get(1).getId(), mapping2.getId());
		assertEquals(mappingAction.getMappingViewList().get(1).getName(), mapping2.getName());
		assertEquals(mappingAction.getMappingViewList().get(1).getConnectionName(), mapping2.getConnection().getName());
		assertEquals(mappingAction.getMappingViewList().get(1).getTableName(), mapping2.getTableName());
		//test mapping3
		assertEquals(mappingAction.getMappingViewList().get(2).getId(), mapping3.getId());
		assertEquals(mappingAction.getMappingViewList().get(2).getName(), mapping3.getName());
		assertEquals(mappingAction.getMappingViewList().get(2).getConnectionName(), mapping3.getConnection().getName());
		assertEquals(mappingAction.getMappingViewList().get(2).getTableName(), mapping3.getTableName());
				
	}
	
	@Test
	public void testInitializeMappingsWithEmptyLists(){
		
		MappingAction mappingAction = new MappingAction();
		
		List<Connection> connectionList = new ArrayList<Connection>();
				
		ConnectionDAO mockedConnectionDAO = mock(ConnectionDAO.class);
		mappingAction.setConnectionDAO(mockedConnectionDAO);
		when(mockedConnectionDAO.findAll()).thenReturn(connectionList);
		
		List<ValueType> valueTypeList = new ArrayList<ValueType>();
		
		ValueTypeDAO mockedValueTypeDAO = mock(ValueTypeDAO.class);
		mappingAction.setValueTypeDAO(mockedValueTypeDAO);
		when(mockedValueTypeDAO.findAll()).thenReturn(valueTypeList);
		
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		mappingAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findAll()).thenReturn(mappingList);
		
		String successString = mappingAction.initializeMappings();
		
		//Returns SUCCESS always
		assertEquals(successString, "success");
		
		//valueTypeList passed by us was empty
		assertTrue(mappingAction.getColumnTypeList().size() == 0);
		
		assertTrue(mappingAction.getConnections().size() == 0);
		
		assertTrue(mappingAction.getMappingViewList().size() == 0);
						
	}
	
	@Test
	public void testPopulateMappingViewList(){
		
		//Code for creating list<Connection> & mock for it.
		User user = new User();
		user.setId(12);
		user.setName("user");
		user.setPassword("password1");

		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
		
		//connection
		Connection connection = new Connection();
		ConnectionProperty connectionProperty = new ConnectionProperty(connection, CruxConstants.HBASE_ZOOKEEPER_PROPERTY,"value1");
		
		connection.addProperty(connectionProperty);
		connection.setDatastore(datastore);
		connection.setUser(user);
		connection.setId(1);
		connection.setName("ConnectionName1");				
		
		
		//Code for creating List<Mapping> & mock for it.
		ValueType valueType = new ValueType(101, datastore, "valueType1", "className1", true);
	
		
		//Code for creating List<Mapping> & mock for it.
		Mapping mapping = new Mapping();
		ColumnAlias columnAlias1 = new ColumnAlias(mapping, valueType,"columnFamily", "qualifier", "alias");
		ColumnAlias columnAlias2 = new ColumnAlias(mapping, valueType,"columnFamily", "qualifier", "alias");
		ColumnAlias columnAlias3= new ColumnAlias(mapping, valueType,"columnFamily", "qualifier", "alias");
				
		Map<String, ColumnAlias> columnAliasMap = new HashMap<String, ColumnAlias>();
		columnAliasMap.put("columnAlias1", columnAlias1);
		columnAliasMap.put("columnAlias2", columnAlias2);
		columnAliasMap.put("columnAlias3", columnAlias3);
		
		RowAlias rowAlias1 = new RowAlias(901, mapping, valueType, 4 ,"rowAliasName1");
		RowAlias rowAlias2 = new RowAlias(902, mapping, valueType, 4 ,"rowAliasName2");
		RowAlias rowAlias3 = new RowAlias(903, mapping, valueType, 4 ,"rowAliasName3");
		
		Map<String, RowAlias> rowAliasMap = new HashMap<String, RowAlias>();
		rowAliasMap.put("rowAlias1", rowAlias1);
		rowAliasMap.put("rowAlias2", rowAlias2);
		rowAliasMap.put("rowAlias3", rowAlias3);
				
		Mapping mapping1 = new Mapping(connection, "mappingParameterName1", "ParameterTableName1", columnAliasMap, rowAliasMap);
		Mapping mapping2 = new Mapping(connection, "mappingParameterName2", "ParameterTableName2", columnAliasMap, rowAliasMap);
		Mapping mapping3 = new Mapping(connection, "mappingParameterName3", "ParameterTableName3", columnAliasMap, rowAliasMap);
		
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping1);
		mappingList.add(mapping2);
		mappingList.add(mapping3);
		
		MappingAction mappingAction = new MappingAction();
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		mappingAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findAll()).thenReturn(mappingList);
		
		// populateMappingViewList() returns void.
		mappingAction.populateMappingViewList();
				
		//Test for same mappings
		//test mapping1
		assertEquals(mappingAction.getMappingViewList().get(0).getId(), mapping1.getId());
		assertEquals(mappingAction.getMappingViewList().get(0).getName(), mapping1.getName());
		assertEquals(mappingAction.getMappingViewList().get(0).getConnectionName(), mapping1.getConnection().getName());
		assertEquals(mappingAction.getMappingViewList().get(0).getTableName(), mapping1.getTableName());
		//test mapping2
		assertEquals(mappingAction.getMappingViewList().get(1).getId(), mapping2.getId());
		assertEquals(mappingAction.getMappingViewList().get(1).getName(), mapping2.getName());
		assertEquals(mappingAction.getMappingViewList().get(1).getConnectionName(), mapping2.getConnection().getName());
		assertEquals(mappingAction.getMappingViewList().get(1).getTableName(), mapping2.getTableName());
		//test mapping3
		assertEquals(mappingAction.getMappingViewList().get(2).getId(), mapping3.getId());
		assertEquals(mappingAction.getMappingViewList().get(2).getName(), mapping3.getName());
		assertEquals(mappingAction.getMappingViewList().get(2).getConnectionName(), mapping3.getConnection().getName());
		assertEquals(mappingAction.getMappingViewList().get(2).getTableName(), mapping3.getTableName());
				
	}
	
	@Test
	public void testPopulateMappingViewListWithEmptyList(){
				
		//Empty List
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
				
		MappingAction mappingAction = new MappingAction();
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		mappingAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findAll()).thenReturn(mappingList);
		
		// populateMappingViewList() returns void.
		mappingAction.populateMappingViewList();
				
		assertTrue(mappingAction.getMappingViewList().size() == 0);
		
	}
	
	@Test
	public void tesPopulateColumnAliasViewList(){
		
		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
		
		ValueType valueType = new ValueType(101, datastore, "valueTypeName", "className", true);
				
		Mapping mapping = new Mapping();
		
		ColumnAlias columnAlias = new ColumnAlias(mapping, valueType,"columnFamily", "qualifier", "aliasName");
				
		Map<String, ColumnAlias> columnAliasMap = new HashMap<String, ColumnAlias>();
		columnAliasMap.put("columnAlias", columnAlias);
				
		mapping.setColumnAlias(columnAliasMap);
		
		MappingAction mappingAction = new MappingAction();
		
		// Now call the populate method. Returns void.
		mappingAction.populateColumnAliasViewList(mapping);
		
		//get columnAlias from getColumnAliasViewList() while asserting.		
		//Using getColumnAliasViewList() we can test following properties to check for equality :
		
		//Test for same column family
		assertEquals(mappingAction.getColumnAliasViewList().get(0).getColumnFamily(), columnAlias.getColumnFamily());
				
		//Test for same column type
		assertEquals(mappingAction.getColumnAliasViewList().get(0).getColumnTypeName(), columnAlias.getValueType().getName());
		// Above test directly using valueType object.		
		assertEquals(mappingAction.getColumnAliasViewList().get(0).getColumnTypeName(),valueType.getName());
		
		// test column alias names
		assertEquals(mappingAction.getColumnAliasViewList().get(0).getAlias(), columnAlias.getAlias());
		
		// test column alias id
		assertEquals(mappingAction.getColumnAliasViewList().get(0).getId(), columnAlias.getId());
		
		// test qualifier name
		assertEquals(mappingAction.getColumnAliasViewList().get(0).getQualifier(), columnAlias.getQualifier());
		
				
	}
	
	@Test(expected= NullPointerException.class)
	public void tesPopulateColumnAliasViewListWithEmptyColumnAlias(){
		
		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
				
		Mapping mapping = new Mapping();
		
		ColumnAlias columnAlias = new ColumnAlias();
				
		Map<String, ColumnAlias> columnAliasMap = new HashMap<String, ColumnAlias>();
		columnAliasMap.put("columnAlias", columnAlias);
				
		mapping.setColumnAlias(columnAliasMap);
		
		MappingAction mappingAction = new MappingAction();
		
		// Now call the populate method. Returns void.
		mappingAction.populateColumnAliasViewList(mapping);
		
		
		assertEquals(mappingAction.getColumnAliasViewList().get(0).getColumnFamily(), columnAlias.getColumnFamily());
		
				
	}
	
	@Test(expected= IndexOutOfBoundsException.class)
	public void testPopulateColumnAliasViewListWithEmptyList(){
		
		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
		
		ValueType valueType = new ValueType(101, datastore, "valueTypeName", "className", true);
				
		Mapping mapping = new Mapping();
		
		ColumnAlias columnAlias = new ColumnAlias(mapping, valueType,"columnFamily", "qualifier", "aliasName");
				
		Map<String, ColumnAlias> columnAliasMap = new HashMap<String, ColumnAlias>();
		//columnAliasMap.put("columnAlias", columnAlias);
				
		mapping.setColumnAlias(columnAliasMap);
		
		MappingAction mappingAction = new MappingAction();
		
		// Now call the populate method. Returns void.
		mappingAction.populateColumnAliasViewList(mapping);
		
		// columnAliasMap is empty. IndexOutOfBoundsException. 
		assertEquals(mappingAction.getColumnAliasViewList().get(0).getId(), columnAlias.getId());
		
				
	}
	
	@Test
	public void TestPopulateRowAliasViewList(){
		
		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
		
		ValueType valueType = new ValueType(101, datastore, "valueType", "className1", true);
		
		Mapping mapping = new Mapping();
				
		RowAlias rowAlias1 = new RowAlias(901, mapping, valueType, 4 ,"rowAliasName1");
		RowAlias rowAlias2 = new RowAlias(902, mapping, valueType, 4 ,"rowAliasName2");
		RowAlias rowAlias3 = new RowAlias(903, mapping, valueType, 4 ,"rowAliasName3");
		
		Map<String, RowAlias> rowAliasMap = new TreeMap<String, RowAlias>();
		rowAliasMap.put("rowAlias1", rowAlias1);
		rowAliasMap.put("rowAlias2", rowAlias2);
		rowAliasMap.put("rowAlias3", rowAlias3);
		
		mapping.setRowAlias(rowAliasMap);
			
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping);
		MappingAction mappingAction = new MappingAction();
				
		// populateRowAliasViewList() returns void.
		mappingAction.populateRowAliasViewList(mapping);
		
		// Test aliases
		assertEquals(mappingAction.getRowAliasViewList().get(0).getAlias(), rowAlias1.getAlias());
		assertEquals(mappingAction.getRowAliasViewList().get(1).getAlias(), rowAlias2.getAlias());
		assertEquals(mappingAction.getRowAliasViewList().get(2).getAlias(), rowAlias3.getAlias());
		
		// Test each column type name
		assertEquals(mappingAction.getRowAliasViewList().get(0).getColumnTypeName(), rowAlias1.getValueType().getName());
		assertEquals(mappingAction.getRowAliasViewList().get(1).getColumnTypeName(), rowAlias2.getValueType().getName());
		assertEquals(mappingAction.getRowAliasViewList().get(2).getColumnTypeName(), rowAlias3.getValueType().getName());
		
		// test each id
		assertEquals(mappingAction.getRowAliasViewList().get(0).getId(), rowAlias1.getId());
		assertEquals(mappingAction.getRowAliasViewList().get(1).getId(), rowAlias2.getId());
		assertEquals(mappingAction.getRowAliasViewList().get(2).getId(), rowAlias3.getId());
		
		// test lenght of each rowAlias
		assertEquals(mappingAction.getRowAliasViewList().get(0).getLength(), rowAlias1.getLength());
		assertEquals(mappingAction.getRowAliasViewList().get(1).getLength(), rowAlias2.getLength());
		assertEquals(mappingAction.getRowAliasViewList().get(2).getLength(), rowAlias3.getLength());

	}


	@Test(expected= NullPointerException.class)
	public void TestPopulateRowAliasViewWithEmptyRowAlias(){
		
		Mapping mapping = new Mapping();
				
		RowAlias rowAlias1 = new RowAlias();
		RowAlias rowAlias2 = new RowAlias();
		RowAlias rowAlias3 = new RowAlias();
		
		Map<String, RowAlias> rowAliasMap = new TreeMap<String, RowAlias>();
		rowAliasMap.put("rowAlias1", rowAlias1);
		rowAliasMap.put("rowAlias2", rowAlias2);
		rowAliasMap.put("rowAlias3", rowAlias3);
		
		mapping.setRowAlias(rowAliasMap);
			
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping);
		MappingAction mappingAction = new MappingAction();
				
		// populateRowAliasViewList() returns void.
		mappingAction.populateRowAliasViewList(mapping);
		
		// NUllPointerException expected 
		assertEquals(mappingAction.getRowAliasViewList().get(0).getAlias(), rowAlias1.getAlias());
		

	}
	
	@Test(expected= IndexOutOfBoundsException.class)
	public void TestPopulateRowAliasViewListWithEmptyMap(){
		
		Mapping mapping = new Mapping();
		
		Map<String, RowAlias> rowAliasMap = new TreeMap<String, RowAlias>();
	
		mapping.setRowAlias(rowAliasMap);
			
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping);
		MappingAction mappingAction = new MappingAction();
				
		// populateRowAliasViewList() returns void.
		mappingAction.populateRowAliasViewList(mapping);
		
		// Test aliases
		assertEquals(mappingAction.getRowAliasViewList().get(0), null);

	}
	
	@Test
	public void testGetColumnType(){
		
		Datastore datastore = new Datastore();
		
		ValueType valueType1 = new ValueType((long) 1001, datastore, "valueType1", "className1", true);
		ValueType valueType2 = new ValueType((long) 1002, datastore, "valueType2", "className2", true);
		ValueType valueType3 = new ValueType((long) 1003, datastore, "valueType3", "className3", true);
		
		List<ValueType> valueTypeList = new ArrayList<ValueType>();
		valueTypeList.add(valueType1);
		valueTypeList.add(valueType2);
		valueTypeList.add(valueType3);
		
		ValueTypeDAO mockedValueTypeDAO = mock(ValueTypeDAO.class);
		MappingAction mappingAction = new MappingAction();
		mappingAction.setValueTypeDAO(mockedValueTypeDAO);
		when(mockedValueTypeDAO.findAll()).thenReturn(valueTypeList);
		
		ValueType columnType1 = mappingAction.getColumnType("valueType1");
		ValueType columnType2 = mappingAction.getColumnType("valueType2");
		ValueType columnType3 = mappingAction.getColumnType("valueType3");
		
		// return type match
		assertEquals(columnType1.getClass(), ValueType.class );
		
		//
		assertEquals(columnType1, valueType1);
		assertEquals(columnType2, valueType2);
		assertEquals(columnType3, valueType3);
				
	}
	
		
	// test editMapping() remains. feasible to test.
	
	@Test
	public void testDeleteMapping(){
		
		// mappings with only two fields.
		Mapping mapping1 = new Mapping();
		mapping1.setId(1001);
		mapping1.setName("mapping1");
		 
		Mapping mapping2 = new Mapping();
		mapping2.setId(2002);
		mapping2.setName("mapping2");
		
		Mapping mapping3 = new Mapping();
		mapping2.setId(3003);
		mapping3.setName("mapping3");
		
		List<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping1);
		mappingList.add(mapping2);
		mappingList.add(mapping3);
				
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		MappingAction mappingAction = new MappingAction();
		mappingAction.setMappingDAO(mockedMappingDAO);
		
		// Problem here.
		try {
			when(mockedMappingDAO.findById((long)1001)).thenReturn(mapping1);
			when(mockedMappingDAO.findById((long)2002)).thenReturn(mapping2);
			when(mockedMappingDAO.findById((long)3003)).thenReturn(mapping3);
		} catch (CruxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			when(mockedMappingDAO.findAll()).thenReturn(mappingList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// populateMappingViewList() returns void.
		mappingAction.populateMappingViewList();
		
		List<MappingView> mappingViewListBefore = mappingAction.getMappingViewList();
		System.out.println("********************");
		System.out.println(mappingViewListBefore.get(0).getId());
		System.out.println(mappingViewListBefore.get(1).getId());
		System.out.println(mappingViewListBefore.get(2).getId());
		System.out.println("@@@@@@@@@@@@@@@@@@@@");
		
		assertEquals(mappingViewListBefore.get(0).getId(), mapping1.getId());
		
		mappingAction.setMapping(mapping1);
		String successString = mappingAction.deleteMapping();
		
		assertEquals(successString, "success");
		
		
		
		mappingAction.populateMappingViewList();
		
		List<MappingView> mappingViewListAfter = mappingAction.getMappingViewList();
		
		assertEquals(mappingViewListBefore.get(0).getId(), mapping1.getId());
		
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println(mappingViewListAfter);
		System.out.println("######################");
		
		//assertEquals();
	}
		
	
	
}
