package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.CPUBean;
import model.DissipatoreBean;

public interface DissipatoreDAO {
	public void doSave(DissipatoreBean d) throws SQLException;
	
	public DissipatoreBean doRetrieveByKey(int idDissipatore) throws SQLException;
	
	public Collection<DissipatoreBean> doRetrieveAll(String cerca, String categoria,String prezzo,String marca,String tipo) throws SQLException;
	
	public boolean doUpdate(DissipatoreBean d) throws SQLException;
	
	public boolean setProductStatus(DissipatoreBean d, boolean attivo) throws SQLException;
	
	public Collection<DissipatoreBean> dissipatoriCompatibili(CPUBean cpu) throws SQLException;
}
