package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.OrdineBean;
import model.OrdineBean.Stato;


public interface OrdineDAO {
	public void doSave(OrdineBean ordine) throws SQLException;

	public Collection<OrdineBean> doRetrieveAll() throws SQLException;
	
	public Collection<OrdineBean> doRetrieveAllByUser(int idUser) throws SQLException;
	
	public boolean doUpdate(OrdineBean o) throws SQLException;
	
	public boolean setOrdineStatus(int idOrdine, Stato stato) throws SQLException;
}	
