package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.MoboBean;
import model.RAMBean;

public interface RAMDAO {
public void doSave(RAMBean ram) throws SQLException;
	
	public RAMBean doRetrieveByKey(int idRAM) throws SQLException;
	
	public Collection<RAMBean> doRetrieveAll(String cerca, String categoria, String prezzo, String marca, String capacita, String frequenza, String tipo) throws SQLException;
	
	public boolean doUpdate(RAMBean p) throws SQLException;
	
	public boolean setProductStatus(RAMBean ram, boolean attivo) throws SQLException;
	
	public Collection<RAMBean> ramCompatibili(MoboBean mobo) throws SQLException;
}
