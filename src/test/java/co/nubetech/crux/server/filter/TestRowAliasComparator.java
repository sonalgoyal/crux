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

import co.nubetech.crux.model.RowAlias;

public class TestRowAliasComparator {
	
	@Test
	public void testFirstRowAliasNull() {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		RowAliasComparator rowAliasComparator = new RowAliasComparator();
		int result = rowAliasComparator.compare(null, rowAlias);
		assertEquals(-1, result);
	}

	@Test
	public void testSecondRowAliasNull() {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		RowAliasComparator rowAliasComparator = new RowAliasComparator();
		int result = rowAliasComparator.compare(rowAlias, null);
		assertEquals(1, result);
	}

	@Test
	public void testBothRowAliasNull() {
		RowAliasComparator RowAliasComparator = new RowAliasComparator();
		int result = RowAliasComparator.compare(null, null);
		assertEquals(0, result);
	}

	
	@Test
	public void testFirstRowAliasIdGreater() {
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(1);
		RowAliasComparator rowAliasComparator = new RowAliasComparator();
		int result = rowAliasComparator.compare(rowAlias1, rowAlias2);
		assertEquals(1, result);
	}

	@Test
	public void testSecondRowAliasIdGreater() {
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(1);
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(2);
		RowAliasComparator rowAliasComparator = new RowAliasComparator();
		int result = rowAliasComparator.compare(rowAlias1, rowAlias2);
		assertEquals(-1, result);
	}

	@Test
	public void testBothRowAliasIdEqual() {
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(2);
		RowAliasComparator rowAliasComparator = new RowAliasComparator();
		int result = rowAliasComparator.compare(rowAlias1, rowAlias2);
		assertEquals(0, result);
	}

}
