package co.nubetech.crux.model;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestFunction {
	
	private static ValueType valueType;
	
	@BeforeClass
	public static void setUp() {
		valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");
		valueType.setName("int");
	}
	
	@Test
	public void testEqualsWithNoValuesNull(){
		Function function = new Function(0, "sum","class.Sum",false, valueType, valueType);
		Function functions1 = new Function(0,"sum","class.Sum",false, valueType, valueType);
		
		assertTrue(function.equals(functions1));
	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		Function function = new Function(1, "sum","class.Sum",false, valueType, valueType);
		Function functions1 = new Function(1, "sum","class.Sum",false, valueType, valueType);
		
		assertTrue(!function.equals(functions1));
	}
	
	@Test
	public void testEqualsWithAllValuesNull(){
		Function function = new Function();
		Function functions1 = new Function();
		
		assertTrue(function.equals(functions1));
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		Function function = new Function(1, "sum","class.Sum",false, valueType, valueType);
		Function functions1 = new Function(1, "sum","class.Sum",false, valueType, valueType);
		
		assertTrue(function.hashCode() == functions1.hashCode());
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		Function function = new Function();
		Function functions1 = new Function();
		
		assertTrue(function.hashCode() == functions1.hashCode());
	}

}
