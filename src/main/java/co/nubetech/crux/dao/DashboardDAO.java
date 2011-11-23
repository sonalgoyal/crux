package co.nubetech.crux.dao;

import java.util.List;

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.Dashboard;
import co.nubetech.crux.util.CruxException;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class DashboardDAO {

	@SessionTarget
	public Session session;
	@TransactionTarget
	public Transaction transaction;
	
	public Dashboard findById(Long id) {
		Dashboard dashboard = (Dashboard) session.get(
				Dashboard.class, id);
		return dashboard;
	}
	
	public long save(Dashboard dashboard) throws CruxException {
		try {
			transaction.begin();
			session.saveOrUpdate(dashboard);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}
		return dashboard.getId();
	}
	
	public List<Dashboard> findAll() {
		List<Dashboard> result = session.createQuery("from Dashboard")
				.list();
		return result;
	}
}
