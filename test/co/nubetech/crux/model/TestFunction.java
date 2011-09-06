package co.nubetech.crux.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestFunction {
	
	@Test
	public void testEqualsWithNoValuesNull(){
		Function function = new Function("sum","class.Sum",(short)1);
		Function functions1 = new Function("sum","class.Sum",(short)1);
		
		assertTrue(function.equals(functions1));
	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		Function function = new Function("sum","class.Sum",(short)1);
		Function functions1 = new Function("sum","class.Sum",(short)2);
		
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
		Function function = new Function("sum","class.Sum",(short)1);
		Function functions1 = new Function("sum","class.Sum",(short)1);
		
		assertTrue(function.hashCode() == functions1.hashCode());
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		Function function = new Function();
		Function functions1 = new Function();
		
		assertTrue(function.hashCode() == functions1.hashCode());
	}

}
