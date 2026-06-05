package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.ChassisBean;

public interface ChassisDAO {
public void doSave(ChassisBean cpu) throws SQLException;
	
	public ChassisBean doRetrieveByKey(int idCPU) throws SQLException;
	
	public Collection<ChassisBean> doRetrieveAll(String cerca, String categoria, String prezzo, String marca, String formato, String colore) throws SQLException;
	
	public boolean doUpdate(ChassisBean p) throws SQLException;
	
	public boolean setProductStatus(ChassisBean cpu, boolean attivo) throws SQLException;
}
