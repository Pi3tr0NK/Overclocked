package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.CPUBean;

public interface CPUDAO {
	public void doSave(CPUBean cpu) throws SQLException;
	
	public CPUBean doRetrieveByKey(int idCPU) throws SQLException;
	
	public Collection<CPUBean> doRetrieveAll(String cerca, String categoria, String prezzo, String marca, String core, String frequenza) throws SQLException;
	
	public boolean doUpdate(CPUBean p) throws SQLException;
	
	public boolean setProductStatus(CPUBean cpu, boolean attivo) throws SQLException;
}
