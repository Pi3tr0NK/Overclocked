package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.GPUBean;

public interface GPUDAO {
	
	public void doSave(GPUBean gpu) throws SQLException;

	public GPUBean doRetrieveByKey(int code) throws SQLException;
	
	public Collection<GPUBean> doRetrieveAll(String categoria, double prezzo, String marca, String vram, String pcie) throws SQLException;
	
	public boolean doUpdate(GPUBean p) throws SQLException;
	
	public boolean setProductStatus(GPUBean gpu, boolean attivo) throws SQLException;
}
