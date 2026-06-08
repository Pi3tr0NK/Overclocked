package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.ImmagineBean;

public interface ImmaginiDAO {
	public void doSave(ImmagineBean immagine, int idProdotto) throws SQLException;

	public boolean doDelete(int code) throws SQLException;

	public Collection<ImmagineBean> doRetrieveByProdotto(int idProdotto) throws SQLException;
	
	public void updateImage(int idImmagine, String path, int idProdotto) throws SQLException;
}

