package co.nubetech.crux.server.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.model.ValueType;

public class TestRangeFilters {

	@Test
	public void testContainsTrue() {
		FilterType type1 = new FilterType();
		type1.setType("Greater Than");

		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		
		RowAlias alias1 = new RowAlias();
		alias1.setAlias("alias");
		alias1.setValueType(valueType1);
		alias1.setLength(8);
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(type1);
		rowFilter1.setValue("123");
		rowFilter1.setRowAlias(alias1);
		
		FilterType type2 = new FilterType();
		type2.setType("Equals");

		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.String");
		
		RowAlias alias2 = new RowAlias();
		alias2.setAlias("alias");
		alias2.setValueType(valueType2);
		alias2.setLength(5);
		
		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setFilterType(type2);
		rowFilter2.setValue("SSSSS");
		rowFilter2.setRowAlias(alias2);
		
		RowAlias alias3 = new RowAlias();
		alias3.setAlias("alias3");
		
		RowAliasFilter rowFilter3 = new RowAliasFilter();
		rowFilter3.setFilterType(type2);
		rowFilter3.setValue("SSSSS");
		rowFilter3.setRowAlias(alias3);
		
		ArrayList<RowAliasFilter> greaterFilters = new ArrayList<RowAliasFilter>();
		greaterFilters.add(rowFilter1);
		
		ArrayList<RowAliasFilter> lesserFilters = new ArrayList<RowAliasFilter>();
		lesserFilters.add(rowFilter2);
		
		RangeFilters range = new RangeFilters(lesserFilters, greaterFilters);
		assertTrue(range.contains(rowFilter2));
		assertTrue(range.contains(rowFilter1));
		assertFalse(range.contains(rowFilter3));
		
	}
	
	@Test
	public void testContainsNullPassed() {
		FilterType type1 = new FilterType();
		type1.setType("Greater Than");

		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		
		RowAlias alias1 = new RowAlias();
		alias1.setAlias("alias");
		alias1.setValueType(valueType1);
		alias1.setLength(8);
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(type1);
		rowFilter1.setValue("123");
		rowFilter1.setRowAlias(alias1);
		
		FilterType type2 = new FilterType();
		type2.setType("Equals");

		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.String");
		
		RowAlias alias2 = new RowAlias();
		alias2.setAlias("alias");
		alias2.setValueType(valueType2);
		alias2.setLength(5);
		
		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setFilterType(type2);
		rowFilter2.setValue("SSSSS");
		rowFilter2.setRowAlias(alias2);
		
		RowAlias alias3 = new RowAlias();
		alias3.setAlias("alias3");
		
		RowAliasFilter rowFilter3 = new RowAliasFilter();
		rowFilter3.setFilterType(type2);
		rowFilter3.setValue("SSSSS");
		rowFilter3.setRowAlias(alias3);
		
		ArrayList<RowAliasFilter> greaterFilters = new ArrayList<RowAliasFilter>();
		greaterFilters.add(rowFilter1);
		
		ArrayList<RowAliasFilter> lesserFilters = new ArrayList<RowAliasFilter>();
		lesserFilters.add(rowFilter2);
		lesserFilters.add(rowFilter3);
		
		RangeFilters range = new RangeFilters(lesserFilters, greaterFilters);
		assertTrue(range.contains(rowFilter2));
		assertTrue(range.contains(rowFilter1));
		assertFalse(range.contains(null));
		
	}
	
	@Test
	public void testContainsBlank() {
		FilterType type1 = new FilterType();
		type1.setType("Greater Than");

		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		
		RowAlias alias1 = new RowAlias();
		alias1.setAlias("alias");
		alias1.setValueType(valueType1);
		alias1.setLength(8);
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(type1);
		rowFilter1.setValue("123");
		rowFilter1.setRowAlias(alias1);
		
		RangeFilters range = new RangeFilters();
		assertFalse(range.contains(rowFilter1));
		
	}
}
