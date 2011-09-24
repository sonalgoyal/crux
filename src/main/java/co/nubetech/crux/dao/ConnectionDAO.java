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

import co.nubetech.crux.model.Connection;
import co.nubetech.crux.util.CruxException;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class ConnectionDAO {

	final static Logger logger = Logger
			.getLogger(co.nubetech.crux.dao.ConnectionDAO.class);

	@SessionTarget
	public Session session;
	@TransactionTarget
	public Transaction transaction;

	public Connection findById(Long id) throws CruxException {
		Connection connection;
		try {
			logger.debug("Id to find connection is: " + id);
			connection = (Connection) session.get(Connection.class, id);
			connection.getId();
		} catch (NullPointerException e) {
			logger.debug(id + " not exists.");
			throw new CruxException(
					"Selected connection does not exists in the database.", e);
		}
		return connection;
	}

	public long save(Connection connection) throws CruxException {
		try {
			transaction.begin();
			session.saveOrUpdate(connection);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}

		return connection.getId();
	}

	public List<Connection> findAll() {
		System.out.println("session is " + session);
		List<Connection> result = session.createQuery(
				"from Connection order by id").list();
		return result;
	}

	public long delete(Connection connection) throws CruxException {
		long id = connection.getId();
		try {
			transaction.begin();
			session.delete(connection);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(
					"This connection is associated with mappings. Please delete them first.",
					e);
		}
		return id;
	}
}
