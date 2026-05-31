package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.MoboBean;

public interface MoboDAO {
public MoboBean doRetrieveByKey(int idMobo) throws SQLException;
	
	public Collection<MoboBean> doRetrieveAll(String categoria, double prezzo, String marca, String formato, String nvme, int slotram) throws SQLException;
	
	public boolean doUpdate(MoboBean p) throws SQLException;
	
	public boolean setProductStatus(MoboBean ram, boolean attivo) throws SQLException;
}
