package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestValueFilterType {
	@Test
	public void testEquals() {
		ValueType valueType = new ValueType();
		FilterType filtertype = new FilterType();

		ValueFilterType valFilterType = new ValueFilterType();
		valFilterType.setFilterType(filtertype);
		valFilterType.setValueType(valueType);

		ValueFilterType valFilterType1 = new ValueFilterType();
		valFilterType1.setFilterType(filtertype);
		valFilterType1.setValueType(valueType);

		assertTrue(valFilterType.equals(valFilterType1));
	}
	
	@Test
	public void testEqualsForNullValue() {

		ValueFilterType valFilterType = new ValueFilterType();
		valFilterType.setFilterType(null);
		valFilterType.setValueType(null);

		ValueFilterType valFilterType1 = new ValueFilterType();
		valFilterType1.setFilterType(null);
		valFilterType1.setValueType(null);

		assertTrue(valFilterType.equals(valFilterType1));
	}
	
	@Test
	public void testHashCode() {
		ValueType valueType = new ValueType();
		FilterType filtertype = new FilterType();

		ValueFilterType valFilterType = new ValueFilterType();
		valFilterType.setFilterType(filtertype);
		valFilterType.setValueType(valueType);

		ValueFilterType valFilterType1 = new ValueFilterType();
		valFilterType1.setFilterType(filtertype);
		valFilterType1.setValueType(valueType);

		assertTrue(valFilterType.hashCode()==valFilterType1.hashCode());
	}
	
	@Test
	public void testHashCodeForNullValue() {

		ValueFilterType valFilterType = new ValueFilterType();
		valFilterType.setFilterType(null);
		valFilterType.setValueType(null);

		ValueFilterType valFilterType1 = new ValueFilterType();
		valFilterType1.setFilterType(null);
		valFilterType1.setValueType(null);

		assertTrue(valFilterType.hashCode()==valFilterType1.hashCode());
	}
}
