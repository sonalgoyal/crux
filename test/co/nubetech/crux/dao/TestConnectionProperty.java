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

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.junit.Test;

import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.ConnectionProperty;
import co.nubetech.crux.util.CruxConstants;
import co.nubetech.crux.util.CruxException;

public class TestConnectionProperty extends DBConnection {

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.dao.TestConnectionProperty.class);

	@Test
	public void testUpdate() throws Exception, CruxException {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into connectionProperty values(99999,99999,'propertyTest','valueTest')");

		ConnectionPropertyDAO connectionPropertyDAO = new ConnectionPropertyDAO();

		connectionPropertyDAO.session = session;
		ConnectionProperty hostProperty = connectionPropertyDAO
				.findById(99999l);

		connectionPropertyDAO.transaction = connectionPropertyDAO.session
				.getTransaction();
		// connectionPropertyDAO.transaction.begin();
		hostProperty.setValue("updateValueTest");
		connectionPropertyDAO.save(hostProperty);
		// connectionPropertyDAO.transaction.commit();

		ResultSet rsOfConnectionProperty = stmt
				.executeQuery("select value from connectionProperty where id=" + 99999);
		while (rsOfConnectionProperty.next()) {
			assertEquals("updateValueTest", rsOfConnectionProperty.getString(1));
		}
		rsOfConnectionProperty.close();

		stmt.executeUpdate("delete from connectionProperty where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);

		session.close();
		closeConnection();
	}

	@Test
	public void testSave() throws Exception, CruxException {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");

		ConnectionDAO connectionDAO = new ConnectionDAO();
		connectionDAO.session = session;
		Connection connection = connectionDAO.findById(99999l);

		ConnectionProperty property = new ConnectionProperty();
		property.setConnection(connection);
		property.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
		property.setValue("valueTest");

		ConnectionPropertyDAO propertyDAO = new ConnectionPropertyDAO();
		propertyDAO.session = session;
		propertyDAO.transaction = propertyDAO.session.getTransaction();
		// propertyDAO.transaction.begin();
		long propertyId = propertyDAO.save(property);
		// propertyDAO.transaction.commit();

		ResultSet rsOfConnectionProperty = stmt
				.executeQuery("select * from connectionProperty where id="
						+ propertyId);
		while (rsOfConnectionProperty.next()) {
			assertEquals(99999, rsOfConnectionProperty.getLong("connectionId"));
			assertEquals(CruxConstants.HBASE_ZOOKEEPER_PROPERTY,
					rsOfConnectionProperty.getString("property"));
			assertEquals("valueTest", rsOfConnectionProperty.getString("value"));
		}
		rsOfConnectionProperty.close();

		stmt.executeUpdate("delete from connectionProperty where id="
				+ propertyId);
		stmt.executeUpdate("delete from connection where id=" + 99999);

		session.close();
		closeConnection();
	}

	@Test
	public void testDelete() throws Exception, CruxException {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into connectionProperty values(99999,99999,'propertyTest','valueTest')");

		ConnectionPropertyDAO propertyDAO = new ConnectionPropertyDAO();
		propertyDAO.session = session;
		ConnectionProperty property = propertyDAO.findById(99999l);

		propertyDAO.transaction = propertyDAO.session.getTransaction();
		// propertyDAO.transaction.begin();
		long propertyId = propertyDAO.delete(property);
		// propertyDAO.transaction.commit();

		assertEquals(99999, propertyId);
		ResultSet rsOfConnectionPropertyDeleted = stmt
				.executeQuery("select id from connectionProperty where id=" + 99999);
		assertEquals(rsOfConnectionPropertyDeleted.next(), false);
		rsOfConnectionPropertyDeleted.close();

		stmt.executeUpdate("delete from connection where id=" + 99999);

		session.close();
		closeConnection();
	}

	@Test
	public void testSaveWhenParentIsDeleted() throws Exception, CruxException {
		boolean exceptionExists = false;
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");

		ConnectionDAO connectionDAO = new ConnectionDAO();
		connectionDAO.session = session;
		connectionDAO.transaction = connectionDAO.session.getTransaction();
		Connection connection = connectionDAO.findById(99999l);
		try {
			connectionDAO.delete(connection);
		} catch (CruxException e) {
		}

		ConnectionProperty property = new ConnectionProperty();
		property.setConnection(connection);
		property.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
		property.setValue("valueTest");

		ConnectionPropertyDAO propertyDAO = new ConnectionPropertyDAO();
		propertyDAO.session = session;
		propertyDAO.transaction = propertyDAO.session.getTransaction();
		try {
			propertyDAO.save(property);
		} catch (CruxException e) {
			exceptionExists = true;
		}
		assertTrue(exceptionExists);

		session.close();
		closeConnection();
	}

}
