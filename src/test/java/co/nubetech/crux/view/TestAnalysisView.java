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
