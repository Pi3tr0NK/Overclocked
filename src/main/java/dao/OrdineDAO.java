package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.CarrelloBean;
import model.OrdineBean;
import model.UtenteBean;
import model.OrdineBean.Stato;


public interface OrdineDAO {
	public void doSave(OrdineBean ordine) throws SQLException;
	
	public OrdineBean doRetrieveByKey(int idOrdine) throws SQLException;

	public Collection<OrdineBean> doRetrieveAll(String stato, int pagina) throws SQLException;
	
	public Collection<OrdineBean> doRetrieveAllByUser(int idUser) throws SQLException;
	
	public boolean doUpdate(OrdineBean o) throws SQLException;
	
	public boolean setOrdineStatus(int idOrdine, Stato stato) throws SQLException;
	
	public int doSaveOrdineCompleto(CarrelloBean cart, UtenteBean utente, int idIndirizzo) throws SQLException;
	
	public Collection<OrdineBean> doRetrieveResiByUser(int idUser) throws SQLException;

	public boolean doRimborsa(int idOrdine, int idUtente) throws SQLException;
	
	public  int doCountByLastMonth() throws SQLException;

	public  int doCount(String stato) throws SQLException;
}	
