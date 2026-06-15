package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.PSUBean;

public interface PSUDAO {
	
	public void doSave(PSUBean cpu) throws SQLException;
	
	public PSUBean doRetrieveByKey(int idPSU) throws SQLException;
	
	public Collection<PSUBean> doRetrieveAll(String cerca, String categoria, String prezzo,String marca, String potenza, String certificazione, String modulare, int pagina) throws SQLException;
	
	public boolean doUpdate(PSUBean p) throws SQLException;
	
	public boolean setProductStatus(PSUBean psu, boolean attivo) throws SQLException;
	
	public Collection<PSUBean> psuCompatibili(int cpuId, int gpuId) throws SQLException;
	
	public int doCountFilteredProducts(String cerca, String categoria, String prezzo,String marca, String potenza, String certificazione, String modulare) throws SQLException;
}
