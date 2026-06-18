package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.IndirizzoBean;


public interface IndirizzoDAO {
	public int doSave(IndirizzoBean indirizzo) throws SQLException;

	public IndirizzoBean doRetrieveByKey(int id) throws SQLException;

	public Collection<IndirizzoBean> doRetrieveAll() throws SQLException;

	public boolean doUpdate(IndirizzoBean indirizzo) throws SQLException;
}
