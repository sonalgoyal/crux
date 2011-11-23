package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestDatastore {
	
	@Test
	public void testEqualsWithNoValuesNull(){
		Datastore datastore = new Datastore();
		datastore.setId(0l);
		datastore.setName("testDatastore");
		
		Datastore datastore1 = new Datastore();
		datastore1.setId(0l);
		datastore1.setName("testDatastore");
		
		assertTrue(datastore.equals(datastore1));
	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		Datastore datastore = new Datastore();
		datastore.setId(0l);
		datastore.setName("testDatastore");
		
		Datastore datastore1 = new Datastore();
		datastore1.setId(1l);
		datastore1.setName("testDatastore1");
		
		assertFalse(datastore.equals(datastore1));
	}
	
	@Test
	public void testEqualsWithAllValuesNull(){
		Datastore datastore = new Datastore();
		Datastore datastore1 = new Datastore();
		assertTrue(datastore.equals(datastore1));
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		Datastore datastore = new Datastore();
		datastore.setId(0l);
		datastore.setName("testDatastore");
		
		Datastore datastore1 = new Datastore();
		datastore1.setId(0l);
		datastore1.setName("testDatastore");
		
		assertTrue(datastore.hashCode() == datastore1.hashCode());
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		Datastore datastore = new Datastore();
		Datastore datastore1 = new Datastore();
		assertTrue(datastore.hashCode() == datastore1.hashCode());
	}

}
