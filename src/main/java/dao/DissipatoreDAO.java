package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.DissipatoreBean;

public interface DissipatoreDAO {
	public void doSave(DissipatoreBean d) throws SQLException;
	
	public DissipatoreBean doRetrieveByKey(int idDissipatore) throws SQLException;
	
	public Collection<DissipatoreBean> doRetrieveAll(String categoria,double prezzo,String marca,String tipo) throws SQLException;
	
	public boolean doUpdate(DissipatoreBean d) throws SQLException;
	
	public boolean setProductStatus(DissipatoreBean d, boolean attivo) throws SQLException;
}
