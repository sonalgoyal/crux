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
package co.nubetech.crux.view;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import co.nubetech.crux.model.Analysis;

public class TestAnalysisView {
	
	@Test
	public void testAnalysisView(){
		
		Analysis analysis = new Analysis();
		analysis.setId(1l);
		analysis.setName("TestAnalysisView");
		analysis.setText("{foo:\"a\",boo:12}");
		
		AnalysisView analysisView = new AnalysisView(11,analysis);
		assertEquals(analysisView.getId(), 1l);
		assertEquals(analysisView.getName(), "TestAnalysisView");
		assertEquals(analysisView.getText(), "{foo:\"a\",boo:12}");
		assertEquals(analysisView.getIndex(), 11);		
	}

}
