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

import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.util.CruxException;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class ColumnFilterDAO {

	@SessionTarget
	public Session session;
	@TransactionTarget
	public Transaction transaction;

	public ColumnFilter findById(Long id) {
		ColumnFilter columnFilter = (ColumnFilter) session.get(
				ColumnFilter.class, id);
		return columnFilter;
	}

	public long save(ColumnFilter columnFilter) throws CruxException {
		try {
			transaction.begin();
			session.saveOrUpdate(columnFilter);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}
		return columnFilter.getId();
	}

	public long delete(ColumnFilter columnFilter) throws CruxException {
		long id = columnFilter.getId();
		try {
			// deleteAllChildrens(detail);
			transaction.begin();
			session.delete(columnFilter);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}
		return id;
	}

	public List<ColumnFilter> findAll() {
		List<ColumnFilter> result = session.createQuery("from ColumnFilter")
				.list();
		return result;
	}
}
