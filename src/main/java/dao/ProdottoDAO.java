package dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import model.ProdottoBean;


public interface ProdottoDAO {
	public int doSave(ProdottoBean product) throws SQLException;

	public ProdottoBean doRetrieveByKey(int code) throws SQLException;
	
	public Collection<ProdottoBean> doRetrieveAll(String cerca, String prezzo, String marca, String categoria, String attivo, int pagina) throws SQLException;
	
	public boolean doUpdate(ProdottoBean p) throws SQLException;
	
	public boolean setProductStatus(int idProdotto, boolean attivo) throws SQLException;
	
	public Map<String, Integer> doCountProductsByCategory() throws SQLException;
	
	public Collection<ProdottoBean> doRetrieveNovita(int limit) throws SQLException;
	
	public Collection<ProdottoBean> doRetrieveBestseller(int n) throws SQLException;
	
	public Collection<ProdottoBean> doRetrieveCorrelati(int n, int idProdotto, String categoria, double prezzo) throws SQLException;
	
	public int doCountProducts() throws SQLException;
	
	public int doCountExpiredProducts() throws SQLException;
	
	public int doCountFilteredProducts(String cerca, String prezzo, String marca, String categoria, String attivo) throws SQLException;
}
