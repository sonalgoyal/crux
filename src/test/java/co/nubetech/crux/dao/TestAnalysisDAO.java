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
package co.nubetech.crux.dao;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.junit.Test;

import co.nubetech.crux.model.Analysis;
import co.nubetech.crux.util.CruxException;

public class TestAnalysisDAO extends DBConnection {
	
	private final static Logger logger = Logger
		.getLogger(co.nubetech.crux.dao.TestAnalysisDAO.class);
	
	@Test
	public void testSaveAnalysis() throws Exception, CruxException {
		Statement stmt = getStatement();
		AnalysisDAO analysisDAO = new AnalysisDAO();
		
		long analysisId = 0;
		try{
			analysisDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
			
			Analysis analysis = new Analysis();
			//analysis.setId(1212);
			analysis.setName("TestAnalysis");
			analysis.setText("{foo:\"a\",boo:12}");
			
			analysisDAO.transaction = analysisDAO.session.getTransaction();
			analysisId = analysisDAO.save(analysis);
			
			ResultSet rsOfAnalysis = stmt
				.executeQuery("select * from analysis where id="+analysisId);
			
			while(rsOfAnalysis.next()){
				assertEquals(rsOfAnalysis.getLong("id"), analysisId);
				assertEquals(rsOfAnalysis.getString("name"), "TestAnalysis");
				assertEquals(rsOfAnalysis.getString("text"), "{foo:\"a\",boo:12}" );
			}
			rsOfAnalysis.close();
			
		}
		finally{
			stmt.executeUpdate("delete from analysis where id=" + analysisId);
			analysisDAO.session.close();
			stmt.close();			
		}		
	}
	
    @Test
    public void testFindById() throws Exception, CruxException {
    	Statement stmt = getStatement();
		stmt.executeUpdate("insert into analysis(id,name,text) values(16,'text','{foo:\"a\",boo:12}')");
    	
		AnalysisDAO analysisDAO = new AnalysisDAO();
		analysisDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		analysisDAO.transaction = analysisDAO.session.getTransaction();
		try {
		  Analysis analysis = analysisDAO.findById(16l);
		  logger.debug("AnalysisId: " + analysis.getId());
		  assertEquals(16, analysis.getId());
		  assertEquals(analysis.getName(), "text");
		  assertEquals(analysis.getText(), "{foo:\"a\",boo:12}");

		} 
		finally {
		  stmt.executeUpdate("delete from analysis where id=" + 16);
		  analysisDAO.session.close();
		  stmt.close();
		}
    }
    
    @Test
	public void testFindByIdWhenIdNotExist() throws Exception, CruxException {
    	AnalysisDAO analysisDAO = new AnalysisDAO();
    	analysisDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
    		.getNewSession();
    	analysisDAO.transaction = analysisDAO.session.getTransaction();
    	boolean analysisNotExist = false;
    	try{
    		Analysis analysis = analysisDAO.findById(1234l);
    		
    	}catch(CruxException e){
    		analysisNotExist = true;
    	}
    	assertTrue(analysisNotExist);
    	analysisDAO.session.close();
    }
    
    @Test
	public void testDeleteAnalysis() throws Exception, CruxException {
    	
    	Statement stmt = getStatement();
    	stmt.executeUpdate("insert into analysis(id,name,text) values(16,'text','{foo:\"a\",boo:12}')");
    	AnalysisDAO analysisDAO = new AnalysisDAO();
    	try{
    		analysisDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
    			.getNewSession();
    	    analysisDAO.transaction = analysisDAO.session.getTransaction();
    	    Analysis analysisDeleteId = analysisDAO.findById(16l);
    	    long analysisDeleteForId = analysisDAO.delete(analysisDeleteId);
    	    
    	    assertEquals(analysisDeleteForId, 16);
    	    
    	    ResultSet rsOfAnalysisDelete = stmt
    	    	.executeQuery("select id from analysis where id="+16);
    	    assertEquals(rsOfAnalysisDelete.next(), false);
    	    rsOfAnalysisDelete.close();
    	}finally{
    		analysisDAO.session.close();
    		stmt.close();
    	}
    		
    }
    
}
