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

import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.util.CruxException;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class RowAliasDAO {

	@SessionTarget
	public Session session;
	@TransactionTarget
	Transaction transaction;

	public RowAlias findById(Long id) {
		RowAlias rowAlias = (RowAlias) session.get(RowAlias.class, id);
		return rowAlias;
	}

	public long save(RowAlias rowAlias) throws CruxException {
		try {
			transaction.begin();
			session.saveOrUpdate(rowAlias);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}
		return rowAlias.getId();
	}

	public long delete(RowAlias rowAlias) throws CruxException {
		long id = rowAlias.getId();
		try {
			transaction.begin();
			session.delete(rowAlias);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}
		return id;
	}

	public List<RowAlias> findAll() {
		List<RowAlias> result = session
				.createQuery("from RowAlias order by id").list();
		return result;
	}
}
