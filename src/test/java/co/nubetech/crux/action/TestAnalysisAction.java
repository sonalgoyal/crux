package co.nubetech.crux.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;

import co.nubetech.crux.dao.AnalysisDAO;
import co.nubetech.crux.model.Analysis;
import co.nubetech.crux.util.CruxException;

import java.util.ArrayList;
import java.util.List;

public class TestAnalysisAction {
	
	@Test
	public void testInitializeAnalysis(){
		
		Analysis analysis1 = new Analysis();
		analysis1.setId(1);
		analysis1.setName("Analysis1");
		analysis1.setText("{foo:\"ab\",boo:12}");
		
		Analysis analysis2 = new Analysis();
		analysis2.setId(2);
		analysis2.setName("Analysis2");
		analysis2.setText("{foo:\"abc\",boo:13}");
		
		Analysis analysis3 = new Analysis();
		analysis3.setId(3);
		analysis3.setName("Analysis3");
		analysis3.setText("{foo:\"abcd\",boo:14}");
		
		//List of Above created Analysis
		List<Analysis> analysisList = new ArrayList<Analysis>();
		analysisList.add(analysis1);
		analysisList.add(analysis2);
		analysisList.add(analysis3);
		
		// mock DAO
		AnalysisDAO mockedAnalysisDAO = mock(AnalysisDAO.class);
		AnalysisAction analysisAction = new AnalysisAction();
		analysisAction.setAnalysisDAO(mockedAnalysisDAO);
		
		when(mockedAnalysisDAO.findAll()).thenReturn(analysisList);
		String successString = analysisAction.initializeAnalysis();
		assertEquals(successString, "success");		
		//Test Analysis1
		assertEquals(analysisList.get(0).getName(), analysisAction.getAnalysisViewList().get(0).getName());
		assertEquals(analysisList.get(0).getText(), analysisAction.getAnalysisViewList().get(0).getText());
		assertEquals(analysisList.get(0).getId(), analysisAction.getAnalysisViewList().get(0).getId());
		
		//Test Analysis2
		assertEquals(analysisList.get(1).getName(), analysisAction.getAnalysisViewList().get(1).getName());
		assertEquals(analysisList.get(1).getText(), analysisAction.getAnalysisViewList().get(1).getText());
		assertEquals(analysisList.get(1).getId(), analysisAction.getAnalysisViewList().get(1).getId());
		
		//Test Analysis1
		assertEquals(analysisList.get(2).getName(), analysisAction.getAnalysisViewList().get(2).getName());
		assertEquals(analysisList.get(2).getText(), analysisAction.getAnalysisViewList().get(2).getText());
		assertEquals(analysisList.get(2).getId(), analysisAction.getAnalysisViewList().get(2).getId());
	}
	
	@Test
	public void testInitializeAnalysisWithEmptyList(){
		
		List<Analysis> analysisList = new ArrayList<Analysis>();
		
		AnalysisDAO mockedAnalysisDAO = mock(AnalysisDAO.class);
		AnalysisAction analysisAction = new AnalysisAction();
		analysisAction.setAnalysisDAO(mockedAnalysisDAO);
		when(mockedAnalysisDAO.findAll()).thenReturn(analysisList);
		
		String successString = analysisAction.initializeAnalysis();
		
		assertEquals(successString, "success");
		assertEquals(analysisAction.getAnalysisViewList().size(), 0);
		
	}
	
	@Test
	public void testSaveAnalysis() throws CruxException{
		
		Analysis analysis = new Analysis();
		analysis.setId(1l);
		analysis.setName("Analysis1");
		analysis.setText("{foo:\"ab\",boo:12}");
		
		AnalysisDAO mockedAnalysisDAO = mock(AnalysisDAO.class);
		AnalysisAction analysisAction = new AnalysisAction();
		analysisAction.setAnalysisDAO(mockedAnalysisDAO);
		when(mockedAnalysisDAO.save(analysis)).thenReturn(1212l);
		
		String successString = analysisAction.saveAnalysis();
		
		assertEquals(analysisAction.getAnalysisDAO().save(analysis), 1212l);
		assertEquals(successString, "success");
	}
	
	@Test
	public void testDeleteAnalysis() throws CruxException{

		Analysis analysis = new Analysis();
		analysis.setId(1l);
		analysis.setName("Analysis1");
		analysis.setText("{foo:\"ab\",boo:12}");

		AnalysisDAO mockedAnalysisDAO = mock(AnalysisDAO.class);
		AnalysisAction analysisAction = new AnalysisAction();
		analysisAction.setAnalysisDAO(mockedAnalysisDAO);
		when(mockedAnalysisDAO.delete(analysis)).thenReturn(1212l);
		
		String successString = analysisAction.deleteAnalysis();
		
		assertEquals(analysisAction.getAnalysisDAO().delete(analysis), 1212l);
		assertEquals(successString, "success");
		
	}
	
}
