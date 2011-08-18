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
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import co.nubetech.crux.dao.ConnectionDAO;
import co.nubetech.crux.dao.DatastoreDAO;
import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.dao.UserDAO;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.ConnectionProperty;
import co.nubetech.crux.model.Datastore;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.User;
import co.nubetech.crux.pool.HBaseConnectionPool;
import co.nubetech.crux.server.HBaseFacade;
import co.nubetech.crux.util.CruxConstants;
import co.nubetech.crux.util.CruxError;
import co.nubetech.crux.util.CruxException;
import co.nubetech.crux.view.ConnectionView;

public class ConnectionAction extends CruxAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.ConnectionAction.class);

	private DatastoreDAO datastoreDAO = new DatastoreDAO();
	private UserDAO userDAO = new UserDAO();
	private ConnectionDAO connectionDAO = new ConnectionDAO();
	private MappingDAO mappingDAO = new MappingDAO();

	private Datastore datastore = new Datastore();
	private User user = new User();
	private Connection connection = new Connection();
	/*
	 * private ConnectionProperty hostProperty = new ConnectionProperty(
	 * CruxConstants.HBASE_HOST_PROPERTY); private ConnectionProperty
	 * portProperty = new ConnectionProperty(
	 * CruxConstants.HBASE_PORT_PROPERTY);
	 */
	private ConnectionProperty hbaseRestServerProperty = new ConnectionProperty(
			CruxConstants.HBASE_ZOOKEEPER_PROPERTY);

	private List<ConnectionView> connectionViewList = new ArrayList<ConnectionView>();

	public ConnectionProperty getHbaseRestServerProperty() {
		return hbaseRestServerProperty;
	}

	public void setHbaseRestServerProperty(
			ConnectionProperty hbaseRestServerProperty) {
		this.hbaseRestServerProperty = hbaseRestServerProperty;
	}

	public List<ConnectionView> getConnectionViewList() {
		return connectionViewList;
	}

	public void setConnectionViewList(List<ConnectionView> connectionViewList) {
		this.connectionViewList = connectionViewList;
	}

	public Connection getConnection() {
		return connection;
	}

	/*
	 * public ConnectionProperty getHostProperty() { return hostProperty; }
	 * 
	 * public ConnectionProperty getPortProperty() { return portProperty; }
	 */

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public CruxError getError() {
		return error;
	}

	public void setError(CruxError error) {
		this.error = error;
	}

	/*
	 * public void setHostProperty(ConnectionProperty hostProperty) {
	 * this.hostProperty = hostProperty; }
	 * 
	 * public void setPortProperty(ConnectionProperty portProperty) {
	 * this.portProperty = portProperty; }
	 */

	public String initializeConnections() {
		long index = 0;
		List<Connection> connections = connectionDAO.findAll();
		logger.debug("Initiating Connection:" + connections.size());
		for (Connection connection : connections) {
			connectionViewList.add(new ConnectionView(++index, connection));
		}
		return SUCCESS;
	}

	public String saveConnection() {
		logger.debug("Connection Id is: " + connection.getId());
		logger.debug("hbaseRestServerProperty property is: "
				+ hbaseRestServerProperty.getProperty());
		logger.debug("hbaseRestServerProperty value is: "
				+ hbaseRestServerProperty.getValue());
		connection.addProperty(hbaseRestServerProperty);
		user = userDAO.findById(CruxConstants.DEFAULT_USER_ID);
		connection.setUser(user);
		datastore = datastoreDAO.findById(CruxConstants.DEFAULT_DATASTORE_ID);
		connection.setDatastore(datastore);

		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HBaseConnectionPool pool = (HBaseConnectionPool) servletContext
				.getAttribute(CruxConstants.HBASE_POOL);

		try {
			pool.save(connection);
			// servletContext.setAttribute(CruxConstants.HBASE_POOL, pool);
			HBaseFacade hbaseFacade = getHBaseFacade();
			boolean isValidConnection = hbaseFacade
					.isValidConnection(connection);
			logger.debug("isValidConnection : " + isValidConnection);
			if (isValidConnection) {
				logger.debug("connection is saving.");
				connectionDAO.save(connection);
			} else {
				error.setMessage("This connection is not valid.");
				try {
					pool.delete(connection);
					// servletContext.setAttribute(CruxConstants.HBASE_POOL,
					// pool);
				} catch (Exception e1) {
					e1.printStackTrace();
					error.setMessage(e1.getMessage());
				}
			}
		} catch (CruxException e) {
			error.setMessage(e.getMessage());
		} catch (Exception e) {
			error.setMessage("Something Wrong has happened");
		}
		initializeConnections(); // for dataGrid updation.
		return SUCCESS;
	}

	public String deleteConnection() {
		long connectionId = connection.getId();
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HBaseConnectionPool pool = (HBaseConnectionPool) servletContext
				.getAttribute(CruxConstants.HBASE_POOL);

		try {
			connection = connectionDAO.findById(connectionId);
			logger.debug("Deleting Connection for connectionId: "
					+ connectionId);
			connectionDAO.delete(connection);
			pool.delete(connection);
			// servletContext.setAttribute(CruxConstants.HBASE_POOL, pool);

		} catch (CruxException e) {
			error.setMessage(e.getMessage());
		} catch (Exception e) {
			error.setMessage("Something Wrong has happened");
			e.printStackTrace();
		}
		initializeConnections(); // for dataGrid updation.
		return SUCCESS;
	}

	public String updateConnection() throws IOException {
		String connectionName = connection.getName();
		String hbaseRestServerPropertyValue = hbaseRestServerProperty
				.getValue();
		logger.debug("Updating connectionId: " + connection.getId());
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HBaseConnectionPool pool = (HBaseConnectionPool) servletContext
				.getAttribute(CruxConstants.HBASE_POOL);

		Connection connectionToTest = new Connection();
		ConnectionProperty property = new ConnectionProperty();
		property.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
		property.setValue(hbaseRestServerPropertyValue);
		connectionToTest.addProperty(property);

		try {
			HBaseFacade hbaseFacade = getHBaseFacade();
			boolean isValidConnection = hbaseFacade
					.isValidConnection(connectionToTest);
			logger.debug("isValidConnection : " + isValidConnection);
			if (isValidConnection) {
				connection = connectionDAO.findById(connection.getId());
				Connection connectionToUpdate = connection;
				connection.setName(connectionName);
				hbaseRestServerProperty = connection.getProperties().get(
						CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
				hbaseRestServerProperty.setValue(hbaseRestServerPropertyValue);

				pool.update(connectionToUpdate, connection);

				// Now checking whether this connection holds all the associated
				// childrens.
				String[] tables = hbaseFacade.getTableList(connection);
				ArrayList<String> tableList = new ArrayList<String>();
				for (int i = 0; i < tables.length; i++) {
					tableList.add(tables[i]);
				}
				long count = 0l;
				for (Mapping mapping : mappingDAO.findAll()) {
					long id = connection.getId();
					if (id == mapping.getConnection().getId()) {
						String tableName = mapping.getTableName();
						if (!tableList.contains(tableName)) {
							count++;
						} else {
							ArrayList<String> columnFamilyList = new ArrayList<String>();
							ArrayList<HColumnDescriptor> columnList = new ArrayList<HColumnDescriptor>(
									hbaseFacade.getColumnFamilies(connection,
											tableName));
							for (HColumnDescriptor hcolumnDescriptor : columnList) {
								columnFamilyList.add(hcolumnDescriptor
										.getNameAsString());
							}
							Iterator<ColumnAlias> iterator = mapping
									.getColumnAlias().values().iterator();
							while (iterator.hasNext()) {
								ColumnAlias columnAlias = iterator.next();
								String columnFamily = columnAlias
										.getColumnFamily();
								if (!columnFamilyList.contains(columnFamily)) {
									count++;
								}
							}
						}

					}
				}

				if (count == 0l) {
					logger.debug("connection is saving.");
					connectionDAO.save(connection);
				} else {
					error.setMessage("Connection can't be updated because it does not contain all "
							+ "the tables and column family of the assosiated mapping.");
					try {
						connectionDAO.transaction.rollback();
						pool.update(connection, connectionToUpdate);
					} catch (Exception e1) {
						e1.printStackTrace();
						error.setMessage(e1.getMessage());
					}
				}
			} else {
				logger.debug("This connection is not valid.");
				error.setMessage("This connection is not valid.");
			}

		} catch (CruxException e) {
			error.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			error.setMessage("Something Wrong has happened");
		}

		initializeConnections(); // for dataGrid updation.
		return SUCCESS;
	}

}
