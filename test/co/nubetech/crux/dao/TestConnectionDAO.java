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

import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.ConnectionProperty;
import co.nubetech.crux.model.Datastore;
import co.nubetech.crux.model.User;
import co.nubetech.crux.util.CruxConstants;
import co.nubetech.crux.util.CruxException;

public class TestConnectionDAO extends DBConnection {

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.dao.TestConnectionDAO.class);

	@Test
	public void testUpdateWhenMappingExists() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into connectionProperty values(99999,99999,'propertyTest','valueTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999, 1, 'cfTest','qualTest', 'aliasTest')");

		ConnectionDAO connectionDAO = new ConnectionDAO();
		try {
		connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		Connection connection = connectionDAO.findById(99999l);
		ConnectionProperty connectionProperty = connection.getProperties().get(
				"propertyTest");
		assertEquals(connectionProperty.getValue(), "valueTest");

		connectionProperty.setValue("updateValueTest");

		connectionDAO.transaction = connectionDAO.session.getTransaction();
		// connectionDAO.transaction.begin();
		connectionDAO.save(connection);
		// connectionDAO.transaction.commit();

		ResultSet rsOfConnectionProperty = stmt
				.executeQuery("select value from connectionProperty where id=" + 99999);
		while (rsOfConnectionProperty.next()) {
			assertEquals("updateValueTest", rsOfConnectionProperty.getString(1));
		}
		rsOfConnectionProperty.close();
		} finally {
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connectionProperty where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);

		connectionDAO.session.close();
		stmt.close();
		}
	}

	@Test
	public void testUpdateOnlyProperties() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into connectionProperty values(99999,99999,'propertyTest','valueTest')");

		ConnectionDAO connectionDAO = new ConnectionDAO();
		try {
		connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		Connection connection = connectionDAO.findById(99999l);
		ConnectionProperty connectionProperty = connection.getProperties().get(
				"propertyTest");
		assertEquals(connectionProperty.getValue(), "valueTest");

		connectionProperty.setValue("updateValueTest");

		connectionDAO.transaction = connectionDAO.session.getTransaction();
		// connectionDAO.transaction.begin();
		connectionDAO.save(connection);
		// connectionDAO.transaction.commit();

		ResultSet rsOfConnectionProperty = stmt
				.executeQuery("select value from connectionProperty where id=" + 99999);
		while (rsOfConnectionProperty.next()) {
			assertEquals("updateValueTest", rsOfConnectionProperty.getString(1));
		}
		rsOfConnectionProperty.close();
		} finally {
		stmt.executeUpdate("delete from connectionProperty where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);

		connectionDAO.session.close();
		stmt.close();
		}
	}

	@Test
	public void testUpdateOnlyConnection() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into connectionProperty values(99999,99999,'propertyTest','valueTest')");

		ConnectionDAO connectionDAO = new ConnectionDAO();
		try {
		connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		connectionDAO.transaction = connectionDAO.session.getTransaction();
		// connectionDAO.transaction.begin();

		Connection connection = connectionDAO.findById(99999l);
		connection.setName("updateConnection");
		connectionDAO.save(connection);
		// connectionDAO.transaction.commit();

		ResultSet rsOfConnection = stmt
				.executeQuery("select name from connection where id=" + 99999);
		while (rsOfConnection.next()) {
			assertEquals("updateConnection", rsOfConnection.getString(1));
		}
		rsOfConnection.close();
		} finally {
		stmt.executeUpdate("delete from connectionProperty where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);

		connectionDAO.session.close();
		stmt.close();
		}
	}

	@Test
	public void testUpdateBothConnectionAndProperties() throws Exception,
			CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into connectionProperty values(99999,99999,'propertyTest','valueTest')");

		ConnectionDAO connectionDAO = new ConnectionDAO();
		try {
		connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		ConnectionPropertyDAO hostPropertyDAO = new ConnectionPropertyDAO();
		hostPropertyDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Connection connection = connectionDAO.findById(99999l);
		connection.setName("updateConnection");

		ConnectionProperty connectionProperty = connection.getProperties().get(
				"propertyTest");
		assertEquals(connectionProperty.getValue(), "valueTest");

		connectionProperty.setValue("updateValueTest");

		connectionDAO.transaction = connectionDAO.session.getTransaction();
		// connectionDAO.transaction.begin();
		connectionDAO.save(connection);
		// connectionDAO.transaction.commit();

		ResultSet rsOfConnection = stmt
				.executeQuery("select name from connection where id=" + 99999);
		while (rsOfConnection.next()) {
			assertEquals("updateConnection", rsOfConnection.getString(1));
		}
		rsOfConnection.close();

		ResultSet rsOfConnectionProperty = stmt
				.executeQuery("select value from connectionProperty where id=" + 99999);
		while (rsOfConnectionProperty.next()) {
			assertEquals("updateValueTest", rsOfConnectionProperty.getString(1));
		}
		rsOfConnectionProperty.close();
		} finally {
		stmt.executeUpdate("delete from connectionProperty where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);

		connectionDAO.session.close();
		stmt.close();
		}
	}

	@Test
	public void testGetConnection() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into connectionProperty values(99999,99999,'propertyTest','valueTest')");

		ConnectionDAO connectionDAO = new ConnectionDAO();
		try {
		connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Connection resultConn = connectionDAO.findById(99999l);
		assertEquals(resultConn.getName(), "connectionTest");
		assertEquals(resultConn.getDatastore().getId(), 1l);
		assertEquals(resultConn.getUser().getId(), 1l);

		Map<String, ConnectionProperty> properties = resultConn.getProperties();
		ConnectionProperty connPorperty = properties.get("propertyTest");

		assertEquals(connPorperty.getId(), 99999l);
		assertEquals(connPorperty.getConnection().getId(), 99999l);
		assertEquals(connPorperty.getProperty(), "propertyTest");
		assertEquals(connPorperty.getValue(), "valueTest");
		} finally{
		stmt.executeUpdate("delete from connectionProperty where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		connectionDAO.session.close();
		stmt.close();
		}
	}

	@Test
	public void testDeleteConnection() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into connectionProperty values(99999,99999,'propertyTest','valueTest')");

		ConnectionDAO connectionDAO = new ConnectionDAO();
		try {
		connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		connectionDAO.transaction = connectionDAO.session.getTransaction();
		// connectionDAO.transaction.begin();

		Connection connectionToDelete = connectionDAO.findById(99999l);

		long connectionDeletedForId = connectionDAO.delete(connectionToDelete);
		// connectionDAO.transaction.commit();

		// assertEquals(null, connectionDAO.findById(99999l));
		assertEquals(connectionDeletedForId, 99999);

		ResultSet rsOfConnectionDeleted = stmt
				.executeQuery("select id from connection where id=" + 99999);
		assertEquals(rsOfConnectionDeleted.next(), false);
		rsOfConnectionDeleted.close();

		ResultSet rsOfConnectionPropertyDeleted = stmt
				.executeQuery("select id from connectionProperty where id=" + 99999);
		assertEquals(rsOfConnectionPropertyDeleted.next(), false);
		rsOfConnectionPropertyDeleted.close();
		} finally {
		connectionDAO.session.close();
		stmt.close();
		}
	}

	@Test
	public void testAddProperty() {
		ConnectionProperty property = new ConnectionProperty();
		Connection connection = new Connection();

		property.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
		property.setValue("testHostValue");
		connection.addProperty(property);
		connection.setName("testConnection");

		assertEquals(property.getConnection().getName(), "testConnection");
		assertEquals(
				connection.getProperties()
						.get(CruxConstants.HBASE_ZOOKEEPER_PROPERTY).getValue(),
				"testHostValue");

	}

	@Test
	public void testSaveConnection() throws Exception, CruxException {
		Statement stmt = getStatement();

		DatastoreDAO datastoreDAO = new DatastoreDAO();
		UserDAO userDAO = new UserDAO();
		ConnectionDAO connectionDAO = new ConnectionDAO();
		long connectionId = 0;
		long hostPropertyId = 0;
		try {
		connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		userDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		datastoreDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		ConnectionProperty property = new ConnectionProperty();
		Datastore datastore = new Datastore();
		User user = new User();
		Connection connection = new Connection();

		property.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
		property.setValue("testHostValue");

		connection.addProperty(property);
		user = userDAO.findById(CruxConstants.DEFAULT_USER_ID);
		connection.setUser(user);
		datastore = datastoreDAO.findById(1l);
		connection.setDatastore(datastore);
		connection.setName("testConnection");

		connectionDAO.transaction = connectionDAO.session.getTransaction();
		// connectionDAO.transaction.begin();
		 connectionId = connectionDAO.save(connection);
		// connectionDAO.transaction.commit();
		 hostPropertyId = connection.getProperties()
				.get(CruxConstants.HBASE_ZOOKEEPER_PROPERTY).getId();
		ResultSet rsOfConnection = stmt
				.executeQuery("select * from connection where id = "
						+ connectionId);
		while (rsOfConnection.next()) {
			assertEquals(rsOfConnection.getString("name"), "testConnection");
			assertEquals(rsOfConnection.getLong("datastoreId"), 1);
			assertEquals(rsOfConnection.getLong("userId"),
					CruxConstants.DEFAULT_USER_ID);
		}
		rsOfConnection.close();

		ResultSet rsOfHostProperty = stmt
				.executeQuery("select * from connectionProperty where id = "
						+ hostPropertyId);
		while (rsOfHostProperty.next()) {
			assertEquals(rsOfHostProperty.getLong("connectionId"), connectionId);
			assertEquals(rsOfHostProperty.getString("property"),
					CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
			assertEquals(rsOfHostProperty.getString("value"), "testHostValue");
		}
		rsOfHostProperty.close();
		} finally {
		stmt.executeUpdate("delete from connectionProperty where id="
				+ hostPropertyId);
		stmt.executeUpdate("delete from connection where id=" + connectionId);
		connectionDAO.session.close();
		stmt.close();
		}

	}

	@Test
	public void testDeleteConnectionWhenChildrenExists() throws Exception,
			CruxException {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into connectionProperty values(99999,99999,'propertyTest','valueTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'cf','qual','alias')");

		ConnectionDAO connectionDAO = new ConnectionDAO();
		connectionDAO.session = session;
		connectionDAO.transaction = connectionDAO.session.getTransaction();
		// connectionDAO.transaction.begin();
		try{
		Connection connectionToDelete = connectionDAO.findById(99999l);
		logger.debug("ConnectionId: " + connectionToDelete.getId());
		boolean exceptionExists = false;
		try {
			long connectionDeletedForId = connectionDAO
					.delete(connectionToDelete);
		} catch (CruxException e) {
			exceptionExists = true;
		}

		assertTrue(exceptionExists);
		} finally {
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connectionProperty where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		connectionDAO.session.close();
		stmt.close();
		}
	}

	@Test
	public void testFindByIdWhenIdNotExist() throws Exception, CruxException {
		ConnectionDAO connectionDAO = new ConnectionDAO();
		connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		connectionDAO.transaction = connectionDAO.session.getTransaction();
		boolean connectionNotExist = false;
		try {
			Connection connection = connectionDAO.findById(99999l);
		} catch (CruxException e) {
			connectionNotExist = true;
		}
		assertTrue(connectionNotExist);
		connectionDAO.session.close();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWrongId() throws Exception, CruxException {
		ConnectionDAO connectionDAO = new ConnectionDAO();
		connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		boolean connectionNotExist = false;
		Connection connection = null;
		try {
			connection = connectionDAO.findById(99999l);
		} catch (CruxException e) {
			connectionNotExist = true;
		}
		assertTrue(connectionNotExist);

		connectionDAO.transaction = connectionDAO.session.getTransaction();
		connectionDAO.save(connection);
	}

	@Test(expected = NullPointerException.class)
	public void testDeleteWrongId() throws Exception, CruxException {
		ConnectionDAO connectionDAO = new ConnectionDAO();
		connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		boolean connectionNotExist = false;
		Connection connection = null;
		try {
			connection = connectionDAO.findById(99999l);
		} catch (CruxException e) {
			connectionNotExist = true;
		}
		assertTrue(connectionNotExist);
		connectionDAO.transaction = connectionDAO.session.getTransaction();
		connectionDAO.delete(connection);

	}

	@Test
	public void testSaveConnectionForNullValues() throws Exception {

		boolean exceptionExists = false;
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		ConnectionDAO connectionDAO = new ConnectionDAO();
		Connection connection = new Connection();
		UserDAO userDAO = new UserDAO();
		userDAO.session = session;
		userDAO.transaction = userDAO.session.getTransaction();
		connection.setUser(userDAO.findById(1l));
		DatastoreDAO datastoreDAO = new DatastoreDAO();
		datastoreDAO.session = session;
		datastoreDAO.transaction = datastoreDAO.session.getTransaction();
		connection.setDatastore(datastoreDAO.findById(1l));
		connection.setName(null);
		ConnectionProperty connectionProperty = new ConnectionProperty();
		connectionProperty.setProperty("hbase.rest");
		connectionProperty.setValue("localhost");
		connection.addProperty(connectionProperty);

		connectionDAO.session = session;
		connectionDAO.transaction = connectionDAO.session.getTransaction();
		try {
			connectionDAO.save(connection);
		} catch (CruxException e) {
			exceptionExists = true;
		}

		assertTrue(exceptionExists);

		session.close();
	}

	@Test
	public void testSaveConnectionForDuplicateObjects() throws Exception {
		boolean exceptionExists = false;
		Statement stmt = getStatement();

		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		ConnectionDAO connectionDAO = new ConnectionDAO();
		try {
		Connection connection = new Connection();
		UserDAO userDAO = new UserDAO();
		userDAO.session = session;
		userDAO.transaction = userDAO.session.getTransaction();
		connection.setUser(userDAO.findById(1l));
		DatastoreDAO datastoreDAO = new DatastoreDAO();
		datastoreDAO.session = session;
		datastoreDAO.transaction = datastoreDAO.session.getTransaction();
		connection.setDatastore(datastoreDAO.findById(1l));
		connection.setName("connectionTest");
		connectionDAO.session = session;
		connectionDAO.transaction = connectionDAO.session.getTransaction();
		try {
			connectionDAO.save(connection);
		} catch (CruxException e) {
			exceptionExists = true;
		}

		assertTrue(exceptionExists);
		} finally {
		stmt.executeUpdate("delete from connection where id=" + 99999);
		connectionDAO.session.close();
		stmt.close();
		}
	}

	@Test
	public void testFindById() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(999999,1,1,'connectionTest')");

		ConnectionDAO connectionDAO = new ConnectionDAO();
		connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		connectionDAO.transaction = connectionDAO.session.getTransaction();
		try {
		Connection connection = connectionDAO.findById(999999l);
		logger.debug("ConnectionId: " + connection.getId());
		assertEquals(999999, connection.getId());
		} finally {
		stmt.executeUpdate("delete from connection where id=" + 999999);
		connectionDAO.session.close();
		stmt.close();
		}
	}

}
