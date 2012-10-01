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
package co.nubetech.crux.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;

import co.nubetech.crux.dao.AnalysisDAO;
import co.nubetech.crux.model.Analysis;
import co.nubetech.crux.util.CruxError;
import co.nubetech.crux.util.CruxException;
import co.nubetech.crux.view.AnalysisView;

public class AnalysisAction extends CruxAction {
	
	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = Logger
		.getLogger(co.nubetech.crux.action.AnalysisAction.class);
	
	private Analysis analysis = new Analysis();
	private AnalysisDAO analysisDAO = new AnalysisDAO();
	public ServletContext context;
	private List<AnalysisView> analysisViewList = new ArrayList<AnalysisView>();
	
	public Analysis getAnalysis() {
		return analysis;
	}
	
	public void setAnalysis(Analysis analysis) {
		this.analysis = analysis;
	}
	
	public AnalysisDAO getAnalysisDAO() {
		return analysisDAO;
	}
	
	public void setAnalysisDAO(AnalysisDAO analysisDAO) {
		this.analysisDAO = analysisDAO;
	}
	
	public List<AnalysisView> getAnalysisViewList() {
		return analysisViewList;
	}
	
	public void setAnalysisViewList(List<AnalysisView> analysisViewList) {
		this.analysisViewList = analysisViewList;
	}
        
	public CruxError getError() {
		return error;
	}

	public void setError(CruxError error) {
		this.error = error;
	}    
	
	public String initializeAnalysis() {
		long index = 0;
		List<Analysis> analysisList = analysisDAO.findAll();
		logger.debug("Initiating Analysis:" + analysisList.size());
		for (Analysis analysis : analysisList) {
			analysisViewList.add(new AnalysisView(++index, analysis));
		}
		return SUCCESS;
	}	
	
	public String saveAnalysis() {
		logger.debug("Analysis Id is: " + analysis.getId());
		
		try {
			
			analysisDAO.save(analysis);
			
		} catch (CruxException e) {
			error.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			error.setMessage("Something Wrong has happened");
		}
		initializeAnalysis(); 
		return SUCCESS;
	}
	
	public String deleteAnalysis() {
		long analysisId = analysis.getId();
		
		try {
			analysis = analysisDAO.findById(analysisId);
			logger.debug("Deleting Analysis for analysisId: "
					+ analysisId);
			analysisDAO.delete(analysis);

		} catch (CruxException e) {
			e.printStackTrace();
			error.setMessage(e.getMessage());
		} catch (Exception e) {
			error.setMessage("Something Wrong has happened");
			e.printStackTrace();
		}
		initializeAnalysis(); 
		return SUCCESS;
	}		
	
	public String updateAnalysis() throws IOException {
		
		String analysisName = analysis.getName();
		String analysisText = analysis.getText();
		logger.debug("Updating analysisId: " + analysis.getId());
		
		try{
			analysis = analysisDAO.findById(analysis.getId());
			analysis.setName(analysisName);
			analysis.setText(analysisText);
			logger.debug("Analysis is saving");
			analysisDAO.save(analysis);
			
		} catch (CruxException e) {
			e.printStackTrace();
			error.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			error.setMessage("Something Wrong has happened");
		}
		
		initializeAnalysis();
		return SUCCESS;
		
	}
}
