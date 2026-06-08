package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.CPUBean;
import model.MoboBean;

public interface MoboDAO {
	public MoboBean doRetrieveByKey(int idMobo) throws SQLException;
	
	public Collection<MoboBean> doRetrieveAll(String cerca, String categoria, String prezzo, String marca, String formato, String nvme, String slotram) throws SQLException;
	
	public boolean doUpdate(MoboBean p) throws SQLException;
	
	public boolean setProductStatus(MoboBean ram, boolean attivo) throws SQLException;
	
	public Collection<MoboBean> moboCompatibili(CPUBean cpu) throws SQLException;
}
