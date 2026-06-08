package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.MemoriaBean;
import model.MoboBean;

public interface MemoriaDAO {
public void doSave(MemoriaBean cpu) throws SQLException;
	
	public MemoriaBean doRetrieveByKey(int idCPU) throws SQLException;
	
	public Collection<MemoriaBean> doRetrieveAll(String cerca, String categoria, String prezzo, String marca, String capacita, String tipo, String tecnologia) throws SQLException;
	
	public boolean doUpdate(MemoriaBean p) throws SQLException;
	
	public boolean setProductStatus(MemoriaBean cpu, boolean attivo) throws SQLException;
	
	public Collection<MemoriaBean> moboCompatibili(MoboBean mobo) throws SQLException;
}
