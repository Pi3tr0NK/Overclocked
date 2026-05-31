package dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import model.ProdottoBean;


public interface ProdottoDAO {
	public void doSave(ProdottoBean product) throws SQLException;

	public ProdottoBean doRetrieveByKey(int code) throws SQLException;
	
	public Collection<ProdottoBean> doRetrieveAll() throws SQLException;
	
	public boolean doUpdate(ProdottoBean p) throws SQLException;
	
	public boolean setProductStatus(int idProdotto, boolean attivo) throws SQLException;
	
	public Map<String, Integer> doCountProductsByCategory() throws SQLException;
	
	public Collection<ProdottoBean> doRetrieveNovita(int limit) throws SQLException;
	
	public Collection<ProdottoBean> doRetrieveBestseller(int n) throws SQLException;
	
	public Collection<ProdottoBean> doRetrieveCorrelati(int n, int idProdotto, String categoria, double prezzo) throws SQLException;
}
