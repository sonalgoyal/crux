package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestValueType {
	
	@Test
	public void testEqualsWithNoValuesNull(){
		ValueType valueType = new ValueType();
		valueType.setClassName("testClass");
		valueType.setDatastore(new Datastore());
		valueType.setId(0l);
		valueType.setName("testName");
		valueType.setNumeric(true);
		
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("testClass");
		valueType1.setDatastore(new Datastore());
		valueType1.setId(0l);
		valueType1.setName("testName");
		valueType1.setNumeric(true);
		
		assertTrue(valueType.equals(valueType1));
	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		ValueType valueType = new ValueType();
		valueType.setClassName("testClass");
		valueType.setDatastore(new Datastore());
		valueType.setId(0l);
		valueType.setName("testName");
		valueType.setNumeric(true);
		
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("testClass1");
		valueType1.setDatastore(new Datastore());
		valueType1.setId(1l);
		valueType1.setName("testName1");
		valueType1.setNumeric(false);
		
		assertFalse(valueType.equals(valueType1));
	}
	
	@Test
	public void testEqualsWithAllValuesNull(){
		ValueType valueType = new ValueType();
		ValueType valueType1 = new ValueType();
		assertTrue(valueType.equals(valueType1));
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		ValueType valueType = new ValueType();
		valueType.setClassName("testClass");
		valueType.setDatastore(new Datastore());
		valueType.setId(0l);
		valueType.setName("testName");
		valueType.setNumeric(true);
		
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("testClass");
		valueType1.setDatastore(new Datastore());
		valueType1.setId(0l);
		valueType1.setName("testName");
		valueType1.setNumeric(true);
		
		assertTrue(valueType.hashCode() == valueType1.hashCode());
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		ValueType valueType = new ValueType();
		ValueType valueType1 = new ValueType();
		assertTrue(valueType.hashCode() == valueType1.hashCode());
	}

}
