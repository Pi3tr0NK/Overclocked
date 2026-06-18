package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.DissipatoreBean;

public interface DissipatoreDAO {
	public void doSave(DissipatoreBean d) throws SQLException;
	
	public DissipatoreBean doRetrieveByKey(int idDissipatore) throws SQLException;
	
	public Collection<DissipatoreBean> doRetrieveAll(String cerca, String categoria,String prezzo,String marca,String tipo, String ordine, int pagina) throws SQLException;
	
	public boolean doUpdate(DissipatoreBean d) throws SQLException;
	
	public boolean setProductStatus(DissipatoreBean d, boolean attivo) throws SQLException;
	
	public Collection<DissipatoreBean> dissipatoriCompatibili(int cpuId) throws SQLException;
	
	public int doCountFilteredProducts(String cerca, String categoria,String prezzo,String marca,String tipo) throws SQLException;
}

