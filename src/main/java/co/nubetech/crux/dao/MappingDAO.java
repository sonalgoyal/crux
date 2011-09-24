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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.util.CruxException;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class MappingDAO {

	@SessionTarget
	public Session session;
	@TransactionTarget
	public Transaction transaction;

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.dao.MappingDAO.class);

	public Mapping findById(Long id) throws CruxException {
		Mapping mapping;
		try {
			mapping = (Mapping) session.get(Mapping.class, id);
			mapping.getId();
		} catch (NullPointerException e) {
			throw new CruxException(
					"Selected mapping does not exists in the database.", e);
		}
		return mapping;
	}

	public long delete(Mapping mapping) throws CruxException {
		long id = mapping.getId();
		try {
			transaction.begin();
			session.delete(mapping);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(
					"This mapping is associated with reports. Please delete them first.",
					e);
		}
		return id;
	}

	public long save(Mapping mapping) throws CruxException {
		if (mapping != null) {
			long id = mapping.getId();
			if (id != 0) {
				Mapping foundMapping = this.findById(id);
				logger.debug("foundMapping: " + foundMapping);
				foundMapping = updateRowAlias(mapping, foundMapping);
				foundMapping = updateColumnAlias(mapping, foundMapping);
				foundMapping.setName(mapping.getName());
				return saveMapping(foundMapping);
			}
			return saveMapping(mapping);
		} else {
			return 0;
		}
	}

	private Mapping updateColumnAlias(Mapping mapping, Mapping foundMapping)
			throws CruxException {
		Map<String, ColumnAlias> oldColumnAliasMap = new HashMap<String, ColumnAlias>(
				foundMapping.getColumnAlias());
		Map<String, ColumnAlias> newColumnAliasMap = new HashMap<String, ColumnAlias>(
				mapping.getColumnAlias());

		Iterator<ColumnAlias> iteratorForOldColumnAlias = oldColumnAliasMap
				.values().iterator();
		Iterator<ColumnAlias> iteratorForNewColumnAlias = newColumnAliasMap
				.values().iterator();

		// Delete non-existing column alias.
		while (iteratorForOldColumnAlias.hasNext()) {
			ColumnAlias oldColumnAlias = iteratorForOldColumnAlias.next();
			Iterator<ColumnAlias> iteratorForNewColumnAlias1 = newColumnAliasMap
					.values().iterator();
			boolean columnAliasExists = false;
			while (iteratorForNewColumnAlias1.hasNext()) {
				ColumnAlias newColumnAlias = iteratorForNewColumnAlias1.next();
				if (newColumnAlias.getId() == oldColumnAlias.getId()) {
					columnAliasExists = true;
				}
			}

			logger.debug("ColumnAliasExists: " + columnAliasExists);

			if (!columnAliasExists) {
				logger.debug("Removing ColumnAlias: " + oldColumnAlias);
				foundMapping.getColumnAlias().remove(oldColumnAlias.getAlias());
				try {
					transaction.begin();
					session.delete(oldColumnAlias);
					transaction.commit();
				} catch (JDBCException e) {
					transaction.rollback();
					throw new CruxException(e.getSQLException().getMessage(), e);
				}
			}

		}

		// Add new column alias and update old rowAlias.
		while (iteratorForNewColumnAlias.hasNext()) {
			ColumnAlias columnAlias = iteratorForNewColumnAlias.next();
			logger.debug("columnAlias: " + columnAlias);
			if (columnAlias.getId() == 0l) {
				foundMapping.addColumnAlias(columnAlias);
			} else {
				long columnAliasId = columnAlias.getId();
				String alias = columnAlias.getAlias();
				ValueType columnAliasColumnType = columnAlias.getValueType();
				String columnAliasCF = columnAlias.getColumnFamily();
				String columnAliasQualifier = columnAlias.getQualifier();

				Iterator<ColumnAlias> iteratorForOldColumnAlias1 = oldColumnAliasMap
						.values().iterator();

				while (iteratorForOldColumnAlias1.hasNext()) {
					ColumnAlias oldColumnAlias = iteratorForOldColumnAlias1
							.next();
					if (oldColumnAlias.getId() == columnAliasId) {
						oldColumnAlias.setAlias(alias);
						oldColumnAlias.setValueType(columnAliasColumnType);
						oldColumnAlias.setColumnFamily(columnAliasCF);
						oldColumnAlias.setQualifier(columnAliasQualifier);
					}
				}
			}
		}

		return foundMapping;

	}

	private Mapping updateRowAlias(Mapping mapping, Mapping foundMapping)
			throws CruxException {
		Map<String, RowAlias> oldRowAliasMap = new HashMap<String, RowAlias>(
				foundMapping.getRowAlias());
		Map<String, RowAlias> newRowAliasMap = new HashMap<String, RowAlias>(
				mapping.getRowAlias());

		Iterator<RowAlias> iteratorForOldRowAlias = oldRowAliasMap.values()
				.iterator();
		Iterator<RowAlias> iteratorForNewRowAlias = newRowAliasMap.values()
				.iterator();

		// Delete non-existing row alias.
		while (iteratorForOldRowAlias.hasNext()) {
			RowAlias oldRowAlias = iteratorForOldRowAlias.next();
			Iterator<RowAlias> iteratorForNewRowAlias1 = newRowAliasMap
					.values().iterator();
			boolean rowAliasExists = false;
			while (iteratorForNewRowAlias1.hasNext()) {
				RowAlias newRowAlias = iteratorForNewRowAlias1.next();
				if (newRowAlias.getId() == oldRowAlias.getId()) {
					rowAliasExists = true;
				}
			}

			logger.debug("rowAliasExists: " + rowAliasExists);

			if (!rowAliasExists) {
				logger.debug("Removing rowAlias: " + oldRowAlias);
				foundMapping.getRowAlias().remove(oldRowAlias.getAlias());
				try {
					transaction.begin();
					session.delete(oldRowAlias);
					transaction.commit();
				} catch (JDBCException e) {
					transaction.rollback();
					throw new CruxException(e.getSQLException().getMessage(), e);
				}
			}

		}

		// Add new row alias and update old rowAlias.
		while (iteratorForNewRowAlias.hasNext()) {
			RowAlias rowAlias = iteratorForNewRowAlias.next();
			logger.debug("rowAlias: " + rowAlias);
			if (rowAlias.getId() == 0l) {
				foundMapping.addRowAlias(rowAlias);
			} else {
				long rowAliasId = rowAlias.getId();
				String alias = rowAlias.getAlias();
				ValueType rowAliasColumnType = rowAlias.getValueType();
				Integer rowAliasLength = rowAlias.getLength();

				Iterator<RowAlias> iteratorForOldRowAlias1 = oldRowAliasMap
						.values().iterator();

				while (iteratorForOldRowAlias1.hasNext()) {
					RowAlias oldRowAlias = iteratorForOldRowAlias1.next();
					if (oldRowAlias.getId() == rowAliasId) {
						oldRowAlias.setAlias(alias);
						oldRowAlias.setValueType(rowAliasColumnType);
						oldRowAlias.setLength(rowAliasLength);
					}
				}
			}
		}
		return foundMapping;
	}

	public long saveMapping(Mapping mapping) throws CruxException {
		try {
			logger.debug("Session is: " + session);
			transaction.begin();
			session.saveOrUpdate(mapping);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}
		return mapping.getId();
	}

	public List<Mapping> findAll() {
		System.out.println("session in findAll is: " + session);
		List<Mapping> result = session.createQuery("from Mapping order by id")
				.list();
		return result;
	}
}
