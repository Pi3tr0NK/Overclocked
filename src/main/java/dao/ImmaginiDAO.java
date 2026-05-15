package dao;

import java.awt.List;
import java.sql.SQLException;
import java.util.Collection;

import model.ImmagineBean;
import model.ProdottoBean;

public interface ImmaginiDAO {
	public void doSave(ImmagineBean product);

	public boolean doDelete(int code);

	public Collection<ImmagineBean> doRetrieveByProdotto(int idProdotto);
	
	public void updateImage(int idProdotto, String path);
}

