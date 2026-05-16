package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.ProdottoBean;


public interface ProdottoDAO {
	public void doSave(ProdottoBean product) throws SQLException;

	public ProdottoBean doRetrieveByKey(int code) throws SQLException;
	
	public Collection<ProdottoBean> doRetrieveAll(String order) throws SQLException;
	
	public boolean doUpdate(ProdottoBean p) throws SQLException;
}
