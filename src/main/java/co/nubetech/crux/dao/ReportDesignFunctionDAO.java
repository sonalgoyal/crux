package co.nubetech.crux.dao;

import java.util.List;

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import co.nubetech.crux.model.ReportDesignFunction;
import co.nubetech.crux.util.CruxException;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class ReportDesignFunctionDAO {
	
	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;
	
	public ReportDesignFunction findById(Long id) {
		ReportDesignFunction reportDesignFunction = (ReportDesignFunction) session.get(ReportDesignFunction.class, id);
		return reportDesignFunction;
	}
	
	public long save(ReportDesignFunction reportDesignFunction) throws CruxException{
		try {
			transaction.begin();
			session.saveOrUpdate(reportDesignFunction);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}
		return reportDesignFunction.getId();
	}
	
	public long delete(ReportDesignFunction reportDesignFunction) throws CruxException{
		long id = reportDesignFunction.getId();
		try {
			transaction.begin();
			session.delete(reportDesignFunction);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(e.getSQLException().getMessage(), e);
		}
		return id;
	}
	
	public List<ReportDesignFunction> findAll() {
		List<ReportDesignFunction> result = session.createQuery("from ReportDesignFunction").list();
		return result;
	}

}
