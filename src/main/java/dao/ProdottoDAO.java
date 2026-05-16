package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.OrdineBean.Stato;
import model.ProdottoBean;


public interface ProdottoDAO {
	public void doSave(ProdottoBean product) throws SQLException;

	public ProdottoBean doRetrieveByKey(int code) throws SQLException;
	
	public Collection<ProdottoBean> doRetrieveAll() throws SQLException;
	
	public boolean doUpdate(ProdottoBean p) throws SQLException;
	
	public boolean setProductStatus(int idProdotto, boolean attivo) throws SQLException;
}
