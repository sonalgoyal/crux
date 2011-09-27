/**
 * Copyright 2011 Nube Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package co.nubetech.crux.server.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.log4j.Logger;
import org.junit.Test;

import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.server.filter.ComparatorNotFoundException;
import co.nubetech.crux.server.filter.FilterHelper;
import co.nubetech.crux.util.CruxException;

public class TestFilterHelper {

	final static Logger logger = Logger.getLogger(TestFilterHelper.class);

	@Test
	public void testGetCompareOp() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");
		assertEquals(CompareOp.EQUAL, FilterHelper.getCompareOp(type));
	}

	/*@Test(expected = CruxException.class)
	public void testGetCompareOpExcptn() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals is not going to match");
		FilterHelper.getCompareOp(type);
	}*/

	@Test
	public void testGetCompareOpGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");
		assertEquals(CompareOp.GREATER, FilterHelper.getCompareOp(type));
	}
	
	@Test
	public void testGetCompareOpNots() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");
		assertEquals(CompareOp.NOT_EQUAL, FilterHelper.getCompareOp(type));
		type.setType("Pattern Not Matches");
		assertEquals(CompareOp.NOT_EQUAL, FilterHelper.getCompareOp(type));
		type.setType("Substring Not Equals");
		//assertEquals(CompareOp.NOT_EQUAL, FilterHelper.getCompareOp(type));
		
	}


	/*@Test
	public void testGetColumnComparator() throws ComparatorNotFoundException {
		ColumnFilter columnFilter = new ColumnFilter();
		ColumnAlias alias = new ColumnAlias();
		ValueType type = new ValueType();
		type.setClassName("java.lang.Long");
		alias.setValueType(type);
		columnFilter.setColumnAlias(alias);
		logger.debug("Class is "
				+ columnFilter.getColumnAlias().getValueType().getClassName());
		assertTrue(FilterHelper.getColumnComparator(columnFilter).getClass()
				.getName()
				.equals("co.nubetech.crux.server.filter.types.LongComparator"));
	}*/

	@Test
	public void testGetFilterTypeString() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");
		assertEquals("GREATERTHAN",
				FilterHelper.getTrimUpperFilterTypeString(type));
	}

	@Test(expected = CruxException.class)
	public void testGetFilterTypeStringBlank() throws CruxException {
		FilterType type = new FilterType();
		FilterHelper.getTrimUpperFilterTypeString(type);
	}

	@Test(expected = CruxException.class)
	public void testIsEqualsFilterNull() throws CruxException {
		FilterHelper.isEqualsFilter(null);
	}

	@Test
	public void testIsEqualsFilterEq() throws CruxException {
		RowAliasFilter filter = new RowAliasFilter();
		FilterType type = new FilterType();
		type.setType("Equals");
		filter.setFilterType(type);
		assertTrue(FilterHelper.isEqualsFilter(filter));
	}
	
	@Test
	public void testIsNotEqualsFilterEq() throws CruxException {
		RowAliasFilter filter = new RowAliasFilter();
		FilterType type = new FilterType();
		type.setType("Not Equals");
		filter.setFilterType(type);
		assertFalse(FilterHelper.isEqualsFilter(filter));
	}

}
