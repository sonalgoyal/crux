package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConnectionProperty {
	
	@Test
	public void testEqualsWithNoValuesNull(){
		Datastore datastore = new Datastore();
		datastore.setName("testDatastore");
		
		User user = new User();
		user.setName("testUser");
		user.setPassword("testPassword");
		
		Connection connection = new Connection();
		connection.setName("testConnection");
		connection.setDatastore(datastore);
		connection.setUser(user);
		connection.setId(0l);
		
		ConnectionProperty property = new ConnectionProperty();
		property.setConnection(connection);
		property.setProperty("testProperty");
		property.setValue("testValue");
		
		ConnectionProperty property1 = new ConnectionProperty();
		property1.setConnection(connection);
		property1.setProperty("testProperty");
		property1.setValue("testValue");
		
		assertTrue(property.equals(property1));
	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		ConnectionProperty property = new ConnectionProperty();
		property.setConnection(new Connection());
		property.setProperty("testProperty");
		property.setValue("testValue");
		
		ConnectionProperty property1 = new ConnectionProperty();
		property1.setConnection(new Connection());
		property1.setProperty("testProperty1");
		property1.setValue("testValue1");
		
		assertFalse(property.equals(property1));
	}
	
	@Test
	public void testEqualsWithAllValuesNull(){
		ConnectionProperty property = new ConnectionProperty();		
		ConnectionProperty property1 = new ConnectionProperty();
		assertTrue(property.equals(property1));
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		Datastore datastore = new Datastore();
		datastore.setName("testDatastore");
		
		User user = new User();
		user.setName("testUser");
		user.setPassword("testPassword");
		
		Connection connection = new Connection();
		connection.setName("testConnection");
		connection.setDatastore(datastore);
		connection.setUser(user);
		connection.setId(0l);
		
		ConnectionProperty property = new ConnectionProperty();
		property.setConnection(connection);
		property.setProperty("testProperty");
		property.setValue("testValue");
		
		ConnectionProperty property1 = new ConnectionProperty();
		property1.setConnection(connection);
		property1.setProperty("testProperty");
		property1.setValue("testValue");
		
		assertTrue(property.hashCode() == property1.hashCode());
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		ConnectionProperty property = new ConnectionProperty();
		ConnectionProperty property1 = new ConnectionProperty();
		assertTrue(property.hashCode() == property1.hashCode());
	}

}
