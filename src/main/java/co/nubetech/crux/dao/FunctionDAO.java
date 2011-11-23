package co.nubetech.crux.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import co.nubetech.crux.model.Function;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class FunctionDAO {
	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;

	public Function findById(Long id) {
		Function function = (Function) session.get(Function.class, id);
		return function;
	}

	public List<Function> findAll() {
		List<Function> result = session.createQuery("from Function").list();
		return result;
	}

}
