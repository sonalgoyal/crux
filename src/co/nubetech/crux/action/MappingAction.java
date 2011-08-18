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

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.log4j.Logger;

import co.nubetech.crux.dao.ConnectionDAO;
import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.dao.ReportDesignDAO;
import co.nubetech.crux.dao.RowFilterDAO;
import co.nubetech.crux.dao.ValueTypeDAO;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.server.HBaseFacade;
import co.nubetech.crux.util.CruxError;
import co.nubetech.crux.util.CruxException;
import co.nubetech.crux.view.ColumnAliasView;
import co.nubetech.crux.view.MappingView;
import co.nubetech.crux.view.RowAliasView;

public class MappingAction extends CruxAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.MappingAction.class);

	private ConnectionDAO connectionDAO = new ConnectionDAO();
	private ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
	private MappingDAO mappingDAO = new MappingDAO();
	private ReportDesignDAO reportDesignDAO = new ReportDesignDAO();
	private RowFilterDAO rowFilterDAO = new RowFilterDAO();

	private Connection connection = new Connection();
	private ValueType valueType = new ValueType();
	private Mapping mapping = new Mapping();
	private ColumnAlias columnAlias = new ColumnAlias();
	private RowAlias rowAlias = new RowAlias();

	private List<Connection> connections = new ArrayList<Connection>();
	private List<ValueType> columnTypeList = new ArrayList<ValueType>();
	private String[] tableList;
	private ArrayList<String> columnFamilyList = new ArrayList<String>();
	private List<ColumnAliasView> columnAliasViewList = new ArrayList<ColumnAliasView>();
	private List<RowAliasView> rowAliasViewList = new ArrayList<RowAliasView>();
	private List<MappingView> mappingViewList = new ArrayList<MappingView>();

	public CruxError getError() {
		return error;
	}

	public void setError(CruxError error) {
		this.error = error;
	}

	public RowAlias getRowAlias() {
		return rowAlias;
	}

	public List<RowAliasView> getRowAliasViewList() {
		return rowAliasViewList;
	}

	public void setRowAlias(RowAlias rowAlias) {
		this.rowAlias = rowAlias;
	}

	public void setRowAliasViewList(List<RowAliasView> rowAliasViewList) {
		this.rowAliasViewList = rowAliasViewList;
	}

	public List<MappingView> getMappingViewList() {
		return mappingViewList;
	}

	public void setMappingViewList(List<MappingView> mappingViewList) {
		this.mappingViewList = mappingViewList;
	}

	public List<ColumnAliasView> getColumnAliasViewList() {
		return columnAliasViewList;
	}

	public void setColumnAliasViewList(List<ColumnAliasView> columnAliasViewList) {
		this.columnAliasViewList = columnAliasViewList;
	}

	public ValueType getColumnType() {
		return valueType;
	}

	public void setColumnType(ValueType valueType) {
		this.valueType = valueType;
	}

	public ColumnAlias getColumnAlias() {
		return columnAlias;
	}

	public void setColumnAlias(ColumnAlias columnAlias) {
		this.columnAlias = columnAlias;
	}

	public Mapping getMapping() {
		return mapping;
	}

	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}

	public ArrayList<String> getColumnFamilyList() {
		return columnFamilyList;
	}

	public void setColumnFamilyList(ArrayList<String> columnFamilyList) {
		this.columnFamilyList = columnFamilyList;
	}

	public List<ValueType> getColumnTypeList() {
		return columnTypeList;
	}

	public void setColumnTypeList(List<ValueType> columnTypeList) {
		this.columnTypeList = columnTypeList;
	}

	public String[] getTableList() {
		return tableList;
	}

	public Connection getConnection() {
		return connection;
	}

	public List<Connection> getConnections() {
		return connections;
	}

	public void setTableList(String[] tableList) {
		this.tableList = tableList;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}

	public void populateMappingViewList() {
		long index = 0;
		logger.debug("Going to populate MappingView.");
		List<Mapping> mappingList = mappingDAO.findAll();
		logger.debug("MappingList is: " + mappingList);
		for (Mapping mapping : mappingList) {
			logger.debug("Mapping is: " + mapping);
			mappingViewList.add(new MappingView(++index, mapping));
			logger.debug("mappingViewList: " + mappingViewList);
		}
		for (MappingView mappingView : mappingViewList) {
			logger.debug("Connection name is: "
					+ mappingView.getConnectionName());
			logger.debug("MappingName is: " + mappingView.getName());
			logger.debug("Table name is: " + mappingView.getTableName());
		}
		logger.debug("Populated mappingView.");
	}

	public void populateColumnAliasViewList(Mapping mapping) {
		Iterator<ColumnAlias> iterator = mapping.getColumnAlias().values()
				.iterator();
		while (iterator.hasNext()) {
			columnAliasViewList.add(new ColumnAliasView(iterator.next()));
		}
	}

	public void populateRowAliasViewList(Mapping mapping) {
		Iterator<RowAlias> iterator = mapping.getRowAlias().values().iterator();
		while (iterator.hasNext()) {
			rowAliasViewList.add(new RowAliasView(iterator.next()));
		}
	}

	public String initializeMappings() {
		logger.debug("Initing mappings");
		connections = connectionDAO.findAll();
		columnTypeList = valueTypeDAO.findAll();
		populateMappingViewList();
		return SUCCESS;
	}

	public String populateHBaseTable() throws IOException {
		logger.debug("populating hbaseTableList");
		long connectionId = connection.getId();
		try {
			connection = connectionDAO.findById(connectionId);
			HBaseFacade hbaseFacade = getHBaseFacade();
			tableList = hbaseFacade.getTableList(connection);
			logger.debug("tableList.length is: " + tableList.length
					+ " for connection" + connection);
		} catch (CruxException e) {
			error.setMessage(e.getMessage());
		}
		return SUCCESS;
	}

	public String populateColumnFamily() throws IOException {
		long connectionId = connection.getId();
		logger.debug("ConnectionId is: " + connectionId);
		try {
			connection = connectionDAO.findById(connectionId);
			String tableName = mapping.getTableName();
			HBaseFacade hbaseFacade = getHBaseFacade();
			ArrayList<HColumnDescriptor> columnList = new ArrayList<HColumnDescriptor>(
					hbaseFacade.getColumnFamilies(connection, tableName));
			for (HColumnDescriptor hcolumnDescriptor : columnList) {
				columnFamilyList.add(hcolumnDescriptor.getNameAsString());
			}
		} catch (CruxException e) {
			error.setMessage(e.getMessage());
		}
		return SUCCESS;
	}

	public ValueType getColumnType(String columnTypeName) {
		List<ValueType> columnTypeList = valueTypeDAO.findAll();
		for (ValueType colType : columnTypeList) {
			if (colType.getName().equals(columnTypeName)) {
				valueType = colType;
			}
		}
		return valueType;
	}

	public String saveMapping() {
		logger.debug("MappingId is: " + mapping.getId());
		long connectionId = connection.getId();
		logger.debug("ConnectionId is: " + connectionId);

		try {
			if (connectionId != 0l) {
				logger.debug("Finding connection.");
				connection = connectionDAO.findById(connectionId);
				mapping.setConnection(connection);
			}

			for (int i = 0; i < columnAliasViewList.size(); i++) {
				columnAlias = new ColumnAlias();
				logger.debug("ColumnAlias id is: "
						+ columnAliasViewList.get(i).getId());
				columnAlias.setId(columnAliasViewList.get(i).getId());
				columnAlias.setAlias(columnAliasViewList.get(i).getAlias());
				columnAlias.setColumnFamily(columnAliasViewList.get(i)
						.getColumnFamily());
				columnAlias.setQualifier(columnAliasViewList.get(i)
						.getQualifier());
				columnAlias.setValueType(getColumnType(columnAliasViewList.get(
						i).getColumnTypeName()));
				mapping.addColumnAlias(columnAlias);
			}
			logger.debug("Size of ColumnAlias is: "
					+ mapping.getColumnAlias().size());

			for (int i = 0; i < rowAliasViewList.size(); i++) {
				rowAlias = new RowAlias();
				rowAlias.setId(rowAliasViewList.get(i).getId());
				rowAlias.setAlias(rowAliasViewList.get(i).getAlias());
				logger.debug("RowKeyAlias length is: "
						+ rowAliasViewList.get(i).getLength());
				rowAlias.setLength(rowAliasViewList.get(i).getLength());
				rowAlias.setValueType(getColumnType(rowAliasViewList.get(i)
						.getColumnTypeName()));
				mapping.addRowAlias(rowAlias);
			}
			logger.debug("Size of rowAlias is: " + mapping.getRowAlias().size());

			try {
				mappingDAO.save(mapping);
			} catch (CruxException e) {
				error.setMessage(e.getMessage());
			} catch (Exception e) {
				error.setMessage("Something Wrong has happened");
				e.printStackTrace();
			}
			logger.debug("Mapping saved.");
			populateMappingViewList();
			// populateColumnAliasViewList(mapping);
		} catch (CruxException e) {
			error.setMessage(e.getMessage());
		}
		return SUCCESS;
	}

	public String editMapping() throws IOException {
		try {
			mapping = mappingDAO.findById(mapping.getId());
		} catch (CruxException e) {
			error.setMessage(e.getMessage());
		}
		populateColumnAliasViewList(mapping);
		populateRowAliasViewList(mapping);
		connection = mapping.getConnection();
		populateColumnFamily();
		return SUCCESS;
	}

	public String deleteMapping() {
		logger.debug("Deleting mapping id: " + mapping.getId());
		try {
			mapping = mappingDAO.findById(mapping.getId());
			mappingDAO.delete(mapping);
		} catch (CruxException e) {
			error.setMessage(e.getMessage());
		} catch (Exception e) {
			error.setMessage("Something Wrong has happened");
			e.printStackTrace();
		}
		populateMappingViewList();
		logger.debug("Returning Success.");
		return SUCCESS;
	}

	public String checkRowAliasForForeignKey() {
		long rowAliasId = rowAlias.getId();
		logger.debug("RowAlias id is: " + rowAliasId);
		List<ReportDesign> reportDesignList = reportDesignDAO.findAll();
		for (ReportDesign reportDesign : reportDesignList) {
			RowAlias rowAliasToCheck = reportDesign.getRowAlias();
			if (rowAliasToCheck != null) {
				if (rowAliasToCheck.getId() == rowAliasId) {
					error.setMessage("This alias is in use. Can't delete or update this alias.");
				}
			}
		}

		List<RowAliasFilter> rowFilterList = rowFilterDAO.findAll();
		for (RowAliasFilter rowFilter : rowFilterList) {
			if (rowFilter.getRowAlias().getId() == rowAliasId) {
				error.setMessage("This alias is in use. Can't delete or update this alias.");
			}
		}

		return SUCCESS;
	}

	public String checkColumnAliasForForeignKey() {
		long columnAliasId = columnAlias.getId();
		logger.debug("ColumnAlias id is: " + columnAliasId);
		List<ReportDesign> reportDesignList = reportDesignDAO.findAll();
		for (ReportDesign reportDesign : reportDesignList) {
			ColumnAlias columnAliasToCheck = reportDesign.getColumnAlias();
			if (columnAliasToCheck != null) {
				if (columnAliasToCheck.getId() == columnAliasId) {
					error.setMessage("This alias is in use. Can't delete or update this alias.");
				}
			}
		}
		return SUCCESS;
	}

}
