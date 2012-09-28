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
package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAnalysis {

	@Test
	public void testEqualsWithAllValuesNull(){
		Analysis analysis = new Analysis();
		Analysis analysis1 = new Analysis();
		assertTrue(analysis.equals(analysis1));
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		Analysis analysis = new Analysis();
		Analysis analysis1 = new Analysis();
		assertTrue(analysis.hashCode() == analysis1.hashCode());
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		Analysis analysis = new Analysis();
		analysis.setId(1l);
		analysis.setName("TestAnalysis");
		analysis.setText("{foo:\"a\",boo:12}");
		
		Analysis analysis1 = new Analysis();
		analysis1.setId(1l);
		analysis1.setName("TestAnalysis");
		analysis1.setText("{foo:\"a\",boo:12}");
		
		assertTrue(analysis.hashCode() == analysis1.hashCode());

	}
	
	@Test
	public void testEqualsWithNoValuesNull(){
		Analysis analysis = new Analysis();
		analysis.setId(1l);
		analysis.setName("TestAnalysis");
		analysis.setText("{foo:\"a\",boo:12}");
		
		Analysis analysis1 = new Analysis();
		analysis1.setId(1l);
		analysis1.setName("TestAnalysis");
		analysis1.setText("{foo:\"a\",boo:12}");
		
		assertTrue(analysis.equals(analysis1));
	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		Analysis analysis = new Analysis();
		analysis.setId(1l);
		analysis.setName("TestAnalysis");
		analysis.setText("{foo:\"a\",boo:12}");
		
		Analysis analysis1 = new Analysis();
		analysis1.setId(1l);
		analysis1.setName("TestAnalysis1");
		analysis1.setText("{foo:\"a\",boo:12}");
		
		assertFalse(analysis.equals(analysis1));
	}
	
}
