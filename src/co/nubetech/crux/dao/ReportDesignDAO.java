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

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.util.CruxException;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class ReportDesignDAO {

	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;

	public ReportDesign findById(Long id) {
		ReportDesign report = (ReportDesign) session
				.get(ReportDesign.class, id);
		return report;
	}

	public long save(ReportDesign report) throws CruxException {
		try {
			transaction.begin();
			session.saveOrUpdate(report);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}
		return report.getId();
	}

	public long delete(ReportDesign report) throws CruxException {
		long id = report.getId();
		try {
			transaction.begin();

			session.delete(report);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}
		return id;
	}

	public List<ReportDesign> findAll() {
		List<ReportDesign> result = session.createQuery("from ReportDesign")
				.list();
		return result;
	}
}
