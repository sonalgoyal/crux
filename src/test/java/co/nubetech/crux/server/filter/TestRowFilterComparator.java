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

import org.junit.Test;

import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.server.filter.RowFilterComparator;

public class TestRowFilterComparator {

	@Test
	public void testFirstRowFilterNull() {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		RowAliasFilter rowFilter = new RowAliasFilter(new Report(), new FilterType(),
				"value", rowAlias);
		RowFilterComparator rowFilterComparator = new RowFilterComparator();
		int result = rowFilterComparator.compare(null, rowFilter);
		assertEquals(-1, result);
	}

	@Test
	public void testSecondRowFilterNull() {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		RowAliasFilter rowFilter = new RowAliasFilter(new Report(), new FilterType(),
				"value", rowAlias);
		RowFilterComparator rowFilterComparator = new RowFilterComparator();
		int result = rowFilterComparator.compare(rowFilter, null);
		assertEquals(1, result);
	}

	@Test
	public void testBothRowFilterNull() {
		RowFilterComparator rowFilterComparator = new RowFilterComparator();
		int result = rowFilterComparator.compare(null, null);
		assertEquals(0, result);
	}

	@Test
	public void testFirstRowAliasNull() {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		RowAliasFilter rowFilter1 = new RowAliasFilter(new Report(), new FilterType(),
				"value", null);
		RowAliasFilter rowFilter2 = new RowAliasFilter(new Report(), new FilterType(),
				"value", rowAlias);
		RowFilterComparator rowFilterComparator = new RowFilterComparator();
		int result = rowFilterComparator.compare(rowFilter1, rowFilter2);
		assertEquals(-1, result);
	}

	@Test
	public void testSecondRowAliasNull() {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		RowAliasFilter rowFilter1 = new RowAliasFilter(new Report(), new FilterType(),
				"value", rowAlias);
		RowAliasFilter rowFilter2 = new RowAliasFilter(new Report(), new FilterType(),
				"value", null);
		RowFilterComparator rowFilterComparator = new RowFilterComparator();
		int result = rowFilterComparator.compare(rowFilter1, rowFilter2);
		assertEquals(1, result);
	}

	@Test
	public void testBothRowAliasNull() {
		RowAliasFilter rowFilter1 = new RowAliasFilter(new Report(), new FilterType(),
				"value", null);
		RowAliasFilter rowFilter2 = new RowAliasFilter(new Report(), new FilterType(),
				"value", null);
		RowFilterComparator rowFilterComparator = new RowFilterComparator();
		int result = rowFilterComparator.compare(rowFilter1, rowFilter2);
		assertEquals(0, result);
	}

	@Test
	public void testFirstRowAliasIdGreater() {
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(1);
		RowAliasFilter rowFilter1 = new RowAliasFilter(new Report(), new FilterType(),
				"value", rowAlias1);
		RowAliasFilter rowFilter2 = new RowAliasFilter(new Report(), new FilterType(),
				"value", rowAlias2);
		RowFilterComparator rowFilterComparator = new RowFilterComparator();
		int result = rowFilterComparator.compare(rowFilter1, rowFilter2);
		assertEquals(1, result);
	}

	@Test
	public void testSecondRowAliasIdGreater() {
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(1);
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(2);
		RowAliasFilter rowFilter1 = new RowAliasFilter(new Report(), new FilterType(),
				"value", rowAlias1);
		RowAliasFilter rowFilter2 = new RowAliasFilter(new Report(), new FilterType(),
				"value", rowAlias2);
		RowFilterComparator rowFilterComparator = new RowFilterComparator();
		int result = rowFilterComparator.compare(rowFilter1, rowFilter2);
		assertEquals(-1, result);
	}

	@Test
	public void testBothRowAliasIdEqual() {
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(2);
		RowAliasFilter rowFilter1 = new RowAliasFilter(new Report(), new FilterType(),
				"value", rowAlias1);
		RowAliasFilter rowFilter2 = new RowAliasFilter(new Report(), new FilterType(),
				"value", rowAlias2);
		RowFilterComparator rowFilterComparator = new RowFilterComparator();
		int result = rowFilterComparator.compare(rowFilter1, rowFilter2);
		assertEquals(0, result);
	}
}
