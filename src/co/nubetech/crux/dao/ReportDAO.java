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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.util.CruxException;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class ReportDAO {

	@SessionTarget
	public Session session;
	@TransactionTarget
	public Transaction transaction;

	public long delete(Report report) throws CruxException {
		long id = 0;
		try {
			id = report.getId();
		} catch (NullPointerException e) {
			throw new CruxException(
					"Selected Report does not exists in the database.", e);
		}
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

	public Report findById(long id) throws CruxException {

		Report report = (Report) session.get(Report.class, id);
		if (report == null) {
			throw new CruxException(
					"Selected Report does not exists in the database.",
					new Throwable());
		}
		return report;
	}

	public long save(Report report) throws CruxException {
		if (report != null) {
			long id = report.getId();
			Report foundReport = null;
			ArrayList<ReportDesign> oldDesignList = null;
			ArrayList<RowAliasFilter> oldRowFilter = null;
			ArrayList<ColumnFilter> oldColumnFilter = null;
			if (id != 0) {
				try {
					foundReport = this.findById(id);
				} catch (CruxException e) {
					throw new CruxException(
							"Report you are trying to edit does not exists in database",
							e);
				}
				oldDesignList = new ArrayList<ReportDesign>(
						foundReport.getDesigns());
				oldRowFilter = new ArrayList<RowAliasFilter>(
						foundReport.getRowAliasFilters());
				oldColumnFilter = new ArrayList<ColumnFilter>(
						foundReport.getColumnFilters());

				foundReport.getDesigns().clear();
				foundReport.getRowAliasFilters().clear();
				foundReport.getColumnFilters().clear();

				for (ReportDesign design : oldDesignList) {
					try {
						transaction.begin();
						session.delete(design);
						transaction.commit();
					} catch (JDBCException e) {
						transaction.rollback();
						throw new CruxException(e.getSQLException()
								.getMessage(), e);
					}
				}

				for (RowAliasFilter rowFilter : oldRowFilter) {
					try {
						transaction.begin();
						session.delete(rowFilter);
						transaction.commit();
					} catch (JDBCException e) {
						transaction.rollback();
						throw new CruxException(e.getSQLException()
								.getMessage(), e);
					}
				}

				for (ColumnFilter columnFilter : oldColumnFilter) {
					try {
						transaction.begin();
						session.delete(columnFilter);
						transaction.commit();
					} catch (JDBCException e) {
						transaction.rollback();
						throw new CruxException(e.getSQLException()
								.getMessage(), e);
					}
				}

				ArrayList<ReportDesign> newDesignList = new ArrayList<ReportDesign>(
						report.getDesigns());
				for (ReportDesign design : newDesignList) {
					design.setReport(foundReport);
				}

				ArrayList<RowAliasFilter> newRowFilter = new ArrayList<RowAliasFilter>(
						report.getRowAliasFilters());
				for (RowAliasFilter rowFilter : newRowFilter) {
					rowFilter.setReport(foundReport);
				}

				ArrayList<ColumnFilter> newColumnFilter = new ArrayList<ColumnFilter>(
						report.getColumnFilters());
				for (ColumnFilter columnFilter : newColumnFilter) {
					columnFilter.setReport(foundReport);
				}

				foundReport.setColumnFilters(newColumnFilter);
				foundReport.setRowAliasFilters(newRowFilter);
				foundReport.setDesigns(newDesignList);
				foundReport.setName(report.getName());
				foundReport.setReportType(report.getReportType());
				foundReport.setUser(report.getUser());
				return saveReport(foundReport);

			}
			return saveReport(report);
		} else {
			return 0;
		}

	}

	public List<Report> findAll() {
		System.out.println("session is " + session);
		List<Report> result = session.createQuery("from Report order by id")
				.list();
		return result;
	}

	public long saveReport(Report report) throws CruxException {
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
}
