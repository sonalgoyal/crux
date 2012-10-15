package co.nubetech.crux.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestConnection {
	
	@Test
	public void testEqualsWithNoValuesNull(){
		ConnectionProperty property =new ConnectionProperty();
		property.setProperty("testProperty");
		property.setValue("testValue");	
		
		Datastore datastore = new Datastore();
		datastore.setName("testDatastore");
		
		User user = new User();
		user.setName("testUser");
		user.setPassword("testPassword");
		
		Connection connection = new Connection();
		connection.setDatastore(datastore);
		connection.setId(0l);
		connection.setName("testConnection");
		connection.setUser(user);
		connection.addProperty(property);
		
		Connection connection1 = new Connection();
		connection1.setDatastore(datastore);
		connection1.setId(0l);
		connection1.setName("testConnection");
		connection1.setUser(user);
		connection1.addProperty(property);
		
		assertTrue(connection.equals(connection1));
	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		Connection connection = new Connection();
		connection.setDatastore(new Datastore());
		connection.setId(0l);
		connection.setName("testConnection");
		connection.setUser(new User());
		
		Connection connection1 = new Connection();
		connection1.setDatastore(new Datastore());
		connection1.setId(1l);
		connection1.setName("testConnection1");
		connection1.setUser(new User());
		
		assertFalse(connection.equals(connection1));
	}
	
	@Test
	public void testEqualsWithAllValuesNull(){
		Connection connection = new Connection();
		Connection connection1 = new Connection();
		assertTrue(connection.equals(connection1));
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		ConnectionProperty property =new ConnectionProperty();
		property.setProperty("testProperty");
		property.setValue("testValue");	
		
		Datastore datastore = new Datastore();
		datastore.setName("testDatastore");
		
		User user = new User();
		user.setName("testUser");
		user.setPassword("testPassword");
		
		Connection connection = new Connection();
		connection.setDatastore(datastore);
		connection.setId(0l);
		connection.setName("testConnection");
		connection.setUser(user);
		connection.addProperty(property);
		
		Connection connection1 = new Connection();
		connection1.setDatastore(datastore);
		connection1.setId(0l);
		connection1.setName("testConnection");
		connection1.setUser(user);
		connection1.addProperty(property);
		
		assertTrue(connection.hashCode() == connection1.hashCode());
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		Connection connection = new Connection();		
		Connection connection1 = new Connection();
		assertTrue(connection.hashCode() == connection1.hashCode());
	}

}
