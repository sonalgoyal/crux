package co.nubetech.crux.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestFunctionTypeMapping {
	
	@Test
	public void testEqualsWithNoValuesNull(){
		Function function = new Function("sum","class.Sum",false);
		ValueType valueType = new ValueType();
		valueType.setClassName("testClass");
		valueType.setDatastore(new Datastore());
		valueType.setId(0l);
		valueType.setName("testName");
		valueType.setNumeric(true);	
		
		FunctionTypeMapping functionTypeMapping1 = new FunctionTypeMapping(function, valueType, valueType);
		FunctionTypeMapping functionTypeMapping2 = new FunctionTypeMapping(function, valueType, valueType);
		assertTrue(functionTypeMapping1.equals(functionTypeMapping2));
	}
	
	
	@Test
	public void testEqualsForNotEqualObjects(){
		Function functions1 = new Function("sum","class.Sum",false);
		Function functions2 = new Function("sum","class.Sum", false);
		ValueType valueType = new ValueType();
		valueType.setClassName("testClass");
		valueType.setDatastore(new Datastore());
		valueType.setId(0l);
		valueType.setName("testName");
		valueType.setNumeric(true);	
		
		FunctionTypeMapping functionTypeMapping1 = new FunctionTypeMapping(functions1, valueType, valueType);
		FunctionTypeMapping functionTypeMapping2 = new FunctionTypeMapping(functions2, valueType, valueType);
		assertTrue(!functionTypeMapping1.equals(functionTypeMapping2));
	}
	
	@Test
	public void testEqualsWithAllValuesNull(){
		FunctionTypeMapping functionTypeMapping1 = new FunctionTypeMapping();
		FunctionTypeMapping functionTypeMapping2 = new FunctionTypeMapping();
		assertTrue(functionTypeMapping1.equals(functionTypeMapping2));
	}
	
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		Function function = new Function("sum","class.Sum",false);
		ValueType valueType = new ValueType();
		valueType.setClassName("testClass");
		valueType.setDatastore(new Datastore());
		valueType.setId(0l);
		valueType.setName("testName");
		valueType.setNumeric(true);	
		
		FunctionTypeMapping functionTypeMapping1 = new FunctionTypeMapping(function, valueType, valueType);
		FunctionTypeMapping functionTypeMapping2 = new FunctionTypeMapping(function, valueType, valueType);
		
		assertTrue(functionTypeMapping1.hashCode() == functionTypeMapping2.hashCode());
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		FunctionTypeMapping functionTypeMapping1 = new FunctionTypeMapping();
		FunctionTypeMapping functionTypeMapping2 = new FunctionTypeMapping();
		
		assertTrue(functionTypeMapping1.hashCode() == functionTypeMapping2.hashCode());
	}

}
