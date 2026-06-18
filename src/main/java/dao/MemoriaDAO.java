package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.MemoriaBean;

public interface MemoriaDAO {
public void doSave(MemoriaBean cpu) throws SQLException;
	
	public MemoriaBean doRetrieveByKey(int idCPU) throws SQLException;
	
	public Collection<MemoriaBean> doRetrieveAll(String cerca, String categoria, String prezzo, String marca, String capacita, String tipo, String tecnologia, String ordine, int pagina) throws SQLException;
	
	public boolean doUpdate(MemoriaBean p) throws SQLException;
	
	public boolean setProductStatus(MemoriaBean cpu, boolean attivo) throws SQLException;
	
	public Collection<MemoriaBean> memoriaCompatibili(int moboId) throws SQLException;
	
	public int doCountFilteredProducts(String cerca, String categoria, String prezzo, String marca, String capacita, String tipo, String tecnologia) throws SQLException;
}
