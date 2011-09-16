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

import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.util.CruxException;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class ColumnAliasDAO {

	@SessionTarget
	public Session session;
	@TransactionTarget
	Transaction transaction;

	public ColumnAlias findById(Long id) {
		ColumnAlias detail = (ColumnAlias) session.get(ColumnAlias.class, id);
		return detail;
	}

	public long save(ColumnAlias detail) throws CruxException {
		try {
			transaction.begin();
			session.saveOrUpdate(detail);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}
		return detail.getId();
	}

	public long delete(ColumnAlias detail) throws CruxException {
		long id = detail.getId();
		try {
			// deleteAllChildrens(detail);
			transaction.begin();
			session.delete(detail);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}
		return id;
	}

	/*
	 * public void deleteAllChildrens(ColumnAlias detail) throws CruxException {
	 * List<ReportDesign> result = session.createQuery("from ReportDesign")
	 * .list(); Iterator<ReportDesign> iterator = result.iterator(); while
	 * (iterator.hasNext()) { ReportDesign reportDesign = iterator.next(); if
	 * (reportDesign.getColumnAlias().getId() == detail.getId()) {
	 * transaction.begin(); session.delete(reportDesign); transaction.commit();
	 * } } }
	 */

	public List<ColumnAlias> findAll() {
		System.out.println("session is " + session);
		List<ColumnAlias> result = session.createQuery(
				"from ColumnAlias order by id").list();
		return result;
	}
}
