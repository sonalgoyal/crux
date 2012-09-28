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

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import co.nubetech.crux.model.Analysis;
import co.nubetech.crux.util.CruxException;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class AnalysisDAO {
	
	final static Logger logger = Logger
		.getLogger(co.nubetech.crux.dao.AnalysisDAO.class);

    @SessionTarget
    public Session session;
    @TransactionTarget
    public Transaction transaction;
    
	public Analysis findById(Long id) throws CruxException {
		Analysis analysis;
		try {
			logger.debug("Id to find analysis is: " + id);
			analysis = (Analysis) session.get(Analysis.class, id);
			analysis.getId();
		} catch (NullPointerException e) {
			logger.debug(id + " not exists.");
			throw new CruxException(
					"Selected analysis does not exists in the database.", e);
		}
		return analysis;
	}
	
	public long save(Analysis analysis) throws CruxException {
		try {
			transaction.begin();
			session.saveOrUpdate(analysis);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}

		return analysis.getId();
	}	
	
	public List<Analysis> findAll() {
		System.out.println("session is " + session);
		List<Analysis> result = session.createQuery(
				"from Analysis order by id").list();
		return result;
	}	

	public long delete(Analysis analysis) throws CruxException {
		long id = analysis.getId();
		try {
			transaction.begin();
			session.delete(analysis);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(),e);
		}
		return id;
	}	
}
