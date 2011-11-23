package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestMapping {
	
	@Test
	public void testEqualsWithNoValuesNull(){
		Mapping mapping = new Mapping();
		mapping.setConnection(new Connection());
		mapping.setId(0l);
		mapping.setName("testMapping");
		mapping.setTableName("testTable");
		
		Mapping mapping1 = new Mapping();
		mapping1.setConnection(new Connection());
		mapping1.setId(0l);
		mapping1.setName("testMapping");
		mapping1.setTableName("testTable");
		
		assertTrue(mapping.equals(mapping1));
	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		Mapping mapping = new Mapping();
		mapping.setConnection(new Connection());
		mapping.setId(0l);
		mapping.setName("testMapping");
		mapping.setTableName("testTable");
		
		Mapping mapping1 = new Mapping();
		mapping1.setConnection(new Connection());
		mapping1.setId(1l);
		mapping1.setName("testMapping1");
		mapping1.setTableName("testTable1");
		
		assertFalse(mapping.equals(mapping1));
	}
	
	@Test
	public void testEqualsWithAllValuesNull(){
		Mapping mapping = new Mapping();
		Mapping mapping1 = new Mapping();
		assertTrue(mapping.equals(mapping1));
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		Mapping mapping = new Mapping();
		mapping.setConnection(new Connection());
		mapping.setId(0l);
		mapping.setName("testMapping");
		mapping.setTableName("testTable");
		
		Mapping mapping1 = new Mapping();
		mapping1.setConnection(new Connection());
		mapping1.setId(0l);
		mapping1.setName("testMapping");
		mapping1.setTableName("testTable");
		
		assertTrue(mapping.hashCode() == mapping1.hashCode());
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		Mapping mapping = new Mapping();
		Mapping mapping1 = new Mapping();
		assertTrue(mapping.hashCode() == mapping1.hashCode());
	}

}
