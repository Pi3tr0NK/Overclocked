package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.UtenteBean;
import model.UtenteBean.Ruolo;

public interface UtenteDAO 
{
	public void doSave(UtenteBean u) throws SQLException;
	
	public UtenteBean doRetrieveByKey(int id) throws SQLException;
	
	public UtenteBean doRetrieveByEmail(String email) throws SQLException;
	
	public boolean doUpdate(UtenteBean u) throws SQLException;
	
	public UtenteBean checkLogin(String email, String password) throws SQLException;
	
	public Collection<UtenteBean> doRetrieveAll() throws SQLException;
	
	public boolean updatePassword(int idUtente, String newPassword) throws SQLException;
	
	public boolean updateRuolo(int idUtente, Ruolo ruolo) throws SQLException;
}
